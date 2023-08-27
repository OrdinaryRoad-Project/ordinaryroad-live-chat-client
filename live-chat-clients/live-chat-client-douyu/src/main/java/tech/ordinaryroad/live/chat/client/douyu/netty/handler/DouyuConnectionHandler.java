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
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyLiveChatClientConnectionHandler;


/**
 * 连接处理器
 *
 * @author mjz
 * @date 2023/8/21
 */
@Slf4j
@ChannelHandler.Sharable
public class DouyuConnectionHandler extends BaseNettyLiveChatClientConnectionHandler<DouyuLiveChatClient, DouyuConnectionHandler> {

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, IBaseConnectionListener<DouyuConnectionHandler> listener, DouyuLiveChatClient client, long roomId) {
        super(handshaker, listener, client, roomId);
    }

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, IBaseConnectionListener<DouyuConnectionHandler> listener, DouyuLiveChatClient client) {
        super(handshaker, listener, client);
    }

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, DouyuLiveChatClient client) {
        super(handshaker, client);
    }

    public DouyuConnectionHandler(WebSocketClientHandshaker handshaker, long roomId) {
        super(handshaker, roomId);
    }

    @Override
    protected void sendHeartbeat(ChannelHandlerContext ctx) {
        log.debug("发送心跳包");
        ctx.writeAndFlush(
                DouyuWebSocketFrameFactory.getInstance()
                        .createHeartbeat()
        ).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.debug("心跳包发送完成");
            } else {
                log.error("心跳包发送失败", future.cause());
            }
        });
    }

    @Override
    public void sendAuthRequest(Channel channel) {
        // 5s内认证
        log.debug("发送认证包");
        channel.writeAndFlush(DouyuWebSocketFrameFactory.getInstance().createAuth(getRoomId()));
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
