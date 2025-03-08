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

package tech.ordinaryroad.live.chat.client.example.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient;
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliConnectionListener;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.constant.RoomInfoGetTypeEnum;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.client.config.BaseLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyin.client.DouyinLiveChatClient;
import tech.ordinaryroad.live.chat.client.douyin.config.DouyinLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinConnectionListener;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener;
import tech.ordinaryroad.live.chat.client.douyu.client.DouyuLiveChatClient;
import tech.ordinaryroad.live.chat.client.douyu.config.DouyuLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuConnectionListener;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.client.KuaishouLiveChatClient;
import tech.ordinaryroad.live.chat.client.kuaishou.config.KuaishouLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouConnectionListener;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.servers.netty.client.base.BaseNettyClient;

/**
 * @author mjz
 * @date 2023/8/21
 */
@RestController
@RequestMapping("client/multiply")
public class MultiplyLiveChatClientController {

    private final IBilibiliMsgListener bilibiliMsgListener;
    private final IBilibiliConnectionListener bilibiliConnectionListener;
    private final IDouyuMsgListener douyuMsgListener;
    private final IDouyuConnectionListener douyuConnectionListener;
    private final IKuaishouMsgListener kuaishouMsgListener;
    private final IKuaishouConnectionListener kuaishouConnectionListener;
    private final IDouyinMsgListener douyinMsgListener;
    private final IDouyinConnectionListener douyinConnectionListener;

    public MultiplyLiveChatClientController(IBilibiliMsgListener bilibiliMsgListener, IBilibiliConnectionListener bilibiliConnectionListener, IDouyuMsgListener douyuMsgListener, IDouyuConnectionListener douyuConnectionListener, IKuaishouMsgListener kuaishouMsgListener, IKuaishouConnectionListener kuaishouConnectionListener, IDouyinMsgListener douyinMsgListener, IDouyinConnectionListener douyinConnectionListener) {
        this.bilibiliMsgListener = bilibiliMsgListener;
        this.bilibiliConnectionListener = bilibiliConnectionListener;
        this.douyuMsgListener = douyuMsgListener;
        this.douyuConnectionListener = douyuConnectionListener;
        this.kuaishouMsgListener = kuaishouMsgListener;
        this.kuaishouConnectionListener = kuaishouConnectionListener;
        this.douyinMsgListener = douyinMsgListener;
        this.douyinConnectionListener = douyinConnectionListener;
    }


    @GetMapping("newClientAndStart/{roomId}")
    public void newClientAndStart(@PathVariable Long roomId, @RequestParam String platform) {
        BaseLiveChatClientConfig config;
        BaseNettyClient client;
        switch (platform) {
            case "bilibili" -> {
                config = BilibiliLiveChatClientConfig.builder()
                        .roomId(roomId)
                        .build();
                client = new BilibiliLiveChatClient((BilibiliLiveChatClientConfig) config, bilibiliMsgListener, bilibiliConnectionListener);
            }
            case "douyu" -> {
                config = DouyuLiveChatClientConfig.builder()
                        .roomId(roomId)
                        .build();
                client = new DouyuLiveChatClient((DouyuLiveChatClientConfig) config, douyuMsgListener, douyuConnectionListener);
            }
            case "kuaishou" -> {
                config = KuaishouLiveChatClientConfig.builder()
                        .roomId(roomId)
                        .roomInfoGetType(RoomInfoGetTypeEnum.NOT_COOKIE)
                        .build();
                client = new KuaishouLiveChatClient((KuaishouLiveChatClientConfig) config, kuaishouMsgListener, kuaishouConnectionListener);
            }
            case "douyin" -> {
                config = DouyinLiveChatClientConfig.builder()
                        .roomId(roomId)
                        .build();
                client = new DouyinLiveChatClient((DouyinLiveChatClientConfig) config, douyinMsgListener, douyinConnectionListener);
            }
            default -> throw new BaseException("暂不支持 " + platform);
        }
        client.connect();
    }

}
