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

package tech.ordinaryroad.live.chat.client.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.BilibiliApis;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.ProtoverEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.HeartbeatMsg;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.UserAuthenticationMsg;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.base.IBilibiliMsg;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.factory.BilibiliMsgFactory;
import tech.ordinaryroad.live.chat.client.codec.bilibili.util.BilibiliCodecUtil;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.IBaseConnectionHandler;
import tech.ordinaryroad.live.chat.client.websocket.client.WebSocketLiveChatClient;
import tech.ordinaryroad.live.chat.client.websocket.config.WebSocketLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.websocket.listener.IWebSocketMsgListener;
import tech.ordinaryroad.live.chat.client.websocket.msg.WebSocketMsg;
import tech.ordinaryroad.live.chat.client.websocket.netty.handler.WebSocketBinaryFrameHandler;

import java.util.List;

/**
 * Hello world!
 */
@Slf4j
public class BilibiliCodecExample {
    static WebSocketLiveChatClient webSocketLiveChatClient;
    static long roomId = 7777;
    static ProtoverEnum protoverEnum = ProtoverEnum.NORMAL_BROTLI;
    static BilibiliApis.RoomInitResult roomInitResult = BilibiliApis.roomInit(roomId, null);

    public static void main(String[] args) throws Exception {
        webSocketLiveChatClient = new WebSocketLiveChatClient(
                WebSocketLiveChatClientConfig.builder()
                        .websocketUri("wss://broadcastlv.chat.bilibili.com:443/sub")
                        .roomId(roomId)
                        .build(),
                new IBaseConnectionHandler() {
                    @Override
                    public void sendHeartbeat(Channel channel) {
                        HeartbeatMsg heartbeat = BilibiliMsgFactory.getInstance(roomId).createHeartbeat(protoverEnum);
                        ByteBuf encode = BilibiliCodecUtil.encode(heartbeat);
//                        channel.writeAndFlush(new BinaryWebSocketFrame(encode)).addListener((ChannelFutureListener) future -> {
//                            if (future.isSuccess()) {
//                                log.info("心跳包发送成功");
//                            } else {
//                                log.error("心跳包发送失败", future.cause());
//                            }
//                        });
                        // 只有连接成功后才可以发送心跳包，因此可以使用WebSocketLiveChatClient.send方法
                        webSocketLiveChatClient.send(new BinaryWebSocketFrame(encode), () -> log.info("心跳包发送成功"), (e) -> log.error("心跳包发送失败", e));
                    }

                    @Override
                    public void sendAuthRequest(Channel channel) {
                        UserAuthenticationMsg auth = BilibiliMsgFactory.getInstance(roomId).createAuth(protoverEnum, roomInitResult);
                        ByteBuf encode = BilibiliCodecUtil.encode(auth);
                        channel.writeAndFlush(new BinaryWebSocketFrame(encode)).addListener((ChannelFutureListener) future -> {
                            if (future.isSuccess()) {
                                log.info("认证包发送成功");
                            } else {
                                log.error("认证包发送失败", future.cause());
                            }
                        });
                    }
                });

        webSocketLiveChatClient.addMsgListener(new IWebSocketMsgListener() {
            @Override
            public void onMsg(WebSocketBinaryFrameHandler webSocketBinaryFrameHandler, IMsg msg) {
                log.debug("{} onMsg {}", webSocketBinaryFrameHandler.getRoomId(), msg);

                List<IBilibiliMsg> decode = BilibiliCodecUtil.decode(((WebSocketMsg) msg).content());
                for (IBilibiliMsg iBilibiliMsg : decode) {
                    log.info("{} 收到{}消息 {}", webSocketBinaryFrameHandler.getRoomId(), iBilibiliMsg.getClass().getSimpleName(), iBilibiliMsg);
                }
            }
        });
        webSocketLiveChatClient.connect();

        while (true) {
            System.in.read();
        }
    }
}
