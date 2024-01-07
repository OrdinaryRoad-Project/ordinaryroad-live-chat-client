package tech.ordinaryroad.live.chat.client.kuaishou.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.config.KuaishouLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.PayloadTypeOuterClass;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.WebCommentFeedOuterClass;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.WebGiftFeedOuterClass;

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
//        String cookie ="";
        log.error("cookie: {}", cookie);
        KuaishouLiveChatClientConfig config = KuaishouLiveChatClientConfig.builder()
                // TODO 浏览器Cookie
                .cookie(cookie)
                .roomId("3xpbyu443usqwau")
                .roomId("DX204317461")
                .roomId("xzx11234")
                .roomId("N-ik-T8-20")
                .roomId("3x9f7e3t3fsr74k")
                .roomId("3xiqpb2riusznvq")
                .roomId("QQ2027379716")
                .roomId("xiannvwan1008")
                // 五台山老罗
                .roomId("3xkz5pb2kx3q4u6")
                .build();

        client = new KuaishouLiveChatClient(config, new IKuaishouMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                // log.debug("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
                // log.debug("收到CMD消息{} {}", cmd, cmdMsg);
            }

            @Override
            public void onOtherCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
                log.debug("收到其他CMD消息 {}", cmd);
            }

            @Override
            public void onUnknownCmd(String cmdString, IMsg msg) {
                log.debug("收到未知CMD消息 {}", cmdString);
            }

            @Override
            public void onDanmuMsg(KuaishouBinaryFrameHandler binaryFrameHandler, WebCommentFeedOuterClass.WebCommentFeed msg) {
                log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onGiftMsg(KuaishouBinaryFrameHandler binaryFrameHandler, WebGiftFeedOuterClass.WebGiftFeed msg) {
                log.info("{} 收到礼物 {}({}) {} {}({})x{}({})", binaryFrameHandler.getRoomId(), msg.getUsername(), msg.getUid(), "赠送", msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice());
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