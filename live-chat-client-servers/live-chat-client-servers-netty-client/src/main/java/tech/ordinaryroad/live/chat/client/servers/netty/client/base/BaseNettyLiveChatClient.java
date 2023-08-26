package tech.ordinaryroad.live.chat.client.servers.netty.client.base;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseMsgListener;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.commons.client.BaseLiveChatClient;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.servers.netty.client.config.BaseNettyLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.BaseBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.BaseConnectionHandler;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * @author mjz
 * @date 2023/8/26
 */
public abstract class BaseNettyLiveChatClient
        <Config extends BaseNettyLiveChatClientConfig,
                CmdEnum extends Enum<CmdEnum>,
                Msg extends IMsg,
                MsgListener extends IBaseMsgListener<CmdEnum>,
                ConnectionHandler extends BaseConnectionHandler<ConnectionHandler>,
                BinaryFrameHandler extends BaseBinaryFrameHandler<CmdEnum, Msg, MsgListener>
                >
        extends BaseLiveChatClient<Config> {

    Logger log = LoggerFactory.getLogger(getClass());

    @Getter
    private final EventLoopGroup workerGroup;
    @Getter
    private final Bootstrap bootstrap = new Bootstrap();
    private BinaryFrameHandler binaryFrameHandler;
    private ConnectionHandler connectionHandler;
    private IBaseConnectionListener<ConnectionHandler> connectionListener;
    private Channel channel;
    @Getter
    private URI websocketUri;
    private IBaseConnectionListener<ConnectionHandler> clientConnectionListener;

    public abstract ConnectionHandler initConnectionHandler(IBaseConnectionListener<ConnectionHandler> clientConnectionListener);

    public abstract BinaryFrameHandler initBinaryFrameHandler();

    protected BaseNettyLiveChatClient(Config config, EventLoopGroup workerGroup, IBaseConnectionListener<ConnectionHandler> connectionListener) {
        super(config);
        this.workerGroup = workerGroup;
        this.connectionListener = connectionListener;
    }

    public void onConnected() {
        if (this.connectionListener != null) {
            this.connectionListener.onConnected();
        }
    }

    public void onConnectFailed(ConnectionHandler baseConnectionHandler) {
        tryReconnect();
        if (this.connectionListener != null) {
            this.connectionListener.onConnectFailed(connectionHandler);
        }
    }

    public void onDisconnected(ConnectionHandler baseConnectionHandler) {
        tryReconnect();
        if (this.connectionListener != null) {
            this.connectionListener.onDisconnected(connectionHandler);
        }
    }

    @Override
    public void init() {
        if (checkStatus(ClientStatusEnums.INITIALIZED)) {
            return;
        }
        try {
            this.websocketUri = new URI(getWebSocketUriString());
            SslContext sslCtx = SslContextBuilder.forClient().build();

            this.clientConnectionListener = new IBaseConnectionListener<ConnectionHandler>() {
                @Override
                public void onConnected() {
                    BaseNettyLiveChatClient.this.onConnected();
                }

                @Override
                public void onConnectFailed(ConnectionHandler connectionHandler) {
                    BaseNettyLiveChatClient.this.onConnectFailed(connectionHandler);
                }

                @Override
                public void onDisconnected(ConnectionHandler connectionHandler) {
                    BaseNettyLiveChatClient.this.onDisconnected(connectionHandler);
                }
            };
            this.binaryFrameHandler = this.initBinaryFrameHandler();
            this.connectionHandler = this.initConnectionHandler(this.clientConnectionListener);

            this.bootstrap.group(this.workerGroup)
                    // 创建Channel
                    .channel(NioSocketChannel.class)
                    .remoteAddress(this.websocketUri.getHost(), this.websocketUri.getPort())
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // Channel配置
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 责任链
                            ChannelPipeline pipeline = ch.pipeline();

                            // 放到第一位 addFirst 支持wss链接服务端
                            pipeline.addFirst(sslCtx.newHandler(ch.alloc(), BaseNettyLiveChatClient.this.websocketUri.getHost(), BaseNettyLiveChatClient.this.websocketUri.getPort()));

                            // 添加一个http的编解码器
                            pipeline.addLast(new HttpClientCodec());
                            // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                            pipeline.addLast(new HttpObjectAggregator(BaseNettyLiveChatClient.this.getConfig().getAggregatorMaxContentLength()));

                            // 连接处理器
                            pipeline.addLast(BaseNettyLiveChatClient.this.connectionHandler);
                            // 弹幕处理器
                            pipeline.addLast(BaseNettyLiveChatClient.this.binaryFrameHandler);
                        }
                    });
            setStatus(ClientStatusEnums.INITIALIZED);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connect() {
        if (this.cancelReconnect) {
            this.cancelReconnect = false;
        }
        if (!checkStatus(ClientStatusEnums.INITIALIZED)) {
            return;
        }
        this.bootstrap.connect().addListener((ChannelFutureListener) connectFuture -> {
            if (connectFuture.isSuccess()) {
                log.debug("连接建立成功！");
                this.channel = connectFuture.channel();
                // 监听是否握手成功
                this.connectionHandler.getHandshakeFuture().addListener((ChannelFutureListener) handshakeFuture -> {
                    connectionHandler.sendAuthRequest(channel);
                });
            } else {
                log.error("连接建立失败", connectFuture.cause());
                this.onConnectFailed(this.connectionHandler);
            }
        });
    }

    @Override
    public void disconnect() {
        if (this.channel == null) {
            return;
        }
        this.channel.close();
    }

    @Override
    protected void tryReconnect() {
        if (this.cancelReconnect) {
            this.cancelReconnect = false;
            return;
        }
        if (!getConfig().isAutoReconnect()) {
            return;
        }
        log.debug("{}s后将重新连接", getConfig().getReconnectDelay());
        workerGroup.schedule(this::connect, getConfig().getReconnectDelay(), TimeUnit.SECONDS);
    }

    @Override
    public void send(Object msg) {
        this.channel.writeAndFlush(msg);
    }

    @Override
    public void destroy() {
        workerGroup.shutdownGracefully();
    }

    @Override
    protected String getWebSocketUriString() {
        return getConfig().getWebsocketUri();
    }

}
