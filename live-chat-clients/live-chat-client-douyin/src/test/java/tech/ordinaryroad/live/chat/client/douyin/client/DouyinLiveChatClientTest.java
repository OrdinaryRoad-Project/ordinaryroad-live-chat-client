package tech.ordinaryroad.live.chat.client.douyin.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.douyin.config.DouyinLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener;

/**
 * @author mjz
 * @date 2024/1/2
 */
@Slf4j
class DouyinLiveChatClientTest {

    static Object lock = new Object();
    DouyinLiveChatClient client;

    @Test
    void example() throws InterruptedException {
        String cookie = System.getenv("cookie");
        log.error("cookie: {}", cookie);
        DouyinLiveChatClientConfig config = DouyinLiveChatClientConfig.builder()
                // TODO 浏览器Cookie
                .cookie(cookie)
                .roomId("renyixu1989")
                .roomId("567789235524")
                .build();

        client = new DouyinLiveChatClient(config, new IDouyinMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                log.debug("收到消息 {}", msg);
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