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

import cn.hutool.core.thread.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.BilibiliApis;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.ProtoverEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.HeartbeatMsg;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.UserAuthenticationMsg;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.base.IBilibiliMsg;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.factory.BilibiliMsgFactory;
import tech.ordinaryroad.live.chat.client.codec.bilibili.room.BilibiliRoomInitResult;
import tech.ordinaryroad.live.chat.client.codec.bilibili.util.BilibiliCodecUtil;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
@Slf4j
public class BilibiliCodecExampleJavaWebSocketClient {
    static long roomId = 7777;
    static ProtoverEnum protoverEnum = ProtoverEnum.NORMAL_BROTLI;
    static BilibiliRoomInitResult roomInitResult = BilibiliApis.roomInit(roomId, null);

    static WebSocketClient webSocketClient;

    public static void main(String[] args) throws Exception {
        webSocketClient = new WebSocketClient(URI.create("wss://broadcastlv.chat.bilibili.com/sub")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                log.info("onOpen {}", handshakedata);

                UserAuthenticationMsg auth = BilibiliMsgFactory.getInstance(roomId).createAuth(protoverEnum, roomInitResult);
                ByteBuf encodeAuth = BilibiliCodecUtil.encode(auth, Unpooled.buffer());
                send(encodeAuth.array());
                log.info("认证包发送成功");

                ThreadUtil.execAsync(() -> {
                    while (true) {
                        HeartbeatMsg heartbeat = BilibiliMsgFactory.getInstance(roomId).createHeartbeat(protoverEnum);
                        ByteBuf encodeHeartbeat = BilibiliCodecUtil.encode(heartbeat, Unpooled.buffer());
                        webSocketClient.send(encodeHeartbeat.array());
                        log.info("心跳包发送成功");
                        ThreadUtil.sleep(TimeUnit.SECONDS.toMillis(25));
                    }
                });
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                List<IBilibiliMsg> msgList = BilibiliCodecUtil.decode(ByteBufAllocator.DEFAULT.buffer().writeBytes(bytes));
                for (IBilibiliMsg iBilibiliMsg : msgList) {
                    log.info("{} 收到{}消息 {}", roomId, iBilibiliMsg.getClass().getSimpleName(), iBilibiliMsg);
                }
            }

            @Override
            public void onMessage(String message) {
                log.info("{} 收到消息 {}", roomId, message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                log.info("onClose {} {} {}", code, reason, remote);
            }

            @Override
            public void onError(Exception ex) {
                log.error("onError", ex);
            }
        };

        webSocketClient.connect();

        while (true) {
            System.in.read();
        }
    }
}
