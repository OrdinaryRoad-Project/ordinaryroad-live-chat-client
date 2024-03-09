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

package tech.ordinaryroad.live.chat.client.websocket.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientConnectionHandler;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.IBaseConnectionHandler;
import tech.ordinaryroad.live.chat.client.websocket.client.WebSocketLiveChatClient;


/**
 * 连接处理器
 *
 * @author mjz
 * @date 2024/3/8
 */
@Slf4j
@ChannelHandler.Sharable
public class WebSocketConnectionHandler extends BaseNettyClientConnectionHandler<WebSocketLiveChatClient, WebSocketConnectionHandler> {

    private final IBaseConnectionHandler connectionHandler;

    public WebSocketConnectionHandler(WebSocketClientHandshaker handshaker, IBaseConnectionHandler connectionHandler, WebSocketLiveChatClient client, IBaseConnectionListener<WebSocketConnectionHandler> listener) {
        super(handshaker, client, listener);
        this.connectionHandler = connectionHandler;
    }

    public WebSocketConnectionHandler(WebSocketClientHandshaker handshaker, IBaseConnectionHandler connectionHandler, WebSocketLiveChatClient client) {
        this(handshaker, connectionHandler, client, null);
    }

    public WebSocketConnectionHandler(WebSocketClientHandshaker handshaker, IBaseConnectionHandler connectionHandler) {
        this(handshaker, connectionHandler, null);
    }

    public WebSocketConnectionHandler(WebSocketClientHandshaker handshaker) {
        this(handshaker, null);
    }

    @Override
    public void sendHeartbeat(ChannelHandlerContext ctx) {
        if (connectionHandler == null) {
            return;
        }
        connectionHandler.sendHeartbeat(ctx);
    }

    @Override
    public void sendAuthRequest(Channel channel) {
        if (connectionHandler == null) {
            return;
        }
        connectionHandler.sendAuthRequest(channel);
    }
}
