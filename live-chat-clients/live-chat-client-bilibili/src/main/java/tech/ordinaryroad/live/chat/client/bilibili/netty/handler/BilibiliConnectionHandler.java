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

package tech.ordinaryroad.live.chat.client.bilibili.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.BilibiliApis;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.ProtoverEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.factory.BilibiliMsgFactory;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientConnectionHandler;

import java.util.function.Supplier;


/**
 * 连接处理器
 *
 * @author mjz
 * @date 2023/8/21
 */
@Slf4j
@ChannelHandler.Sharable
public class BilibiliConnectionHandler extends BaseNettyClientConnectionHandler<BilibiliLiveChatClient, BilibiliConnectionHandler> {

    /**
     * 以ClientConfig为主
     */
    private final long roomId;
    /**
     * 以ClientConfig为主
     */
    private final ProtoverEnum protover;
    /**
     * 以ClientConfig为主
     */
    private String cookie;

    /**
     * 发送认证包需要
     * 以ClientConfig为主
     */
    private final BilibiliApis.RoomInitResult roomInitResult;

    public BilibiliConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, BilibiliLiveChatClient client, IBaseConnectionListener<BilibiliConnectionHandler> listener) {
        super(webSocketProtocolHandler, client, listener);
        this.roomInitResult = client.getRoomInitResult();
        this.roomId = client.getConfig().getRoomId();
        this.protover = client.getConfig().getProtover();
        this.cookie = client.getConfig().getCookie();
    }

    public BilibiliConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, BilibiliLiveChatClient client) {
        this(webSocketProtocolHandler, client, null);
    }

    public BilibiliConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, BilibiliApis.RoomInitResult roomInitResult, long roomId, ProtoverEnum protover, IBaseConnectionListener<BilibiliConnectionHandler> listener, String cookie) {
        super(webSocketProtocolHandler, listener);
        this.roomInitResult = roomInitResult;
        this.roomId = roomId;
        this.protover = protover;
        this.cookie = cookie;
    }

    public BilibiliConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, BilibiliApis.RoomInitResult roomInitResult, long roomId, ProtoverEnum protover, IBaseConnectionListener<BilibiliConnectionHandler> listener) {
        this(webSocketProtocolHandler, roomInitResult, roomId, protover, listener, null);
    }

    public BilibiliConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, BilibiliApis.RoomInitResult roomInitResult, long roomId, ProtoverEnum protover, String cookie) {
        this(webSocketProtocolHandler, roomInitResult, roomId, protover, null, cookie);
    }

    public BilibiliConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, BilibiliApis.RoomInitResult roomInitResult, long roomId, ProtoverEnum protover) {
        this(webSocketProtocolHandler, roomInitResult, roomId, protover, null, null);
    }

    @Override
    public void sendHeartbeat(Channel channel) {
        if (log.isDebugEnabled()) {
            log.debug("发送心跳包");
        }
        channel.writeAndFlush(
                getWebSocketFrameFactory(getRoomId()).createHeartbeat(getProtover())
        ).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("心跳包发送完成");
                }
            } else {
                log.error("心跳包发送失败", future.cause());
            }
        });
    }

    private static BilibiliMsgFactory getWebSocketFrameFactory(long roomId) {
        return BilibiliMsgFactory.getInstance(roomId);
    }

    @Override
    public void sendAuthRequest(Channel channel) {
        // 5s内认证
        if (log.isDebugEnabled()) {
            log.debug("发送认证包");
        }
        channel.writeAndFlush(getWebSocketFrameFactory(getRoomId()).createAuth(getProtover(), roomInitResult)).addListener(future -> {
            if (future.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("认证包发送完成");
                }
            } else {
                log.error("认证包发送失败", future.cause());
            }
        });
    }

    public long getRoomId() {
        return client != null ? client.getConfig().getRoomId() : roomId;
    }

    private ProtoverEnum getProtover() {
        return client != null ? client.getConfig().getProtover() : protover;
    }

    private String getCookie() {
        return client != null ? client.getConfig().getCookie() : cookie;
    }
}
