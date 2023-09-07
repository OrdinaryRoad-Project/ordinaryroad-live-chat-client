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

package tech.ordinaryroad.live.chat.client.bilibili.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient;
import tech.ordinaryroad.live.chat.client.bilibili.constant.BilibiliCmdEnum;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliSendSmsReplyMsgListener;
import tech.ordinaryroad.live.chat.client.bilibili.msg.SendSmsReplyMsg;
import tech.ordinaryroad.live.chat.client.bilibili.msg.base.IBilibiliMsg;
import tech.ordinaryroad.live.chat.client.bilibili.util.BilibiliCodecUtil;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseCmdMsg;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientBinaryFrameHandler;

import java.util.List;


/**
 * 消息处理器
 *
 * @author mjz
 * @date 2023/1/4
 */
@Slf4j
@ChannelHandler.Sharable
public class BilibiliBinaryFrameHandler extends BaseNettyClientBinaryFrameHandler<BilibiliLiveChatClient, BilibiliBinaryFrameHandler, BilibiliCmdEnum, IBilibiliMsg, SendSmsReplyMsg, IBilibiliSendSmsReplyMsgListener> {

    public BilibiliBinaryFrameHandler(IBilibiliSendSmsReplyMsgListener listener, BilibiliLiveChatClient client) {
        super(listener, client);
    }

    public BilibiliBinaryFrameHandler(IBilibiliSendSmsReplyMsgListener listener, long roomId) {
        super(listener, roomId);
    }

    @Override
    public void onCmdMsg(BilibiliCmdEnum cmd, BaseCmdMsg<BilibiliCmdEnum> cmdMsg) {
        super.onCmdMsg(cmd, cmdMsg);

        if (super.listener == null) {
            return;
        }

        SendSmsReplyMsg sendSmsReplyMsg = (SendSmsReplyMsg) cmdMsg;
        switch (cmd) {
            case DANMU_MSG -> listener.onDanmuMsg(BilibiliBinaryFrameHandler.this, sendSmsReplyMsg);
            case SEND_GIFT -> listener.onSendGift(BilibiliBinaryFrameHandler.this, sendSmsReplyMsg);
            case INTERACT_WORD -> listener.onEnterRoom(BilibiliBinaryFrameHandler.this, sendSmsReplyMsg);
            case ENTRY_EFFECT -> listener.onEntryEffect(BilibiliBinaryFrameHandler.this, sendSmsReplyMsg);
            case WATCHED_CHANGE -> listener.onWatchedChange(BilibiliBinaryFrameHandler.this, sendSmsReplyMsg);
            case LIKE_INFO_V3_CLICK -> listener.onClickLike(BilibiliBinaryFrameHandler.this, sendSmsReplyMsg);
            case LIKE_INFO_V3_UPDATE -> listener.onClickUpdate(BilibiliBinaryFrameHandler.this, sendSmsReplyMsg);
            case HOT_RANK_CHANGED_V2 -> {
                // TODO 主播实时活动排名变化
                listener.onOtherCmdMsg(BilibiliBinaryFrameHandler.this, cmd, sendSmsReplyMsg);
            }
            case ONLINE_RANK_COUNT -> {
                // TODO 高能榜数量更新
                listener.onOtherCmdMsg(BilibiliBinaryFrameHandler.this, cmd, sendSmsReplyMsg);
            }
            case ROOM_REAL_TIME_MESSAGE_UPDATE -> {
                // TODO 主播粉丝信息更新
                listener.onOtherCmdMsg(BilibiliBinaryFrameHandler.this, cmd, sendSmsReplyMsg);
            }
            case STOP_LIVE_ROOM_LIST -> {
                // TODO 停止直播的房间ID列表
                listener.onOtherCmdMsg(BilibiliBinaryFrameHandler.this, cmd, sendSmsReplyMsg);
            }
            case ONLINE_RANK_V2 -> {
                // TODO 高能用户排行榜 更新
                listener.onOtherCmdMsg(BilibiliBinaryFrameHandler.this, cmd, sendSmsReplyMsg);
            }
            default -> {
                listener.onOtherCmdMsg(BilibiliBinaryFrameHandler.this, cmd, cmdMsg);
            }
        }
    }

    @Override
    protected List<IBilibiliMsg> decode(ByteBuf byteBuf) {
        return BilibiliCodecUtil.decode(byteBuf);
    }
}
