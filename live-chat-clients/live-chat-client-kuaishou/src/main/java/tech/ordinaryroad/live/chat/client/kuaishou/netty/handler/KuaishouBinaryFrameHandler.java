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

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.client.KuaishouLiveChatClient;
import tech.ordinaryroad.live.chat.client.kuaishou.constant.KuaishouCmdEnum;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.msg.base.IKuaishouMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.kuaishou_websocket_danmu_msg;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.kuaishou_websocket_frame;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientBinaryFrameHandler;

import java.util.Collections;
import java.util.List;

/**
 * @author mjz
 * @date 2024/1/5
 */
@Slf4j
@ChannelHandler.Sharable
public class KuaishouBinaryFrameHandler extends BaseNettyClientBinaryFrameHandler<KuaishouLiveChatClient, KuaishouBinaryFrameHandler, KuaishouCmdEnum, IKuaishouMsg, IKuaishouMsgListener> {

    public KuaishouBinaryFrameHandler(List<IKuaishouMsgListener> iKuaishouMsgListeners, KuaishouLiveChatClient client) {
        super(iKuaishouMsgListeners, client);
    }

    public KuaishouBinaryFrameHandler(List<IKuaishouMsgListener> iKuaishouMsgListeners, long roomId) {
        super(iKuaishouMsgListeners, roomId);
    }

    @SneakyThrows
    @Override
    public void onCmdMsg(KuaishouCmdEnum cmd, ICmdMsg<KuaishouCmdEnum> cmdMsg) {
        if (super.msgListeners.isEmpty()) {
            return;
        }

        ByteString payload = ((kuaishou_websocket_frame) cmdMsg).getPayload();
        switch (cmd) {
            case DANMU -> {
                kuaishou_websocket_danmu_msg kuaishouWebsocketDanmuFrame = kuaishou_websocket_danmu_msg.parseFrom(payload);
                iteratorMsgListeners(msgListener -> msgListener.onDanmuMsg(KuaishouBinaryFrameHandler.this, kuaishouWebsocketDanmuFrame));
            }
            default ->
                    iteratorMsgListeners(msgListener -> msgListener.onOtherCmdMsg(KuaishouBinaryFrameHandler.this, cmd, cmdMsg));
        }
    }

    @Override
    protected List<IKuaishouMsg> decode(ByteBuf byteBuf) {
        try {
            return Collections.singletonList(kuaishou_websocket_frame.parseFrom(byteBuf.nioBuffer()));
        } catch (InvalidProtocolBufferException e) {
            throw new BaseException(e);
        }
    }
}
