/*
 * MIT License
 *
 * Copyright (c) 2023 OrdinaryRoad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tech.ordinaryroad.live.chat.client.douyin.browser;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.ruiyun.jvppeteer.api.core.CDPSession;
import com.ruiyun.jvppeteer.api.events.ConnectionEvents;
import com.ruiyun.jvppeteer.api.events.PageEvents;
import com.ruiyun.jvppeteer.cdp.core.CdpRequest;
import com.ruiyun.jvppeteer.cdp.entities.Cookie;
import com.ruiyun.jvppeteer.cdp.entities.CookieParam;
import com.ruiyun.jvppeteer.cdp.entities.GoToOptions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.douyin.api.DouyinApis;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinCmdEnum;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinPayloadTypeEnum;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.*;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.base.IDouyinMsg;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.*;
import tech.ordinaryroad.live.chat.client.codec.douyin.room.DouyinRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener;
import tech.ordinaryroad.tech.live.chat.client.servers.puppeteer.client.BaseBrowserClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author mjz
 * @date 2024/1/2
 */
@Slf4j
public class DouyinBrowserLiveChatClient extends BaseBrowserClient<DouyinBrowserLiveChatClientConfig, DouyinRoomInitResult, DouyinCmdEnum, IDouyinMsg, IDouyinMsgListener> {

    public DouyinBrowserLiveChatClient(DouyinBrowserLiveChatClientConfig config, List<IDouyinMsgListener> msgListeners) {
        super(config, IDouyinMsgListener.class);
        addMsgListeners(msgListeners);

        // 初始化
        this.init();
    }

    public DouyinBrowserLiveChatClient(DouyinBrowserLiveChatClientConfig config, IDouyinMsgListener msgListener) {
        super(config, IDouyinMsgListener.class);
        addMsgListener(msgListener);

        // 初始化
        this.init();
    }

    public DouyinBrowserLiveChatClient(DouyinBrowserLiveChatClientConfig config) {
        this(config, Collections.emptyList());
    }

    @Override
    public DouyinRoomInitResult initRoom() {
        return DouyinApis.roomInit(getConfig().getRoomId(), getConfig().getCookie(), roomInitResult);
    }

    @Override
    public void connect(Runnable success, Consumer<Throwable> failed) {
        super.connect(success, failed);

        CDPSession cdpSession = page.createCDPSession();
        cdpSession.send("Debugger.enable");
        cdpSession.send("Network.enable");

        try {
            Map<String, String> cookieMap = OrLiveChatCookieUtil.parseCookieString(getConfig().getCookie());
            if (MapUtil.isNotEmpty(cookieMap)) {
                // 清空Cookie
                List<Cookie> cookies = page.cookies();
                String[] cookieNames = cookies.stream().map(Cookie::getName).toArray(String[]::new);
                page.deleteCookie(cookieNames);

                // 设置Cookie
                CookieParam[] cookieArray = cookieMap.entrySet().stream().map(entry -> {
                    CookieParam cookieParam = new CookieParam();
                    cookieParam.setName(entry.getKey());
                    cookieParam.setValue(entry.getValue());
                    cookieParam.setDomain(".douyin.com");
                    cookieParam.setPath("/");
                    return cookieParam;
                }).toArray(CookieParam[]::new);
                page.setCookie(cookieArray);
            }
        } catch (Exception e) {
            log.error("Cookie设置失败", e);
        }

        page.on(PageEvents.RequestFinished, e -> {
            CdpRequest event = (CdpRequest) e;
            String url = event.url();
            // 轮循弹幕
            if (url.contains("/webcast/im/fetch")) {
                byte[] content = event.response().content();
                if (content.length > 0) {
                    try {
                        Response response = Response.parseFrom(content);
                        handleMsg(response.getMessagesListList().stream().map(DouyinCmdMsg::new).collect(Collectors.toList()));
                    } catch (Exception exception) {
                        log.error("解析弹幕失败", exception);
                    }
                }
            }
        });

        // 弹幕WebSocket请求ID
        AtomicReference<String> imRequestId = new AtomicReference<>("");
        cdpSession.on(ConnectionEvents.Network_webSocketCreated, (e) -> {
            JsonNode event = (JsonNode) e;
            String url = event.get("url").asText();
            if (url.contains("/webcast/im/push/")) {
                imRequestId.set(event.get("requestId").asText());
            }
        });

        cdpSession.on(ConnectionEvents.Network_webSocketFrameReceived, (e) -> {
            JsonNode event = (JsonNode) e;
            String requestId = event.get("requestId").asText();
            if (Objects.equals(imRequestId.get(), requestId)) {
                String text = event.get("response").get("payloadData").asText();
                byte[] decode = Base64.decode(text);
                try {
                    PushFrame pushFrame = PushFrame.parseFrom(decode);
                    decode(pushFrame);
                } catch (Exception exception) {
                    log.error("解析弹幕失败", exception);
                }
            }
        });

        GoToOptions goToOptions = new GoToOptions();
        goToOptions.setTimeout((int) TimeUnit.MINUTES.toMillis(3));
        try {
            page.goTo(StrUtil.format("https://live.douyin.com/{}", getConfig().getRoomId()), goToOptions);
            setStatus(ClientStatusEnums.CONNECTED);
        } catch (Exception e) {
            log.error("导航到直播间页面失败", e);
            setStatus(ClientStatusEnums.CONNECT_FAILED);
        }
    }

    @SneakyThrows
    private void decode(PushFrame pushFrame) {
        byte[] bytes;
        String compressType = null;
        if (CollUtil.isNotEmpty(pushFrame.getHeadersListList())) {
            for (HeadersList headersList : pushFrame.getHeadersListList()) {
                if ("compress_type".equals(headersList.getKey())) {
                    compressType = headersList.getValue();
                }
            }
        }
        // 无压缩
        if (StrUtil.isBlank(compressType) || "none".equals(compressType)) {
            bytes = pushFrame.getPayload().toByteArray();
        }
        // gzip
        else if ("gzip".equalsIgnoreCase(compressType)) {
            ByteString payload = pushFrame.getPayload();
            bytes = ZipUtil.unGzip(payload.newInput());
        }
        // 暂不支持
        else {
            if (log.isWarnEnabled()) {
                log.warn("暂不支持的压缩方式: {}", compressType);
            }
            return;
        }

        String payloadType = pushFrame.getPayloadType();
        DouyinPayloadTypeEnum payloadTypeEnum = DouyinPayloadTypeEnum.getByCode(payloadType);
        if (payloadTypeEnum == null) {
            if (log.isDebugEnabled()) {
                log.debug("暂不支持的payloadType: {}", payloadType);
            }
            return;
        }

        switch (payloadTypeEnum) {
            case MSG: {
                Response response = Response.parseFrom(bytes);
                handleMsg(response.getMessagesListList().stream().map(DouyinCmdMsg::new).collect(Collectors.toList()));
                return;
            }
            default: {
                // ignore
            }
        }
    }

    private void handleMsg(List<DouyinCmdMsg> msgList) {
        for (DouyinCmdMsg douyinCmdMsg : msgList) {
            if (douyinCmdMsg == null) {
                continue;
            }

            ByteString payload = douyinCmdMsg.getMsg().getPayload();
            DouyinCmdEnum cmdEnum = douyinCmdMsg.getCmdEnum();
            if (cmdEnum == null) {
                iteratorMsgListeners(msgListener -> msgListener.onUnknownCmd(null, douyinCmdMsg.getCmd(), douyinCmdMsg));
            } else {
                iteratorMsgListeners(msgListener -> msgListener.onCmdMsg(null, cmdEnum, douyinCmdMsg));

                switch (cmdEnum) {
                    case WebcastChatMessage: {
                        try {
                            ChatMessage chatMessage = ChatMessage.parseFrom(payload);
                            DouyinDanmuMsg msg = new DouyinDanmuMsg(chatMessage);
                            iteratorMsgListeners(msgListener -> msgListener.onDanmuMsg(null, msg));
                        } catch (IOException e) {
                            throw new BaseException(e);
                        }
                        break;
                    }
                    case WebcastGiftMessage: {
                        try {
                            GiftMessage giftMessage = GiftMessage.parseFrom(payload);
                            DouyinGiftMsg msg = new DouyinGiftMsg(giftMessage);
                            // 计算礼物个数
                            DouyinApis.calculateGiftCount(msg, getConfig().getGiftCountCalculationTime());
                            iteratorMsgListeners(msgListener -> msgListener.onGiftMsg(null, msg));
                        } catch (InvalidProtocolBufferException e) {
                            throw new BaseException(e);
                        }
                        break;
                    }
                    case WebcastMemberMessage: {
                        try {
                            MemberMessage memberMessage = MemberMessage.parseFrom(payload);
                            DouyinEnterRoomMsg msg = new DouyinEnterRoomMsg(memberMessage);
                            iteratorMsgListeners(msgListener -> msgListener.onEnterRoomMsg(null, msg));
                        } catch (InvalidProtocolBufferException e) {
                            throw new BaseException(e);
                        }
                        break;
                    }
                    case WebcastLikeMessage: {
                        try {
                            LikeMessage likeMessage = LikeMessage.parseFrom(payload);
                            DouyinLikeMsg msg = new DouyinLikeMsg(likeMessage);

                            DouyinRoomStatsMsg douyinRoomStatsMsg = new DouyinRoomStatsMsg();
                            douyinRoomStatsMsg.setLikedCount(NumberUtil.toStr(likeMessage.getTotal()));

                            iteratorMsgListeners(msgListener -> {
                                msgListener.onLikeMsg(null, msg);
                                msgListener.onRoomStatsMsg(null, douyinRoomStatsMsg);
                            });
                        } catch (InvalidProtocolBufferException e) {
                            throw new BaseException(e);
                        }
                        break;
                    }

                    case WebcastControlMessage: {
                        try {
                            ControlMessage controlMessage = ControlMessage.parseFrom(payload);
                            DouyinControlMsg msg = new DouyinControlMsg(controlMessage);
                            iteratorMsgListeners(msgListener -> msgListener.onLiveStatusMsg(null, msg));
                        } catch (InvalidProtocolBufferException e) {
                            throw new BaseException(e);
                        }
                        break;
                    }

                    case WebcastSocialMessage: {
                        try {
                            SocialMessage socialMessage = SocialMessage.parseFrom(payload);
                            DouyinSocialMsg msg = new DouyinSocialMsg(socialMessage);
                            iteratorMsgListeners(msgListener -> msgListener.onSocialMsg(null, msg));
                        } catch (InvalidProtocolBufferException e) {
                            throw new BaseException(e);
                        }
                        break;
                    }

                    case WebcastRoomStatsMessage: {
                        try {
                            RoomStatsMessage roomStatsMessage = RoomStatsMessage.parseFrom(payload);
                            DouyinRoomStatsMsg douyinRoomStatsMsg = new DouyinRoomStatsMsg();
                            douyinRoomStatsMsg.setMsg(roomStatsMessage);
                            iteratorMsgListeners(msgListener -> msgListener.onRoomStatsMsg(null, douyinRoomStatsMsg));
                        } catch (InvalidProtocolBufferException e) {
                            throw new BaseException(e);
                        }
                        break;
                    }
                    default: {
                        iteratorMsgListeners(msgListener -> msgListener.onOtherCmdMsg(null, cmdEnum, douyinCmdMsg));
                    }
                }
            }
        }
    }
}
