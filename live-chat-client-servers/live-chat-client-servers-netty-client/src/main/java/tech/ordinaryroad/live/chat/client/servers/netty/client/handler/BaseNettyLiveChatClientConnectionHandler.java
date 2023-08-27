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

package tech.ordinaryroad.live.chat.client.servers.netty.client.handler;

import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.servers.netty.client.base.BaseNettyLiveChatClient;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.BaseConnectionHandler;

/**
 * BaseClientConnectionHandler
 *
 * @author mjz
 * @date 2023/8/27
 */
public abstract class BaseNettyLiveChatClientConnectionHandler<
        Client extends BaseNettyLiveChatClient<?, ?, ?, ?, ?, ?>,
        ConnectionHandler extends BaseConnectionHandler<ConnectionHandler>>
        extends BaseConnectionHandler<ConnectionHandler> {

    protected final Client client;
    private final long roomId;

    public BaseNettyLiveChatClientConnectionHandler(WebSocketClientHandshaker handshaker, IBaseConnectionListener<ConnectionHandler> listener, Client client, long roomId) {
        super(handshaker, listener);
        this.client = client;
        this.roomId = roomId;
    }

    public BaseNettyLiveChatClientConnectionHandler(WebSocketClientHandshaker handshaker, IBaseConnectionListener<ConnectionHandler> listener, Client client) {
        super(handshaker, listener);
        this.client = client;
        this.roomId = client.getConfig().getRoomId();
    }

    public BaseNettyLiveChatClientConnectionHandler(WebSocketClientHandshaker handshaker, Client client) {
        super(handshaker);
        this.client = client;
        this.roomId = client.getConfig().getRoomId();
    }

    public BaseNettyLiveChatClientConnectionHandler(WebSocketClientHandshaker handshaker, long roomId) {
        super(handshaker);
        this.client = null;
        this.roomId = roomId;
    }

    /**
     * 以client配置为主
     *
     * @return long
     */
    protected long getRoomId() {
        return this.client != null ? this.client.getConfig().getRoomId() : roomId;
    }
}
