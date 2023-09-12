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

package tech.ordinaryroad.live.chat.client.example.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuMsgListener;
import tech.ordinaryroad.live.chat.client.douyu.msg.ChatmsgMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.DgbMsg;
import tech.ordinaryroad.live.chat.client.douyu.netty.handler.DouyuBinaryFrameHandler;

/**
 * @author mjz
 * @date 2023/9/3
 */
@Slf4j
@Primary
@Service
public class DouyuMsgListener implements IDouyuMsgListener {

    @Override
    public void onDanmuMsg(DouyuBinaryFrameHandler binaryFrameHandler, ChatmsgMsg msg) {
        IDouyuMsgListener.super.onDanmuMsg(binaryFrameHandler, msg);

        log.info("{} 收到弹幕 {}({})：{}", binaryFrameHandler.getRoomId(), msg.getUsername(), msg.getUid(), msg.getContent());
    }

    @Override
    public void onGiftMsg(DouyuBinaryFrameHandler binaryFrameHandler, DgbMsg msg) {
        IDouyuMsgListener.super.onGiftMsg(binaryFrameHandler, msg);

        log.info("{} 收到礼物 {}({}) {} {}({})x{}({})", binaryFrameHandler.getRoomId(), msg.getUsername(), msg.getUid(), "赠送", "`礼物名未知`", msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice());
    }

}
