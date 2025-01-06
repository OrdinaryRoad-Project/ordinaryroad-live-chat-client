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

package tech.ordinaryroad.live.chat.client.example.handler;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliCodecHandler;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliConnectionHandler;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.BilibiliApis;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliCmdEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.ProtoverEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.*;
import tech.ordinaryroad.live.chat.client.codec.bilibili.room.BilibiliRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;

import java.net.URI;
import java.util.concurrent.TimeUnit;


/**
 * @author mjz
 * @date 2023/1/7
 */
@Slf4j
public class BilibiliHandlerModeExample {

    static Channel channel;
    // TODO 修改房间ID
    static long roomId = 7777;
    // TODO 设置浏览器Cookie
    static String cookie = System.getenv("cookie");
    // TODO 修改版本
    static ProtoverEnum protover = ProtoverEnum.NORMAL_BROTLI;
    static BilibiliRoomInitResult roomInitResult = BilibiliApis.roomInit(roomId, cookie);

    public static void main(String[] args) {
        log.error("cookie: {}", cookie);

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        BilibiliConnectionHandler connectionHandler = null;
        IBaseConnectionListener<BilibiliConnectionHandler> connectionListener = new IBaseConnectionListener<BilibiliConnectionHandler>() {

            @Override
            public void onConnected(BilibiliConnectionHandler connectionHandler) {
                log.error("{} 连接成功", roomId);
            }

            @Override
            public void onDisconnected(BilibiliConnectionHandler connectionHandler) {
                log.error("连接断开，5s后将重新连接");
                workerGroup.schedule(() -> {
                    bootstrap.connect().addListener((ChannelFutureListener) connectFuture -> {
                        if (connectFuture.isSuccess()) {
                            log.debug("连接建立成功！");
                            channel = connectFuture.channel();
                            // 监听是否握手成功
                            connectionHandler.getHandshakePromise().addListener((ChannelFutureListener) handshakeFuture -> {
                                // 5s内认证
                                connectionHandler.sendAuthRequest(channel);
                            });
                        } else {
                            log.error("连接建立失败", connectFuture.cause());
                            this.onConnectFailed(connectionHandler);
                        }
                    });
                }, 5, TimeUnit.SECONDS);
            }

            @Override
            public void onConnectFailed(BilibiliConnectionHandler connectionHandler) {
                onDisconnected(connectionHandler);
            }
        };

        try {
            URI websocketURI = new URI("wss://broadcastlv.chat.bilibili.com:443/sub");

            connectionHandler = new BilibiliConnectionHandler(
                    () -> new WebSocketClientProtocolHandler(
                            WebSocketClientProtocolConfig.newBuilder()
                                    .webSocketUri(websocketURI)
                                    .version(WebSocketVersion.V13)
                                    .subprotocol(null)
                                    .allowExtensions(true)
                                    .customHeaders(new DefaultHttpHeaders())
                                    .build()
                    ),
                    roomInitResult, roomId, protover, connectionListener, cookie
            );
            IBilibiliMsgListener msgListener = new IBilibiliMsgListener() {
                @Override
                public void onDanmuMsg(BilibiliBinaryFrameHandler binaryFrameHandler, DanmuMsgMsg msg) {
                    IBilibiliMsgListener.super.onDanmuMsg(binaryFrameHandler, msg);
                    log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
                }

                @Override
                public void onGiftMsg(BilibiliBinaryFrameHandler binaryFrameHandler, SendGiftMsg msg) {
                    IBilibiliMsgListener.super.onGiftMsg(binaryFrameHandler, msg);
                    log.info("{} 收到礼物 {} {}({}) {} {}({})x{}({})", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getData().getAction(), msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice());
                }

                @Override
                public void onSuperChatMsg(BilibiliBinaryFrameHandler binaryFrameHandler, SuperChatMessageMsg msg) {
                    IBilibiliMsgListener.super.onSuperChatMsg(binaryFrameHandler, msg);
                    log.info("{} 收到醒目留言 {}({})：{}", binaryFrameHandler.getRoomId(), msg.getUsername(), msg.getUid(), msg.getContent());
                }

                @Override
                public void onEnterRoomMsg(BilibiliBinaryFrameHandler binaryFrameHandler, InteractWordMsg msg) {
                    log.info("{} {}({}) 进入直播间", msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid());
                }

                @Override
                public void onLikeMsg(BilibiliBinaryFrameHandler binaryFrameHandler, LikeInfoV3ClickMsg msg) {
                    IBilibiliMsgListener.super.onLikeMsg(binaryFrameHandler, msg);
                    log.info("{} 收到点赞 [{}] {}({})x{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getClickCount());
                }

                @Override
                public void onLiveStatusMsg(BilibiliBinaryFrameHandler binaryFrameHandler, BilibiliLiveStatusChangeMsg msg) {
                    IBilibiliMsgListener.super.onLiveStatusMsg(binaryFrameHandler, msg);
                    log.error("{} 状态变化 {}", binaryFrameHandler.getRoomId(), msg.getLiveStatusAction());
                }

                @Override
                public void onRoomStatsMsg(BilibiliBinaryFrameHandler binaryFrameHandler, BilibiliRoomStatsMsg msg) {
                    IBilibiliMsgListener.super.onRoomStatsMsg(binaryFrameHandler, msg);
                    log.info("{} 统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", binaryFrameHandler.getRoomId(), msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
                }

                @Override
                public void onEntryEffect(MessageMsg msg) {
                    JsonNode data = msg.getData();
                    String copyWriting = data.get("copy_writing").asText();
                    log.info("入场效果 {}", copyWriting);
                }

                @Override
                public void onMsg(IMsg msg) {
                    // log.debug("收到{}消息 {}", msg.getClass(), msg);
                }

                @Override
                public void onCmdMsg(BilibiliCmdEnum cmd, ICmdMsg<BilibiliCmdEnum> cmdMsg) {
                    log.debug("收到CMD消息{} {}", cmd, cmdMsg);
                }

                @Override
                public void onOtherCmdMsg(BilibiliCmdEnum cmd, ICmdMsg<BilibiliCmdEnum> cmdMsg) {
                    log.debug("收到其他CMD消息 {}", cmd);
                }

                @Override
                public void onUnknownCmd(String cmdString, IMsg msg) {
                    log.debug("收到未知CMD消息 {}", cmdString);
                }
            };
            BilibiliBinaryFrameHandler bilibiliHandler = new BilibiliBinaryFrameHandler(CollUtil.newArrayList(msgListener), roomId, Pair.of(roomInitResult.getRoomPlayInfoResult().getRoom_id(), roomInitResult.getRoomPlayInfoResult().getShort_id()));

            //进行握手
            log.info("握手开始");
//            SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            SslContext sslCtx = SslContextBuilder.forClient().build();

            BilibiliConnectionHandler finalConnectionHandler = connectionHandler;
            bootstrap.group(workerGroup)
                    // 创建Channel
                    .channel(NioSocketChannel.class)
                    .remoteAddress(websocketURI.getHost(), websocketURI.getPort())
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // Channel配置
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 责任链
                            ChannelPipeline pipeline = ch.pipeline();

                            //放到第一位 addFirst 支持wss链接服务端
                            pipeline.addFirst(sslCtx.newHandler(ch.alloc(), websocketURI.getHost(), websocketURI.getPort()));

                            // 添加一个http的编解码器
                            pipeline.addLast(new HttpClientCodec());
                            // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64));
//                            pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);
                            // 添加一个WebSocket协议处理器
                            pipeline.addLast(finalConnectionHandler.getWebSocketProtocolHandler().get());
                            // pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);

                            // 连接处理器
                            pipeline.addLast(finalConnectionHandler);
                            pipeline.addLast(new BilibiliCodecHandler());
                            pipeline.addLast(bilibiliHandler);
                        }
                    });

            channel = bootstrap.connect().sync().channel();
            // 阻塞等待是否握手成功
            connectionHandler.getHandshakePromise().sync();
            log.error("连接成功，10s后将断开连接，模拟自动重连");
            workerGroup.schedule(() -> {
                channel.close();
            }, 10, TimeUnit.SECONDS);
            // 5s内认证
            connectionHandler.sendAuthRequest(channel);

            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            connectionListener.onConnectFailed(connectionHandler);
//            throw new RuntimeException(e);
        }
    }

}