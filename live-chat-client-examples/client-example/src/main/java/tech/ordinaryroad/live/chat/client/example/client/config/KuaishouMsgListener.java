package tech.ordinaryroad.live.chat.client.example.client.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.api.KuaishouApis;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.constant.RoomInfoGetTypeEnum;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouDanmuMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouGiftMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouRoomStatsMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.config.KuaishouLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouBinaryFrameHandler;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class KuaishouMsgListener implements IKuaishouMsgListener {
    @Resource
    private LiveChatClientConfigurations liveChatClientConfigurations;
    @Resource
    private KuaishouLiveChatClient kuaishouLiveChatClient;

    @Override
    public void onDanmuMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouDanmuMsg msg) {
        IKuaishouMsgListener.super.onDanmuMsg(binaryFrameHandler, msg);
        KuaishouLiveChatClientConfig kuaishou = liveChatClientConfigurations.getKuaishou();
        String content = msg.getContent();
        String uid = msg.getUid();
        log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), uid, content);

        // TODO 可以用大模型进行FAQ回复
        // String answer = content + "  的回复";
        // kuaishouLiveChatClient.sendDanmu(answer);
    }

    @Override
    public void onGiftMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouGiftMsg msg) {
        IKuaishouMsgListener.super.onGiftMsg(binaryFrameHandler, msg);
        //log.info("{} 收到礼物 {} {}({}) {} {}({})x{}({})", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getUid(), msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice());
    }


    @Override
    public void onRoomStatsMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouRoomStatsMsg msg) {
        IKuaishouMsgListener.super.onRoomStatsMsg(binaryFrameHandler, msg);
        log.info("{} 统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", binaryFrameHandler.getRoomId(), msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
    }


    @Override
    public void onMsg(IMsg msg) {
//        KuaishouCmdMsg  cmdMsg = (KuaishouCmdMsg) msg;
//log info("收到{}消息 {}", msg.getClass(), msg);
//        log.info("收到{}消息 {}", msg.getClass(), msg);
    }

    @Override
    public void onUnknownCmd(String cmdString, IMsg msg) {
        log.info("收到未知CMD消息 {}", cmdString);
    }
}
