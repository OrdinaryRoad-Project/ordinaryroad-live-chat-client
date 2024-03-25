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

package tech.ordinaryroad.live.chat.client.kuaishou.client;


import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCollUtil;
import tech.ordinaryroad.live.chat.client.kuaishou.api.KuaishouApis;
import tech.ordinaryroad.live.chat.client.kuaishou.config.KuaishouLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouConnectionListener;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.impl.KuaishouForwardMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.msg.base.IKuaishouMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouConnectionHandler;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouLiveChatClientChannelInitializer;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.PayloadTypeOuterClass;
import tech.ordinaryroad.live.chat.client.servers.netty.client.base.BaseNettyClient;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author mjz
 * @date 2024/1/5
 */
@Slf4j
public class KuaishouLiveChatClient extends BaseNettyClient<
        KuaishouLiveChatClientConfig,
        PayloadTypeOuterClass.PayloadType,
        IKuaishouMsg,
        IKuaishouMsgListener,
        KuaishouConnectionHandler,
        KuaishouBinaryFrameHandler> {

    @Getter
    KuaishouApis.RoomInitResult roomInitResult = new KuaishouApis.RoomInitResult();

    public KuaishouLiveChatClient(KuaishouLiveChatClientConfig config, List<IKuaishouMsgListener> msgListeners, IKuaishouConnectionListener connectionListener, EventLoopGroup workerGroup) {
        super(config, workerGroup, connectionListener);
        addMsgListeners(msgListeners);

        // 初始化
        this.init();
    }

    public KuaishouLiveChatClient(KuaishouLiveChatClientConfig config, IKuaishouMsgListener msgListener, IKuaishouConnectionListener connectionListener, EventLoopGroup workerGroup) {
        super(config, workerGroup, connectionListener);
        addMsgListener(msgListener);

        // 初始化
        this.init();
    }

    public KuaishouLiveChatClient(KuaishouLiveChatClientConfig config, IKuaishouMsgListener msgListener, IKuaishouConnectionListener connectionListener) {
        this(config, msgListener, connectionListener, new NioEventLoopGroup());
    }

    public KuaishouLiveChatClient(KuaishouLiveChatClientConfig config, IKuaishouMsgListener msgListener) {
        this(config, msgListener, null, new NioEventLoopGroup());
    }

    public KuaishouLiveChatClient(KuaishouLiveChatClientConfig config) {
        this(config, null);
    }

    @Override
    public void init() {
        roomInitResult = KuaishouApis.roomInit(getConfig().getRoomId(), getConfig().getCookie());
        if (StrUtil.isNotBlank(getConfig().getForwardWebsocketUri())) {
            KuaishouForwardMsgListener forwardMsgListener = new KuaishouForwardMsgListener(getConfig().getForwardWebsocketUri());
            addMsgListener(forwardMsgListener);
            addStatusChangeListener((evt, oldStatus, newStatus) -> {
                if (newStatus == ClientStatusEnums.DESTROYED) {
                    forwardMsgListener.destroyForwardClient();
                }
            });
        }
        super.init();
    }

    @Override
    protected String getWebSocketUriString() {
        String webSocketUriString = super.getWebSocketUriString();
        if (StrUtil.isNotBlank(webSocketUriString)) {
            return webSocketUriString;
        }
        return OrLiveChatCollUtil.getRandom(roomInitResult.getWebsocketUrls());
    }

    @Override
    public KuaishouConnectionHandler initConnectionHandler(IBaseConnectionListener<KuaishouConnectionHandler> clientConnectionListener) {
        return new KuaishouConnectionHandler(
                () -> new WebSocketClientProtocolHandler(
                        WebSocketClientProtocolConfig.newBuilder()
                                .webSocketUri(getWebsocketUri())
                                .version(WebSocketVersion.V13)
                                .subprotocol(null)
                                .allowExtensions(true)
                                .customHeaders(new DefaultHttpHeaders())
                                .maxFramePayloadLength(getConfig().getMaxFramePayloadLength())
                                .handshakeTimeoutMillis(getConfig().getHandshakeTimeoutMillis())
                                .build()
                ),
                KuaishouLiveChatClient.this, clientConnectionListener
        );
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline().addLast(new KuaishouLiveChatClientChannelInitializer(KuaishouLiveChatClient.this));
    }

    @Override
    public void sendDanmu(Object danmu, Runnable success, Consumer<Throwable> failed) {
        if (!checkCanSendDanmu()) {
            return;
        }
        if (danmu instanceof String) {
            String msg = (String) danmu;
            try {
                if (log.isDebugEnabled()) {
                    log.debug("{} kuaishou发送弹幕 {}", getConfig().getRoomId(), danmu);
                }

                boolean sendSuccess = false;
                try {
                    KuaishouApis.sendComment(getConfig().getCookie(),
                            getConfig().getRoomId(),
                            KuaishouApis.SendCommentRequest.builder()
                                    .liveStreamId(roomInitResult.getLiveStreamId())
                                    .content(msg)
                                    .build()
                    );
                    sendSuccess = true;
                } catch (Exception e) {
                    log.error("kuaishou弹幕发送失败", e);
                    if (failed != null) {
                        failed.accept(e);
                    }
                }
                if (!sendSuccess) {
                    return;
                }

                if (log.isDebugEnabled()) {
                    log.debug("kuaishou弹幕发送成功 {}", danmu);
                }
                if (success != null) {
                    success.run();
                }
                finishSendDanmu();
            } catch (Exception e) {
                log.error("kuaishou弹幕发送失败", e);
                if (failed != null) {
                    failed.accept(e);
                }
            }
        } else {
            super.sendDanmu(danmu, success, failed);
        }
    }

    @Override
    public void clickLike(int count, Runnable success, Consumer<Throwable> failed) {
        if (count <= 0) {
            throw new BaseException("点赞次数必须大于0");
        }

        boolean successfullyClicked = false;
        try {
            JsonNode jsonNode = KuaishouApis.clickLike(getConfig().getCookie(), getConfig().getRoomId(), roomInitResult.getLiveStreamId(), count);
            if (jsonNode.asBoolean()) {
                successfullyClicked = true;
            }
        } catch (Exception e) {
            log.error("kuaishou为直播间点赞失败", e);
            if (failed != null) {
                failed.accept(e);
            }
        }
        if (!successfullyClicked) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("kuaishou为直播间点赞成功");
        }
        if (success != null) {
            success.run();
        }
    }
}
