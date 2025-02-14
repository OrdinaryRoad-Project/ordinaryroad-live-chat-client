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
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
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
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.ProtoverEnum;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.api.KuaishouApis;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.constant.RoomInfoGetTypeEnum;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouDanmuMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouGiftMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouLikeMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouRoomStatsMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PayloadTypeOuterClass;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.room.KuaishouRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouCodecHandler;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouConnectionHandler;

import java.net.URI;
import java.util.concurrent.TimeUnit;


/**
 * @author mjz
 * @date 2025/2/6
 */
@Slf4j
public class KuaishouHandlerModeExample {

    static Channel channel;
    // TODO 修改房间ID
    static String roomId = "3x7twtpebncz2v2";
    // TODO 设置浏览器Cookie
    static String cookie = "_did=web_969746333D8578B7; did=web_bea7c4d8321f60897a1ab8ff2c946e94; clientid=3; did=web_bea7c4d8321f60897a1ab8ff2c946e94; client_key=65890b29; kpn=GAME_ZONE; app_id=ks686235996059836592; access_token=ChFvYXV0aC5hY2Nlc3NUb2tlbhIwHz-ct7Z7-c6UvBZJs4XZbFydjSjLJATueqdM4VkHEBuKQgCrdcE9giphk_C2tJqsGhIAEuN-HvJNPLUOKVV1NaFEgHAiIFns-h5RB4p4OSdfU2mDReUDsFpNcoFOFMlfaac6lKO8KAUwAQ; nc_user_id=CiVhZC5ub3RpZnkuY2VudGVyLm9hdXRoLnVzZXIuaWQuc2VjcmV0EiDfQuLlgrJyLswxT3Nt6caggzWDkQCcB2guK7hP8ZmRqhoSXOg6QliB3tY0wFa/6ToM59ncIiAcCS9wPv56KqHaS16mCK4EuJcubUIsGv5X94maBdVj7igFMAE=; didv=1735804800736; kwpsecproductname=PCLive; kuaishou.live.bfb1s=3e261140b0cf7444a0ba411c6f227d88; userId=4499176355; userId=4499176355; expire_time=18000000; bUserId=1000417235493; ud=4499176355; language=zh-CN; account_id=17635038; kwfv1=PnGU+9+Y8008S+nH0U+0mjPf8fP08f+98f+nLlwnrIP9P9G98YPf8jPBQSweS0+nr9G0mD8B+fP/L98/qlPe4f8eZAwBrI+APAPfzDGAYY+/mD8/Hh80H7P0cMweDEP/rAwnrAw/8f+/zDwB8Y+f80Pech+n8YG9P9G/G9+AcUP9r=; kuaishou.live.web_st=ChRrdWFpc2hvdS5saXZlLndlYi5zdBKgAfDeO0fLRy-xCOizo4k8FELo1jFTFQVTjFQmhwJ_q5_YaduWBQhorjV3BqAPcsYpmKY1IVcvx5_AKh_Eq9xf66qg5I5ujdoUspT6xLLHV9bh6TDjJyo-Ux4DsopOu82P_tXr6f0vLxPKKCwKAA-cqljwT8YwgEgXiL7LPUAbfKgC1kbZ6YlUVp_7TP9P_Ce17JwmoL9RS-He6ImEBR-WOjwaEqVj6czba05AmU7FMveU3Qsu3iIgAHh2Hm860h1Lq8coz66crYZ11qcLZQwOVCzcrCN7dfooBTAB; kuaishou.live.web_ph=e6e04744f0a440a8f503a02fa077a1e9670b; kwssectoken=bY7whVibhwiCmV8L8X113Xdgwxz1itL30+Kopp2vfKwyrRLKsJGRkS+alQB/c59JhBkZpcSlK+7nqTYlP2Flfg==; kwscode=d41ceef34ccc29476f5f3078333261cc153215dac112cb22233a3653b8e25439";
    // TODO 修改版本
    static ProtoverEnum protover = ProtoverEnum.NORMAL_BROTLI;
    static KuaishouRoomInitResult roomInitResult = KuaishouApis.roomInit(roomId, RoomInfoGetTypeEnum.COOKIE,cookie);
 
    public static void main(String[] args) {
        log.error("cookie: {}", cookie);

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        KuaishouConnectionHandler connectionHandler = null;
        IBaseConnectionListener<KuaishouConnectionHandler> connectionListener = new IBaseConnectionListener<KuaishouConnectionHandler>() {

            @Override
            public void onConnected(KuaishouConnectionHandler connectionHandler) {
                log.error("{} 连接成功", roomId);
            }

            @Override
            public void onDisconnected(KuaishouConnectionHandler connectionHandler) {
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
            public void onConnectFailed(KuaishouConnectionHandler connectionHandler) {
                onDisconnected(connectionHandler);
            }
        };

        try {
            URI websocketURI = new URI("wss://live-ws-pg-group3.kuaishou.com/websocket");

            connectionHandler = new KuaishouConnectionHandler(
                    () -> new WebSocketClientProtocolHandler(
                            WebSocketClientProtocolConfig.newBuilder()
                                    .webSocketUri(websocketURI)
                                    .version(WebSocketVersion.V13)
                                    .subprotocol(null)
                                    .allowExtensions(true)
                                    .customHeaders(new DefaultHttpHeaders())
                                    .build()
                    ),
                    roomInitResult, Long.parseLong(roomId)
            );
            IKuaishouMsgListener msgListener = new IKuaishouMsgListener() {
                @Override
                public void onDanmuMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouDanmuMsg msg) {
                    IKuaishouMsgListener.super.onDanmuMsg(binaryFrameHandler, msg);
                    log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
                }

                @Override
                public void onGiftMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouGiftMsg msg) {
                    IKuaishouMsgListener.super.onGiftMsg(binaryFrameHandler, msg);
                    log.info("{} 收到礼物 {} {}({}) {} {}({})x{}({})", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice());
                }
            


                @Override
                public void onLikeMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouLikeMsg msg) {
                    IKuaishouMsgListener.super.onLikeMsg(binaryFrameHandler, msg);
                    log.info("{} 收到点赞 [{}] {}({})x{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getClickCount());
                }

                @Override
                public void onRoomStatsMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouRoomStatsMsg msg) {
                    IKuaishouMsgListener.super.onRoomStatsMsg(binaryFrameHandler, msg);
                    log.info("{} 统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", binaryFrameHandler.getRoomId(), msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
                }

                @Override
                public void onMsg(IMsg msg) {
                     log.debug("收到{}消息 {}", msg.getClass(), msg);
                }

                @Override
                public void onCmdMsg(KuaishouBinaryFrameHandler kuaishouBinaryFrameHandler, PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
                    log.debug("收到CMD消息{} {}", cmd, cmdMsg);
                }

                @Override
                public void onUnknownCmd(String cmdString, IMsg msg) {
                    log.debug("收到未知CMD消息 {}", cmdString);
                }
            };

            KuaishouBinaryFrameHandler KuaishouHandler = new KuaishouBinaryFrameHandler(CollUtil.newArrayList(msgListener),null );
            //进行握手
            log.info("握手开始");
//            SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            SslContext sslCtx = SslContextBuilder.forClient().build();

            KuaishouConnectionHandler finalConnectionHandler = connectionHandler;
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
                            pipeline.addLast(new KuaishouCodecHandler());
                            pipeline.addLast(KuaishouHandler);
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