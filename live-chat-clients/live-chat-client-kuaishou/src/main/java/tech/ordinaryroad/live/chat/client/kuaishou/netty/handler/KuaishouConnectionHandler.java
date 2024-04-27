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

package tech.ordinaryroad.live.chat.client.kuaishou.netty.handler;

import cn.hutool.core.util.RandomUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.api.KuaishouApis;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouCmdMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.CSHeartbeatOuterClass;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.CSWebEnterRoomOuterClass;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PayloadTypeOuterClass;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.SocketMessageOuterClass;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.kuaishou.client.KuaishouLiveChatClient;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientConnectionHandler;

import java.util.function.Supplier;

/**
 * @author mjz
 * @date 2024/1/5
 */
@Slf4j
@ChannelHandler.Sharable
public class KuaishouConnectionHandler extends BaseNettyClientConnectionHandler<KuaishouLiveChatClient, KuaishouConnectionHandler> {

    /**
     * 以ClientConfig为主
     */
    private final Object roomId;
    /**
     * 以ClientConfig为主
     */
    private String cookie;
    private final KuaishouApis.RoomInitResult roomInitResult;

    public KuaishouConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, KuaishouLiveChatClient client, IBaseConnectionListener<KuaishouConnectionHandler> listener) {
        super(webSocketProtocolHandler, client, listener);
        this.roomId = client.getConfig().getRoomId();
        this.cookie = client.getConfig().getCookie();
        this.roomInitResult = client.getRoomInitResult();
    }

    public KuaishouConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, KuaishouLiveChatClient client) {
        this(webSocketProtocolHandler, client, null);
    }

    public KuaishouConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, long roomId, KuaishouApis.RoomInitResult roomInitResult, IBaseConnectionListener<KuaishouConnectionHandler> listener, String cookie) {
        super(webSocketProtocolHandler, listener);
        this.roomId = roomId;
        this.cookie = cookie;
        this.roomInitResult = roomInitResult;
    }

    public KuaishouConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, long roomId, KuaishouApis.RoomInitResult roomInitResult, IBaseConnectionListener<KuaishouConnectionHandler> listener) {
        this(webSocketProtocolHandler, roomId, roomInitResult, listener, null);
    }

    public KuaishouConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, long roomId, KuaishouApis.RoomInitResult roomInitResult, String cookie) {
        this(webSocketProtocolHandler, roomId, roomInitResult, null, cookie);
    }

    public KuaishouConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, KuaishouApis.RoomInitResult roomInitResult, long roomId) {
        this(webSocketProtocolHandler, roomId, roomInitResult, null, null);
    }

    @Override
    public void sendHeartbeat(Channel channel) {
        if (log.isDebugEnabled()) {
            log.debug("发送心跳包");
        }
        channel.writeAndFlush(
                new KuaishouCmdMsg(
                        SocketMessageOuterClass.SocketMessage.newBuilder()
                                .setPayloadType(PayloadTypeOuterClass.PayloadType.CS_HEARTBEAT)
                                .setPayload(
                                        CSHeartbeatOuterClass.CSHeartbeat.newBuilder()
                                                .setTimestamp(System.currentTimeMillis())
                                                .build()
                                                .toByteString()
                                )
                                .build()
                )
        ).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                if (log.isDebugEnabled()) {
                    log.debug("心跳包发送完成");
                }
            } else {
                log.error("心跳包发送失败", future.cause());
            }
        });
    }

    @Override
    public void sendAuthRequest(Channel channel) {
        channel.writeAndFlush(
                new KuaishouCmdMsg(
                        SocketMessageOuterClass.SocketMessage.newBuilder()
                                .setPayloadType(PayloadTypeOuterClass.PayloadType.CS_ENTER_ROOM)
                                .setPayload(
                                        CSWebEnterRoomOuterClass.CSWebEnterRoom.newBuilder()
                                                .setToken(roomInitResult.getToken())
                                                .setLiveStreamId(roomInitResult.getLiveStreamId())
                                                .setPageId(RandomUtil.randomString(16) + System.currentTimeMillis())
                                                .build()
                                                .toByteString()
                                )
                                .build()
                )
        );
    }

    public Object getRoomId() {
        return client != null ? client.getConfig().getRoomId() : roomId;
    }

    private String getCookie() {
        return client != null ? client.getConfig().getCookie() : cookie;
    }
}
