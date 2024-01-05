package tech.ordinaryroad.live.chat.client.kuaishou.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.config.KuaishouLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.kuaishou_websocket_danmu_msg;

/**
 * @author mjz
 * @date 2024/1/5
 */
@Slf4j
class KuaishouLiveChatClientTest {

    static Object lock = new Object();
    KuaishouLiveChatClient client;

    @Test
    void example() throws InterruptedException {
        String cookie = System.getenv("cookie");
        log.error("cookie: {}", cookie);
        KuaishouLiveChatClientConfig config = KuaishouLiveChatClientConfig.builder()
                // TODO 浏览器Cookie
                .cookie(cookie)
                .roomId("N-ik-T8-20")
                .roomId("3xpbyu443usqwau")
                .build();

        client = new KuaishouLiveChatClient(config, new IKuaishouMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                log.debug("收到消息 {}", msg);
            }

            @Override
            public void onDanmuMsg(KuaishouBinaryFrameHandler binaryFrameHandler, kuaishou_websocket_danmu_msg msg) {
                log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
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