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

package tech.ordinaryroad.live.chat.client.bilibili.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.GlobalHeaders;
import cn.hutool.http.Header;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliConnectionListener;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliConnectionHandler;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliLiveChatClientChannelInitializer;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.BilibiliApis;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliCmdEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.base.IBilibiliMsg;
import tech.ordinaryroad.live.chat.client.codec.bilibili.room.BilibiliRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCollUtil;
import tech.ordinaryroad.live.chat.client.plugin.forward.ForwardMsgPlugin;
import tech.ordinaryroad.live.chat.client.servers.netty.client.base.BaseNettyClient;

import java.util.List;
import java.util.function.Consumer;

/**
 * B站直播间弹幕客户端
 *
 * @author mjz
 * @date 2023/8/20
 */
@Slf4j
public class BilibiliLiveChatClient extends BaseNettyClient<
        BilibiliLiveChatClientConfig,
        BilibiliRoomInitResult,
        BilibiliCmdEnum,
        IBilibiliMsg,
        IBilibiliMsgListener,
        BilibiliConnectionHandler,
        BilibiliBinaryFrameHandler
        > {

    public BilibiliLiveChatClient(BilibiliLiveChatClientConfig config, List<IBilibiliMsgListener> msgListeners, IBilibiliConnectionListener connectionListener, EventLoopGroup workerGroup) {
        super(config, workerGroup, connectionListener, IBilibiliMsgListener.class);
        addMsgListeners(msgListeners);

        // 初始化
        this.init();
    }

    public BilibiliLiveChatClient(BilibiliLiveChatClientConfig config, IBilibiliMsgListener msgListener, IBilibiliConnectionListener connectionListener, EventLoopGroup workerGroup) {
        super(config, workerGroup, connectionListener, IBilibiliMsgListener.class);
        addMsgListener(msgListener);

        // 初始化
        this.init();
    }

    public BilibiliLiveChatClient(BilibiliLiveChatClientConfig config, IBilibiliMsgListener msgListener, IBilibiliConnectionListener connectionListener) {
        this(config, msgListener, connectionListener, new NioEventLoopGroup());
    }

    public BilibiliLiveChatClient(BilibiliLiveChatClientConfig config, IBilibiliMsgListener msgListener) {
        this(config, msgListener, null, new NioEventLoopGroup());
    }

    public BilibiliLiveChatClient(BilibiliLiveChatClientConfig config) {
        this(config, null);
    }

    @Override
    public void init() {
        // TODO remove this
        addPlugin(new ForwardMsgPlugin(getConfig().getForwardWebsocketUri()));
        super.init();
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline().addLast(new BilibiliLiveChatClientChannelInitializer(this));
    }

    @Override
    public BilibiliRoomInitResult initRoom() {
        return BilibiliApis.roomInit(getConfig().getRoomId(), getConfig().getCookie(), roomInitResult);
    }

    @Override
    public BilibiliConnectionHandler initConnectionHandler(IBaseConnectionListener<BilibiliConnectionHandler> clientConnectionListener) {
        return new BilibiliConnectionHandler(() -> {
            DefaultHttpHeaders headers = new DefaultHttpHeaders();
            headers.add(Header.USER_AGENT.name(), GlobalHeaders.INSTANCE.header(Header.USER_AGENT));
            headers.add(Header.ORIGIN.name(), "https://live.bilibili.com");
            headers.add(Header.PRAGMA.name(), "no-cache");
            headers.set(Header.HOST.name(), getWebsocketUri().getHost() + ":" + getWebsocketUri().getPort());
            return new WebSocketClientProtocolHandler(
                    WebSocketClientProtocolConfig.newBuilder()
                            .webSocketUri(getWebsocketUri())
                            .version(WebSocketVersion.V13)
                            .subprotocol(null)
                            .allowExtensions(true)
                            .customHeaders(headers)
                            .maxFramePayloadLength(getConfig().getMaxFramePayloadLength())
                            .handshakeTimeoutMillis(getConfig().getHandshakeTimeoutMillis())
                            .build()
            );
        }, BilibiliLiveChatClient.this, clientConnectionListener);
    }

    @Override
    protected String getWebSocketUriString() {
        String webSocketUriString = super.getWebSocketUriString();
        if (StrUtil.isNotBlank(webSocketUriString)) {
            return webSocketUriString;
        }
        BilibiliApis.Host_list hostList = OrLiveChatCollUtil.getRandom(roomInitResult.getDanmuinfoResult().getHost_list());
        return StrUtil.format("wss://{}:{}/sub", hostList.getHost(), hostList.getWss_port());
    }

    private void sendDanmuInternal(String msg, long replyUid, Runnable success, Consumer<Throwable> failed) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("{} bilibili发送弹幕 {}", getConfig().getRoomId(), danmu);
            }

            boolean sendSuccess = false;
            try {
                BilibiliApis.sendMsg(msg, roomInitResult.getRoomPlayInfoResult().getRoom_id(), replyUid, getConfig().getCookie());
                sendSuccess = true;
            } catch (Exception e) {
                log.error("bilibili弹幕发送失败", e);
                if (failed != null) {
                    failed.accept(e);
                }
            }
            if (!sendSuccess) {
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("bilibili弹幕发送成功 {}", danmu);
            }
            if (success != null) {
                success.run();
            }
            finishSendDanmu();
        } catch (Exception e) {
            log.error("bilibili弹幕发送失败", e);
            if (failed != null) {
                failed.accept(e);
            }
        }
    }

    @Override
    public void sendDanmu(Object danmu, Runnable success, Consumer<Throwable> failed) {
        if (!checkCanSendDanmu(false)) {
            return;
        }
        if (danmu instanceof String) {
            String msg = (String) danmu;
            sendDanmuInternal(msg, 0L, success, failed);
        } else if (danmu instanceof SendDanmuRequest) {
            SendDanmuRequest sendDanmuRequest = (SendDanmuRequest) danmu;
            String msg = sendDanmuRequest.getMsg();
            long replyUid = sendDanmuRequest.getReplyUid();
            sendDanmuInternal(msg, replyUid, success, failed);
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
            BilibiliApis.likeReportV3(roomInitResult.getRoomPlayInfoResult().getUid(), roomInitResult.getRoomPlayInfoResult().getRoom_id(), getConfig().getCookie());
            successfullyClicked = true;
        } catch (Exception e) {
            log.error("Bilibili为直播间点赞失败", e);
            if (failed != null) {
                failed.accept(e);
            }
        }
        if (!successfullyClicked) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Bilibili为直播间点赞成功");
        }
        if (success != null) {
            success.run();
        }
    }
}
