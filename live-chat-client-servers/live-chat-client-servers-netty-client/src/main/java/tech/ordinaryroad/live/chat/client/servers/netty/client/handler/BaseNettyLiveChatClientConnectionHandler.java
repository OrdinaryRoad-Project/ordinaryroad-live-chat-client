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
