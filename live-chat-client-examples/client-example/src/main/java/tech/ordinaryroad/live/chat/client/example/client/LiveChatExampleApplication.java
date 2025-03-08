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

package tech.ordinaryroad.live.chat.client.example.client;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient;
import tech.ordinaryroad.live.chat.client.douyin.client.DouyinLiveChatClient;
import tech.ordinaryroad.live.chat.client.douyu.client.DouyuLiveChatClient;
import tech.ordinaryroad.live.chat.client.kuaishou.client.KuaishouLiveChatClient;

@Slf4j
@SpringBootApplication
public class LiveChatExampleApplication {

    @Resource
    private BilibiliLiveChatClient bilibiliLiveChatClient;
    @Resource
    private DouyuLiveChatClient douyuLiveChatClient;
    @Resource
    private KuaishouLiveChatClient kuaishouLiveChatClient;
    @Resource
    private DouyinLiveChatClient douyinLiveChatClient;
    
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("run");
        SpringApplication.run(LiveChatExampleApplication.class, args);
        stopWatch.stop();
        log.info("run end！ {}", stopWatch.prettyPrint());
    }

    @PreDestroy
    public void preDestroy() {
        douyuLiveChatClient.destroy();
        bilibiliLiveChatClient.destroy();
        kuaishouLiveChatClient.destroy();
        douyinLiveChatClient.destroy();
    }

}
