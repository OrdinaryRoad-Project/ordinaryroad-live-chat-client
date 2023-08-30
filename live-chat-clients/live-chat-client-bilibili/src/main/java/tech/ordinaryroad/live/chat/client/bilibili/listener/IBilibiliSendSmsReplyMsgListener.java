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
import tech.ordinaryroad.live.chat.client.bilibili.msg.SendSmsReplyMsg;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseMsgListener;

/**
 * @author mjz
 * @date 2023/1/7
 */
public interface IBilibiliSendSmsReplyMsgListener extends IBaseMsgListener<BilibiliBinaryFrameHandler, BilibiliCmdEnum> {

    /**
     * 收到弹幕
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param msg                SendSmsReplyMsg
     */
    default void onDanmuMsg(BilibiliBinaryFrameHandler binaryFrameHandler, SendSmsReplyMsg msg) {
        this.onDanmuMsg(msg);
    }

    default void onDanmuMsg(SendSmsReplyMsg msg) {
        // ignore
    }

    /**
     * 收到礼物
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param msg                SendSmsReplyMsg
     */
    default void onSendGift(BilibiliBinaryFrameHandler binaryFrameHandler, SendSmsReplyMsg msg) {
        this.onSendGift(msg);
    }

    default void onSendGift(SendSmsReplyMsg msg) {
        // ignore
    }

    /**
     * 普通用户进入直播间
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param msg                SendSmsReplyMsg
     */
    default void onEnterRoom(BilibiliBinaryFrameHandler binaryFrameHandler, SendSmsReplyMsg msg) {
        this.onEnterRoom(msg);
    }

    default void onEnterRoom(SendSmsReplyMsg msg) {
        // ignore
    }

    /**
     * 入场效果（高能用户）
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param sendSmsReplyMsg    SendSmsReplyMsg
     */
    default void onEntryEffect(BilibiliBinaryFrameHandler binaryFrameHandler, SendSmsReplyMsg sendSmsReplyMsg) {
        this.onEntryEffect(sendSmsReplyMsg);
    }

    default void onEntryEffect(SendSmsReplyMsg sendSmsReplyMsg) {
        // ignore
    }

    /**
     * 观看人数变化
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param msg                SendSmsReplyMsg
     */
    default void onWatchedChange(BilibiliBinaryFrameHandler binaryFrameHandler, SendSmsReplyMsg msg) {
        this.onWatchedChange(msg);
    }

    default void onWatchedChange(SendSmsReplyMsg msg) {
        // ignore
    }

    /**
     * 为主播点赞
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param msg                SendSmsReplyMsg
     */
    default void onClickLike(BilibiliBinaryFrameHandler binaryFrameHandler, SendSmsReplyMsg msg) {
        this.onClickLike(msg);
    }

    default void onClickLike(SendSmsReplyMsg msg) {
        // ignore
    }

    /**
     * 点赞数更新
     *
     * @param binaryFrameHandler BilibiliBinaryFrameHandler
     * @param msg                SendSmsReplyMsg
     */
    default void onClickUpdate(BilibiliBinaryFrameHandler binaryFrameHandler, SendSmsReplyMsg msg) {
        this.onClickUpdate(msg);
    }

    default void onClickUpdate(SendSmsReplyMsg msg) {
        // ignore
    }
}