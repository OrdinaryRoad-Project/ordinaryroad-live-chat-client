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
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient;
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.bilibili.constant.ProtoverEnum;
import tech.ordinaryroad.live.chat.client.bilibili.netty.frame.factory.BilibiliWebSocketFrameFactory;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.BaseConnectionHandler;


/**
 * 连接处理器
 *
 * @author mjz
 * @date 2023/8/21
 */
@Slf4j
@ChannelHandler.Sharable
public class BilibiliConnectionHandler extends BaseConnectionHandler<BilibiliConnectionHandler> {

    private final BilibiliLiveChatClient client;
    private final long roomId;

    public BilibiliConnectionHandler(WebSocketClientHandshaker handshaker, long roomId, IBaseConnectionListener<BilibiliConnectionHandler> listener, BilibiliLiveChatClient client) {
        super(handshaker, listener);
        this.roomId = roomId;
        this.client = client;
    }

    public BilibiliConnectionHandler(WebSocketClientHandshaker handshaker, IBaseConnectionListener<BilibiliConnectionHandler> listener, long roomId) {
        this(handshaker, roomId, listener, null);
    }

    public BilibiliConnectionHandler(WebSocketClientHandshaker handshaker, long roomId) {
        this(handshaker, roomId, null, null);
    }

    private long getRoomId(){
        return client == null ? roomId : client.getConfig().getRoomId();
    }

    private ProtoverEnum getProtoverEnum() {
        return client == null ? ProtoverEnum.NORMAL_ZLIB : client.getConfig().getProtover();
    }

    @Override
    protected void sendHeartbeat(ChannelHandlerContext ctx) {
        log.debug("发送心跳包");
        ctx.writeAndFlush(
                BilibiliWebSocketFrameFactory.getInstance(getProtoverEnum())
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
        channel.writeAndFlush(BilibiliWebSocketFrameFactory.getInstance(getProtoverEnum()).createAuth(getRoomId()));
    }

    @Override
    protected long getHeartbeatPeriod() {
        if (client == null) {
            return BilibiliLiveChatClientConfig.DEFAULT_HEARTBEAT_PERIOD;
        } else {
            return client.getConfig().getHeartbeatPeriod();
        }
    }

    @Override
    protected long getHeartbeatInitialDelay() {
        if (client == null) {
            return BilibiliLiveChatClientConfig.DEFAULT_HEARTBEAT_INITIAL_DELAY;
        } else {
            return client.getConfig().getHeartbeatInitialDelay();
        }
    }
}
