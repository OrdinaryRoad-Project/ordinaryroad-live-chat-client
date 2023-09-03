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

package tech.ordinaryroad.live.chat.client.douyu.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.douyu.client.DouyuLiveChatClient;
import tech.ordinaryroad.live.chat.client.douyu.config.DouyuLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyu.netty.frame.factory.DouyuWebSocketFrameFactory;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientConnectionHandler;


/**
 * 连接处理器
 *
 * @author mjz
 * @date 2023/8/21
 */
@Slf4j
@ChannelHandler.Sharable
public class DouyuConnectionHandler extends BaseNettyClientConnectionHandler<DouyuLiveChatClient, DouyuConnectionHandler> {

    /**
     * 以ClientConfig为主
     */
    private final long roomId;
    /**
     * 以ClientConfig为主
     */
    private final String ver;
    /**
     * 以ClientConfig为主
     */
    private final String aver;
    /**
     * 以ClientConfig为主
     */
    private String cookie;

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, DouyuLiveChatClient client, IBaseConnectionListener<DouyuConnectionHandler> listener) {
        super(handshaker, client, listener);
        this.roomId = client.getConfig().getRoomId();
        this.ver = client.getConfig().getVer();
        this.aver = client.getConfig().getAver();
        this.cookie = client.getConfig().getCookie();
    }

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, DouyuLiveChatClient client) {
        this(handshaker, client, null);
    }

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, long roomId, String ver, String aver, IBaseConnectionListener<DouyuConnectionHandler> listener, String cookie) {
        super(handshaker, listener);
        this.roomId = roomId;
        this.ver = ver;
        this.aver = aver;
        this.cookie = cookie;
    }

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, long roomId, String ver, String aver, IBaseConnectionListener<DouyuConnectionHandler> listener) {
        this(handshaker, roomId, ver, aver, listener, null);
    }

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, long roomId, String ver, String aver, String cookie) {
        this(handshaker, roomId, ver, aver, null, cookie);
    }

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, long roomId, String ver, String aver) {
        this(handshaker, roomId, ver, aver, null, null);
    }

    @Override
    protected void sendHeartbeat(ChannelHandlerContext ctx) {
        if (log.isDebugEnabled()) {
            log.debug("发送心跳包");
        }
        ctx.writeAndFlush(
                getWebSocketFrameFactory(getRoomId()).createHeartbeat()
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

    @Override
    public void sendAuthRequest(Channel channel) {
        // 5s内认证
        if (log.isDebugEnabled()) {
            log.debug("发送认证包");
        }
        channel.writeAndFlush(getWebSocketFrameFactory(getRoomId()).createAuth(getVer(), getAver()));
    }

    private DouyuWebSocketFrameFactory getWebSocketFrameFactory(long roomId) {
        return DouyuWebSocketFrameFactory.getInstance(roomId);
    }

    public long getRoomId() {
        return client != null ? client.getConfig().getRoomId() : roomId;
    }

    private String getVer() {
        return client != null ? client.getConfig().getVer() : ver;
    }

    private String getAver() {
        return client != null ? client.getConfig().getAver() : aver;
    }

    private String getCookie() {
        return client != null ? client.getConfig().getCookie() : cookie;
    }

    @Override
    protected long getHeartbeatPeriod() {
        if (client == null) {
            return DouyuLiveChatClientConfig.DEFAULT_HEARTBEAT_PERIOD;
        } else {
            return client.getConfig().getHeartbeatPeriod();
        }
    }

    @Override
    protected long getHeartbeatInitialDelay() {
        if (client == null) {
            return DouyuLiveChatClientConfig.DEFAULT_HEARTBEAT_INITIAL_DELAY;
        } else {
            return client.getConfig().getHeartbeatInitialDelay();
        }
    }
}
