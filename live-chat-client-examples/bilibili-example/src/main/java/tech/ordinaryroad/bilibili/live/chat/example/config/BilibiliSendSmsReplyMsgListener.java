package tech.ordinaryroad.bilibili.live.chat.example.config;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.ordinaryroad.live.chat.client.bilibili.constant.BilibiliCmdEnum;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliSendSmsReplyMsgListener;
import tech.ordinaryroad.live.chat.client.bilibili.msg.SendSmsReplyMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseCmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;

/**
 * @author mjz
 * @date 2023/8/21
 */
@Slf4j
@Service
public class BilibiliSendSmsReplyMsgListener implements IBilibiliSendSmsReplyMsgListener {

    @Override
    public void onDanmuMsg(SendSmsReplyMsg msg) {
        JsonNode info = msg.getInfo();
        JsonNode jsonNode1 = info.get(1);
        String danmuText = jsonNode1.asText();
        JsonNode jsonNode2 = info.get(2);
        Long uid = jsonNode2.get(0).asLong();
        String uname = jsonNode2.get(1).asText();
        log.info("收到弹幕 {}({})：{}", uname, uid, danmuText);
    }

    @Override
    public void onSendGift(SendSmsReplyMsg msg) {
        JsonNode data = msg.getData();
        String action = data.get("action").asText();
        String giftName = data.get("giftName").asText();
        Integer num = data.get("num").asInt();
        String uname = data.get("uname").asText();
        Integer price = data.get("price").asInt();
        log.info("收到礼物 {} {} {}x{}({})", uname, action, giftName, num, price);
    }

    @Override
    public void onEnterRoom(SendSmsReplyMsg msg) {
        log.debug("普通用户进入直播间 {}", msg.getData().get("uname").asText());
    }

    @Override
    public void onEntryEffect(SendSmsReplyMsg msg) {
        JsonNode data = msg.getData();
        String copyWriting = data.get("copy_writing").asText();
        log.info("入场效果 {}", copyWriting);
    }

    @Override
    public void onWatchedChange(SendSmsReplyMsg msg) {
        JsonNode data = msg.getData();
        int num = data.get("num").asInt();
        String textSmall = data.get("text_small").asText();
        String textLarge = data.get("text_large").asText();
        log.debug("观看人数变化 {} {} {}", num, textSmall, textLarge);
    }

    @Override
    public void onClickLike(SendSmsReplyMsg msg) {
        JsonNode data = msg.getData();
        String uname = data.get("uname").asText();
        String likeText = data.get("like_text").asText();
        log.debug("为主播点赞 {} {}", uname, likeText);
    }

    @Override
    public void onClickUpdate(SendSmsReplyMsg msg) {
        JsonNode data = msg.getData();
        int clickCount = data.get("click_count").asInt();
        log.debug("点赞数更新 {}", clickCount);
    }

    @Override
    public void onMsg(IMsg msg) {
        log.debug("收到{}消息 {}", msg.getClass(), msg);
    }

    @Override
    public void onCmdMsg(BilibiliCmdEnum cmd, BaseCmdMsg<BilibiliCmdEnum> cmdMsg) {
        log.debug("收到CMD消息{} {}", cmd, cmdMsg);
    }

    @Override
    public void onOtherCmdMsg(BilibiliCmdEnum cmd, BaseCmdMsg<BilibiliCmdEnum> cmdMsg) {
        log.debug("收到其他CMD消息 {}", cmd);
    }

    @Override
    public void onUnknownCmd(String cmdString, BaseMsg msg) {
        log.debug("收到未知CMD消息 {}", cmdString);
    }
}
