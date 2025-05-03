package tech.ordinaryroad.live.chat.client.douyin.browser;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UnknownFieldSet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinCmdEnum;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.*;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ChatMessage;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.GiftMessage;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Message;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.User;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener;
import tech.ordinaryroad.live.chat.client.douyin.netty.handler.DouyinBinaryFrameHandler;

import java.util.List;
import java.util.Map;

/**
 * @author mjz
 * @date 2025/5/3
 */
@Slf4j
class DouyinBrowserLiveChatClientTest {

    @Test
    @SneakyThrows
    void example() {
//        FetcherOptions options = new FetcherOptions();
//        options.setCacheDir("/Users/ordinaryroad/Downloads/puppeteer-cache");
//
//        BrowserFetcher browserFetcher = new BrowserFetcher(options);
//        browserFetcher.setDownloadsFolder("/Users/ordinaryroad/Downloads/puppeteer");
//        RevisionInfo revisionInfo = browserFetcher.downloadBrowser();
//        String executablePath = revisionInfo.getExecutablePath();
//        log.warn("executablePath: {}", executablePath);

        String cookie = System.getenv("cookie");
        log.error("cookie: {}", cookie);
        DouyinBrowserLiveChatClientConfig config = DouyinBrowserLiveChatClientConfig.builder()
                .cookie(cookie)
                // 与辉同行
                .roomId("646454278948")
                .build();
        config.getLaunchOptions().setExecutablePath("Chrome浏览器位置");

        DouyinBrowserLiveChatClient client = new DouyinBrowserLiveChatClient(config, new IDouyinMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                // log.debug("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(DouyinCmdEnum cmd, ICmdMsg<DouyinCmdEnum> cmdMsg) {
                // log.debug("收到CMD消息{} {}", cmd, cmdMsg);
            }

            @Override
            public void onOtherCmdMsg(DouyinCmdEnum cmd, ICmdMsg<DouyinCmdEnum> cmdMsg) {
                // log.debug("收到其他CMD消息 {}", cmd);
            }

            @Override
            public void onUnknownCmd(String cmdString, IMsg msg) {
                // log.debug("收到未知CMD消息 {}", cmdString);
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
                ChatMessage chatMessage = msg.getMsg();
                User user = chatMessage.getUser();
                UnknownFieldSet.Field field = user.getUnknownFields().getField(73);
                List<ByteString> lengthDelimitedList = field.getLengthDelimitedList();
                ByteString bytes = lengthDelimitedList.get(0);
                String string = bytes.toStringUtf8();
                System.out.println(string);
                log.info("收到弹幕 {} {}({})：{}", msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
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
                    log.info("收到礼物 {} {}({}) {} {}({})x{}({}) msgId:{}, groupId:{}, groupCount:{}, comboCount:{}, totalCount:{}, repeatCount:{}, traceId:{}, combo:{}", msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), "赠送", msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice(), msgId, groupId, groupCount, comboCount, totalCount, repeatCount, traceId, combo);
                }
            }

            @Override
            public void onLikeMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinLikeMsg msg) {
                log.info("收到点赞 [{}] {}({})x{}", msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getClickCount());
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
                log.info("统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
            }

            @Override
            public void onSocialMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinSocialMsg msg) {
                log.info("社交消息 {} {} {}({})", msg.getSocialAction(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid());
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

        // 防止测试时直接退出
        while (true) {
            System.in.read();
        }
    }

}