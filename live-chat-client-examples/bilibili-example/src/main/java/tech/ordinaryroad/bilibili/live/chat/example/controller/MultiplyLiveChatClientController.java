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

package tech.ordinaryroad.bilibili.live.chat.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient;
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliSendSmsReplyMsgListener;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliConnectionHandler;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;

/**
 * @author mjz
 * @date 2023/8/21
 */
@RestController
@RequestMapping("client/multiply")
public class MultiplyLiveChatClientController {

    @Autowired
    private IBilibiliSendSmsReplyMsgListener bilibiliSendSmsReplyMsgListener;
    @Autowired
    private IBaseConnectionListener<BilibiliConnectionHandler> bilibiliConnectionListener;

    @GetMapping("newClientAndStart/{roomId}")
    public void newClientAndStart(@PathVariable Long roomId) {
        BilibiliLiveChatClientConfig config = BilibiliLiveChatClientConfig.builder()
                .roomId(roomId)
                .build();
        new BilibiliLiveChatClient(config, bilibiliSendSmsReplyMsgListener, bilibiliConnectionListener)
                .connect();
    }

}
