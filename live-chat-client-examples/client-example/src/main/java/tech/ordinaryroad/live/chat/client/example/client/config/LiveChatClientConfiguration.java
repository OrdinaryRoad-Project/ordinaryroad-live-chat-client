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

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliConnectionListener;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener;
import tech.ordinaryroad.live.chat.client.douyu.client.DouyuLiveChatClient;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuConnectionListener;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.client.KuaishouLiveChatClient;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouConnectionListener;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;

/**
 * @author mjz
 * @date 2023/8/21
 */
@Component
public class LiveChatClientConfiguration {

    private final LiveChatClientConfigurations configurations;
    private final IBilibiliMsgListener bilibiliSendSmsReplyMsgListener;
    private final IBilibiliConnectionListener bilibiliConnectionListener;
    private final IDouyuMsgListener douyuCmdMsgListener;
    private final IDouyuConnectionListener douyuConnectionListener;

    private final IKuaishouMsgListener kuaishouMsgListener;
    private final IKuaishouConnectionListener kuaishouConnectionListener;

    public LiveChatClientConfiguration(LiveChatClientConfigurations configurations, IBilibiliMsgListener bilibiliSendSmsReplyMsgListener, IBilibiliConnectionListener bilibiliConnectionListener, IDouyuMsgListener douyuCmdMsgListener, IDouyuConnectionListener douyuConnectionListener, IKuaishouMsgListener kuaishouMsgListener, IKuaishouConnectionListener kuaishouConnectionListener) {
        this.configurations = configurations;
        this.bilibiliSendSmsReplyMsgListener = bilibiliSendSmsReplyMsgListener;
        this.bilibiliConnectionListener = bilibiliConnectionListener;
        this.douyuCmdMsgListener = douyuCmdMsgListener;
        this.douyuConnectionListener = douyuConnectionListener;
        this.kuaishouMsgListener = kuaishouMsgListener;
        this.kuaishouConnectionListener = kuaishouConnectionListener;
    }

    @Bean
    public BilibiliLiveChatClient bilibiliLiveChatClient() {
        return new BilibiliLiveChatClient(configurations.getBilibili(), bilibiliSendSmsReplyMsgListener, bilibiliConnectionListener);
    }

    @Bean
    public DouyuLiveChatClient douyuLiveChatClient() {
        return new DouyuLiveChatClient(configurations.getDouyu(), douyuCmdMsgListener, douyuConnectionListener);
    }

    @Bean
    public KuaishouLiveChatClient kuaishouLiveChatClient() {
        return new KuaishouLiveChatClient(configurations.getKuaishou(), kuaishouMsgListener, kuaishouConnectionListener);
    }

}
