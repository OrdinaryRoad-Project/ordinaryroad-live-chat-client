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

package tech.ordinaryroad.live.chat.client.douyu.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseCmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.douyu.config.DouyuLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyu.constant.DouyuCmdEnum;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuDouyuCmdMsgListener;
import tech.ordinaryroad.live.chat.client.douyu.msg.ChatmsgMsg;
import tech.ordinaryroad.live.chat.client.douyu.netty.handler.DouyuConnectionHandler;

/**
 * @author mjz
 * @date 2023/8/26
 */
@Slf4j
class DouyuLiveChatClientTest {

    static Object lock = new Object();
    DouyuLiveChatClient client;

    @Test
    void example() throws InterruptedException {
        String cookie = System.getenv("cookie");
        log.error("cookie: {}", cookie);
        DouyuLiveChatClientConfig config = DouyuLiveChatClientConfig.builder()
                // FIXME: 暂不支持浏览器Cookie
                .cookie(cookie)
                // FIXME: 暂不支持短id
                .roomId(3168536)
                .build();

        client = new DouyuLiveChatClient(config, new IDouyuDouyuCmdMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                // log.info("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onDanmuMsg(ChatmsgMsg msg) {
                log.info("收到弹幕 {}({})：{}", msg.getNn(), msg.getUid(), msg.getTxt());
            }

            @Override
            public void onCmdMsg(DouyuCmdEnum cmd, BaseCmdMsg<DouyuCmdEnum> cmdMsg) {
                // log.info("收到CMD消息{} {}", cmd, cmdMsg);
            }

            @Override
            public void onOtherCmdMsg(DouyuCmdEnum cmd, BaseCmdMsg<DouyuCmdEnum> cmdMsg) {
                log.debug("收到其他CMD消息 {}", cmd);
            }

            @Override
            public void onUnknownCmd(String cmdString, BaseMsg msg) {
                log.debug("收到未知CMD消息 {}", cmdString);
            }
        }, new IBaseConnectionListener<DouyuConnectionHandler>() {
            @Override
            public void onConnected() {
                log.info("onConnected");
            }

            @Override
            public void onConnectFailed(DouyuConnectionHandler douyuConnectionHandler) {
                log.info("onConnectFailed");
            }

            @Override
            public void onDisconnected(DouyuConnectionHandler douyuConnectionHandler) {
                log.info("onDisconnected");
            }
        });
        client.connect();

        // 防止测试时直接退出
        while (true) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

}