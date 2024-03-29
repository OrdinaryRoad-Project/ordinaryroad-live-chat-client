package tech.ordinaryroad.live.chat.client.douyin.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.douyin.api.DouyinApis;
import tech.ordinaryroad.live.chat.client.douyin.config.DouyinLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyin.constant.DouyinCmdEnum;
import tech.ordinaryroad.live.chat.client.douyin.constant.DouyinRoomStatusEnum;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener;
import tech.ordinaryroad.live.chat.client.douyin.msg.*;
import tech.ordinaryroad.live.chat.client.douyin.netty.handler.DouyinBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.douyin.protobuf.douyin_cmd_msg;
import tech.ordinaryroad.live.chat.client.douyin.protobuf.douyin_webcast_gift_message_msg;
import tech.ordinaryroad.live.chat.client.douyin.protobuf.douyin_webcast_room_stats_message_msg;
import tech.ordinaryroad.live.chat.client.douyin.protobuf.douyin_webcast_social_message_msg;

import java.io.IOException;
import java.util.List;

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
                // .forwardWebsocketUri("ws://127.0.0.1:8080/websocket")

                // TODO 浏览器Cookie
                .cookie(cookie)
                .roomId("renyixu1989")
                .roomId("567789235524")
                .roomId("166163409118")
                .roomId("722266687616")
                .roomId("o333")
                .roomId("qilongmusic")
                .roomId("yimei20210922")
                .build();

        client = new DouyinLiveChatClient(config, new IDouyinMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                // log.debug("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(DouyinCmdEnum cmd, ICmdMsg<DouyinCmdEnum> cmdMsg) {
                log.debug("收到CMD消息{} {}", cmd, cmdMsg);
            }

            @Override
            public void onOtherCmdMsg(DouyinCmdEnum cmd, ICmdMsg<DouyinCmdEnum> cmdMsg) {
                log.debug("收到其他CMD消息 {}", cmd);
                switch (cmd) {
                    case WebcastRoomStatsMessage: {
                        DouyinRoomStatsMsg douyinRoomStatsMsg = (DouyinRoomStatsMsg) cmdMsg;
                        douyin_webcast_room_stats_message_msg msg = douyinRoomStatsMsg.getMsg();
                        break;
                    }
                    case WebcastSocialMessage: {
                        DouyinSocialMsg douyinSocialMsg = (DouyinSocialMsg) cmdMsg;
                        douyin_webcast_social_message_msg msg = douyinSocialMsg.getMsg();
                        switch ((int) msg.getAction()){
                            case 1:
                                // 关注
                                break;
                            case 3:
                                // 分享
                                break;
                            default:
                        }
                        break;
                    }
                    default: {
                        // ignore
                    }
                }
            }

            @Override
            public void onUnknownCmd(String cmdString, IMsg msg) {
                log.debug("收到未知CMD消息 {}", cmdString);
            }

            @Override
            public void onDanmuMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinDanmuMsg msg) {
                log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onGiftMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinGiftMsg msg) {
                douyin_webcast_gift_message_msg douyinWebcastGiftMessageMsg = msg.getMsg();

                boolean combo = douyinWebcastGiftMessageMsg.getGift().getCombo();

                long msgId = douyinWebcastGiftMessageMsg.getCommon().getMsgId();
                long comboCount = douyinWebcastGiftMessageMsg.getComboCount();
                long groupId = douyinWebcastGiftMessageMsg.getGroupId();
                long groupCount = douyinWebcastGiftMessageMsg.getGroupCount();
                long totalCount = douyinWebcastGiftMessageMsg.getTotalCount();
                String traceId = douyinWebcastGiftMessageMsg.getTraceId();
                long repeatCount = douyinWebcastGiftMessageMsg.getRepeatCount();
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