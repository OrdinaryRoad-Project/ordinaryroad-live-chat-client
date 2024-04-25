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

package tech.ordinaryroad.live.chat.client.bilibili.listener;

import tech.ordinaryroad.live.chat.client.bilibili.constant.BilibiliCmdEnum;
import tech.ordinaryroad.live.chat.client.bilibili.msg.*;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.commons.base.listener.*;

/**
 * @author mjz
 * @date 2023/1/7
 */
public interface IBilibiliMsgListener extends IBaseMsgListener<BilibiliBinaryFrameHandler, BilibiliCmdEnum>,
        IDanmuMsgListener<BilibiliBinaryFrameHandler, DanmuMsgMsg>,
        IGiftMsgListener<BilibiliBinaryFrameHandler, SendGiftMsg>,
        ISuperChatMsgListener<BilibiliBinaryFrameHandler, SuperChatMessageMsg>,
        IEnterRoomMsgListener<BilibiliBinaryFrameHandler, InteractWordMsg>,
        ILikeMsgListener<BilibiliBinaryFrameHandler, LikeInfoV3ClickMsg>,
        ILiveStatusChangeListener<BilibiliBinaryFrameHandler, BilibiliLiveStatusChangeMsg>,
        IRoomStatsMsgListener<BilibiliBinaryFrameHandler, BilibiliRoomStatsMsg> {

    /**
     * 入场效果（高能用户）
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param messageMsg         SendSmsReplyMsg
     */
    default void onEntryEffect(BilibiliBinaryFrameHandler binaryFrameHandler, MessageMsg messageMsg) {
        this.onEntryEffect(messageMsg);
    }

    default void onEntryEffect(MessageMsg messageMsg) {
        // ignore
    }

    /**
     * 观看人数变化
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param msg                SendSmsReplyMsg
     * @deprecated use {@link IRoomStatsMsgListener#onRoomStatsMsg}
     */
    default void onWatchedChange(BilibiliBinaryFrameHandler binaryFrameHandler, MessageMsg msg) {
        this.onWatchedChange(msg);
    }

    /**
     * @deprecated use {@link IRoomStatsMsgListener#onRoomStatsMsg}
     */
    default void onWatchedChange(MessageMsg msg) {
        // ignore
    }

    /**
     * 点赞数更新
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param msg                SendSmsReplyMsg
     * @deprecated use {@link IRoomStatsMsgListener#onRoomStatsMsg}
     */
    default void onClickUpdate(BilibiliBinaryFrameHandler binaryFrameHandler, MessageMsg msg) {
        this.onClickUpdate(msg);
    }

    /**
     * @deprecated use {@link IRoomStatsMsgListener#onRoomStatsMsg}
     */
    default void onClickUpdate(MessageMsg msg) {
        // ignore
    }
}