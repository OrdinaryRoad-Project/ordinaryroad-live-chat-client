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

package tech.ordinaryroad.live.chat.client.douyin.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.douyin.client.DouyinLiveChatClient;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientConnectionHandler;

import java.util.function.Supplier;

/**
 * @author mjz
 * @date 2024/1/2
 */
@Slf4j
@ChannelHandler.Sharable
public class DouyinConnectionHandler extends BaseNettyClientConnectionHandler<DouyinLiveChatClient, DouyinConnectionHandler> {

    /**
     * 心跳包
     */
    private static final byte[] HEARTBEAT_BYTES = {58, 2, 104, 98};
    /**
     * 以ClientConfig为主
     */
    private final Object roomId;
    /**
     * 以ClientConfig为主
     */
    private String cookie;

    public DouyinConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, DouyinLiveChatClient client, IBaseConnectionListener<DouyinConnectionHandler> listener) {
        super(webSocketProtocolHandler, client, listener);
        this.roomId = client.getConfig().getRoomId();
        this.cookie = client.getConfig().getCookie();
    }

    public DouyinConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, DouyinLiveChatClient client) {
        this(webSocketProtocolHandler, client, null);
    }

    public DouyinConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, long roomId, IBaseConnectionListener<DouyinConnectionHandler> listener, String cookie) {
        super(webSocketProtocolHandler, listener);
        this.roomId = roomId;
        this.cookie = cookie;
    }

    public DouyinConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, long roomId, IBaseConnectionListener<DouyinConnectionHandler> listener) {
        this(webSocketProtocolHandler, roomId, listener, null);
    }

    public DouyinConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, long roomId, String cookie) {
        this(webSocketProtocolHandler, roomId, null, cookie);
    }

    public DouyinConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, long roomId) {
        this(webSocketProtocolHandler, roomId, null, null);
    }

    @Override
    public void sendHeartbeat(Channel channel) {
        if (log.isDebugEnabled()) {
            log.debug("发送心跳包");
        }
        // douyin_websocket_frame.newBuilder().setPayloadType("hb").build().toByteArray()
        // Base64.decode("OgJoYg==");
        channel.writeAndFlush(new BinaryWebSocketFrame(channel.alloc().buffer().writeBytes(HEARTBEAT_BYTES)))
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        if (log.isDebugEnabled()) {
                            log.debug("心跳包发送完成");
                        }
                    } else {
                        log.error("心跳包发送失败", future.cause());
                    }
                });
    }

    public Object getRoomId() {
        return client != null ? client.getConfig().getRoomId() : roomId;
    }

    private String getCookie() {
        return client != null ? client.getConfig().getCookie() : cookie;
    }
}
