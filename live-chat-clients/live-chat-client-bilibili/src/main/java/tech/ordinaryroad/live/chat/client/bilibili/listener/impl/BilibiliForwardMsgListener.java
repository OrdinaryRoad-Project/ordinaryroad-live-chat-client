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

package tech.ordinaryroad.live.chat.client.bilibili.listener.impl;

import cn.hutool.core.util.StrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener;
import tech.ordinaryroad.live.chat.client.bilibili.msg.*;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.websocket.client.WebSocketLiveChatClient;
import tech.ordinaryroad.live.chat.client.websocket.config.WebSocketLiveChatClientConfig;

import java.nio.charset.StandardCharsets;

/**
 * @author mjz
 * @date 2024/3/8
 * @since 0.3.0
 */
public class BilibiliForwardMsgListener implements IBilibiliMsgListener {

    private final WebSocketLiveChatClient webSocketLiveChatClient;

    public BilibiliForwardMsgListener(String webSocketUri) {
        if (StrUtil.isBlank(webSocketUri)) {
            throw new BaseException("转发地址不能为空");
        }
        webSocketLiveChatClient = new WebSocketLiveChatClient(
                WebSocketLiveChatClientConfig.builder()
                        .websocketUri(webSocketUri)
                        .build()
        );
        webSocketLiveChatClient.connect();
    }

    @Override
    public void onDanmuMsg(DanmuMsgMsg msg) {
        forward(msg);
    }

    @Override
    public void onGiftMsg(SendGiftMsg msg) {
        forward(msg);
    }

    @Override
    public void onSuperChatMsg(SuperChatMessageMsg msg) {
        forward(msg);
    }

    @Override
    public void onEnterRoomMsg(InteractWordMsg msg) {
        forward(msg);
    }

    @Override
    public void onLikeMsg(LikeInfoV3ClickMsg msg) {
        forward(msg);
    }

    @Override
    public void onEntryEffect(SendSmsReplyMsg msg) {
        forward(msg);
    }

    @Override
    public void onWatchedChange(SendSmsReplyMsg msg) {
        forward(msg);
    }

    @Override
    public void onClickUpdate(SendSmsReplyMsg msg) {
        forward(msg);
    }

    @Override
    public void onLiveStatusMsg(BilibiliLiveStatusChangeMsg msg) {
        forward(msg);
    }

    private void forward(IMsg msg) {
        if (webSocketLiveChatClient == null) {
            return;
        }
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeCharSequence(msg.toString(), StandardCharsets.UTF_8);
        webSocketLiveChatClient.send(new BinaryWebSocketFrame(byteBuf));
    }
}
