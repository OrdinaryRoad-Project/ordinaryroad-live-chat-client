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

package tech.ordinaryroad.live.chat.client.websocket.client;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.thread.ThreadUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.IBaseConnectionHandler;
import tech.ordinaryroad.live.chat.client.websocket.config.WebSocketLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.websocket.listener.IWebSocketMsgListener;
import tech.ordinaryroad.live.chat.client.websocket.msg.WebSocketMsg;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mjz
 * @date 2023/8/26
 */
@Slf4j
class WebSocketLiveChatClientTest {

    static Object lock = new Object();
    WebSocketLiveChatClient client;

    /**
     * 先运行main方法启动WebSocketServer，再运行该测试类
     */
    @Test
    void example() throws InterruptedException {
        WebSocketLiveChatClientConfig config = WebSocketLiveChatClientConfig.builder()
//                .websocketUri("wss://localhost:8443/websocket")
//                .websocketUri("wss://broadcastlv.chat.bilibili.com:443/sub")
                .websocketUri("ws://127.0.0.1:8080/websocket")
//                .forwardWebsocketUri("ws://127.0.0.1:8765")
                .build();

        client = new WebSocketLiveChatClient(config, new IBaseConnectionHandler() {
            @Override
            public void sendHeartbeat(Channel channel) {
                log.debug("忽略发送心跳包");
            }

            @Override
            public void sendAuthRequest(Channel channel) {
                log.debug("忽略发送认证包");
            }
        }, new IWebSocketMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                ByteBuf content = ((WebSocketMsg) msg).content();
                String string = content.toString(StandardCharsets.UTF_8);
                log.debug("收到消息 {}", string);
            }
        });
        client.connect(() -> {
            ScheduledThreadPoolExecutor scheduledExecutor = ThreadUtil.createScheduledExecutor(1);
            scheduledExecutor.scheduleAtFixedRate(() -> {
                client.send(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(LocalDateTimeUtil.now().toString().getBytes(StandardCharsets.UTF_8))));
            }, 0, 5, TimeUnit.SECONDS);
        });

        // 防止测试时直接退出
        while (true) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    static final boolean SSL = System.getenv("ssl") != null;
    /**
     * ws://127.0.0.1:8080/websocket
     * wss://127.0.0.1:8443/websocket
     */
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));
    static final String WEBSOCKET_PATH = "/websocket";

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            if (SSL) {
                                // Configure SSL.
                                SelfSignedCertificate ssc = new SelfSignedCertificate();
                                final SslContext sslCtx = SslContextBuilder
                                        .forServer(ssc.certificate(), ssc.privateKey())
                                        .build();
                                pipeline.addLast(sslCtx.newHandler(ch.alloc()));
                            }

                            pipeline.addLast(new HttpServerCodec());
                            // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new WebSocketServerCompressionHandler());
                            pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
                            pipeline.addLast(new SimpleChannelInboundHandler<WebSocketFrame>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
                                    // ping and pong frames already handled

                                    if (frame instanceof TextWebSocketFrame) {
                                        // Send the uppercase string back.
                                        String request = ((TextWebSocketFrame) frame).text();
                                        ctx.channel().writeAndFlush(new TextWebSocketFrame("from server:" + request.toUpperCase(Locale.US)));
                                        ctx.channel().writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(("from server:" + request.toUpperCase(Locale.US)).getBytes(StandardCharsets.UTF_8))));
                                        System.out.println("收到TextWebSocketFrame消息 " + request);
                                    } else if (frame instanceof BinaryWebSocketFrame) {
                                        ByteBuf content = frame.content();
                                        byte[] bytes = new byte[content.readableBytes()];
                                        content.readBytes(bytes);
                                        System.out.println("收到BinaryWebSocketFrame消息 " + new String(bytes));
                                        ctx.channel().writeAndFlush(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(("from server:" + new String(bytes)).getBytes(StandardCharsets.UTF_8))));
                                    } else {
                                        String message = "unsupported frame type: " + frame.getClass().getName();
                                        throw new UnsupportedOperationException(message);
                                    }
                                }
                            });
                        }
                    });

            Channel ch = b.bind(PORT).sync().channel();

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}