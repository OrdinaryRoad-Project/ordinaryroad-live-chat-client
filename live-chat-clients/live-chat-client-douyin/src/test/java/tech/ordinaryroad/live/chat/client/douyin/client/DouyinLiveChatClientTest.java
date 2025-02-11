package tech.ordinaryroad.live.chat.client.douyin.client;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UnknownFieldSet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinCmdEnum;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.*;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.GiftMessage;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Message;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.douyin.config.DouyinLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener;
import tech.ordinaryroad.live.chat.client.douyin.netty.handler.DouyinBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.plugin.forward.ForwardMsgPlugin;

import java.util.Map;

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
//                .cookie(cookie)
                .roomId("renyixu1989")
                .roomId("166163409118")
                .roomId("722266687616")
                .roomId("o333")
                .roomId("qilongmusic")
                .roomId("28945346664")
                .roomId("172783448140")
                .roomId("yimei20210922")
                .roomId("964068287342")
                .roomId("602796691815")
                .roomId("567789235524")
                .roomId("678776111594")
                .roomId("63592101250")
                .roomId("816299952901")
                .roomId("929343306831")

                .roomId("929349829592")


                // 弹幕游戏2
                .roomId("358618109921")

                .roomId("569480017381")
                .roomId("660292215268")

                .roomId("988498434970")

                // 弹幕游戏1
                .roomId("47761745807")


                .roomId("63592101250")

                .roomId("193059181093")
                .roomId("260408198898")

                .roomId("81404386588")
                .roomId("301753121309")
                .roomId("447584845941")

                .roomId("927787683941")
                .roomId("646454278948")
                // 测试连麦
                .roomId("335835086616")
                .roomId("723583785905")
                .build();

        client = new DouyinLiveChatClient(config, new IDouyinMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                log.debug("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(DouyinCmdEnum cmd, ICmdMsg<DouyinCmdEnum> cmdMsg) {
                // log.debug("收到CMD消息{} {}", cmd, cmdMsg);
            }

            @Override
            public void onOtherCmdMsg(DouyinCmdEnum cmd, ICmdMsg<DouyinCmdEnum> cmdMsg) {
                log.debug("收到其他CMD消息 {}", cmd);
            }

            @Override
            public void onUnknownCmd(String cmdString, IMsg msg) {
                log.debug("收到未知CMD消息 {}", cmdString);
                Message douyin_cmd_msg = ((DouyinCmdMsg) msg).getMsg();
                ByteString payload = douyin_cmd_msg.getPayload();
                try {
                    UnknownFieldSet unknownFieldSet = UnknownFieldSet.parseFrom(payload);
                    Map<Integer, UnknownFieldSet.Field> map = unknownFieldSet.asMap();
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onDanmuMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinDanmuMsg msg) {
                log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onGiftMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinGiftMsg msg) {
                GiftMessage giftMessage = msg.getMsg();

                boolean combo = giftMessage.getGift().getCombo();

                long msgId = giftMessage.getCommon().getMsgId();
                long comboCount = giftMessage.getComboCount();
                long groupId = giftMessage.getGroupId();
                long groupCount = giftMessage.getGroupCount();
                long totalCount = giftMessage.getTotalCount();
                String traceId = giftMessage.getTraceId();
                long repeatCount = giftMessage.getRepeatCount();
                if (msg.getGiftCount() > 0) {
                    log.info("{} 收到礼物 {} {}({}) {} {}({})x{}({}) msgId:{}, groupId:{}, groupCount:{}, comboCount:{}, totalCount:{}, repeatCount:{}, traceId:{}, combo:{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), "赠送", msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice(), msgId, groupId, groupCount, comboCount, totalCount, repeatCount, traceId, combo);
                }
            }

            @Override
            public void onLikeMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinLikeMsg msg) {
                log.info("{} 收到点赞 [{}] {}({})x{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getClickCount());
            }

            @Override
            public void onEnterRoomMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinEnterRoomMsg msg) {
                log.info("{} {}({}) 进入直播间", msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid());
            }

            @Override
            public void onLiveStatusMsg(DouyinBinaryFrameHandler douyinBinaryFrameHandler, DouyinControlMsg douyinControlMsg) {
                log.error("状态变化: {}", douyinControlMsg.getMsg().getStatus());
            }

            @Override
            public void onRoomStatsMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinRoomStatsMsg msg) {
                IDouyinMsgListener.super.onRoomStatsMsg(binaryFrameHandler, msg);
                log.info("{} 统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", binaryFrameHandler.getRoomId(), msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
            }

            @Override
            public void onSocialMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinSocialMsg msg) {
                log.info("{} 社交消息 {} {} {}({})", binaryFrameHandler.getRoomId(), msg.getSocialAction(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid());
            }
        });

        client.addStatusChangeListener((evt, oldStatus, newStatus) -> {
            if (newStatus == ClientStatusEnums.CONNECTED) {
                log.warn("{} 已连接", client.getConfig().getRoomId());
                log.warn("直播间标题 {}", client.getRoomInitResult().getRoomTitle());
                log.warn("房间直播状态: {}", client.getRoomInitResult().getRoomLiveStatus());
            }
        });

        client.connect();

        //        ThreadUtil.execAsync(() -> {
//            ThreadUtil.sleep(5000);
//            client.disconnect();
//        });

        // 防止测试时直接退出
        while (true) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    @Test
    void giftTest() throws Exception {
        DouyinLiveChatClient douyinLiveChatClientTest = new DouyinLiveChatClient(DouyinLiveChatClientConfig.builder()
                .roomId("407702376467")
                .roomId("507978586806")
                .build());

        douyinLiveChatClientTest.addMsgListener(new IDouyinMsgListener() {
            @Override
            public void onGiftMsg(DouyinGiftMsg msg) {
                if (msg.getGiftCount() > 0) {
                    log.info("收到礼物 {} 送出 {}x{}", msg.getUsername(), msg.getGiftName(), msg.getGiftCount());
                }
            }

            @Override
            public void onOtherCmdMsg(DouyinCmdEnum cmd, ICmdMsg<DouyinCmdEnum> cmdMsg) {
                log.warn("收到其他CMD消息 {} {}", cmd, cmdMsg);
//                if (cmd == DouyinCmdEnum.WebcastFansclubMessage) {
//                    DouyinCmdMsgOuterClass.DouyinCmdMsg msg = ((DouyinCmdMsg) cmdMsg).getMsg();
//                    ByteString payload = msg.getPayload();
//                    log.warn(payload.toStringUtf8());
//                }
            }
        });

        douyinLiveChatClientTest.connect();

        while (true) {
            System.in.read();
        }
    }

    @SneakyThrows
    @Test
    void forwardMsgTest() {
        String forwardWebsocketUri = "ws://localhost:8080/websocket";
        DouyinLiveChatClient liveChatClient = new DouyinLiveChatClient(DouyinLiveChatClientConfig.builder()
                .forwardWebsocketUri(forwardWebsocketUri)
                .roomId(646454278948L)
                .build()
        );
        liveChatClient.addPlugin(new ForwardMsgPlugin(forwardWebsocketUri));
        liveChatClient.connect();
        while (true) {
            System.in.read();
        }
    }

}