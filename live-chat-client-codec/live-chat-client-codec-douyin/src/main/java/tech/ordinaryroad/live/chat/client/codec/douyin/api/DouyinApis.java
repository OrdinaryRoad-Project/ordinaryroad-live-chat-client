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

package tech.ordinaryroad.live.chat.client.codec.douyin.api;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinRoomStatusEnum;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.DouyinGiftMsg;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.DouyinWebcastGiftMessageMsgOuterClass;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatHttpUtil;

import java.util.Map;

/**
 * @author mjz
 * @date 2024/1/3
 */
@Slf4j
public class DouyinApis {

    public static final String KEY_COOKIE_TTWID = "ttwid";
    public static final String KEY_COOKIE_MS_TOKEN = "msToken";
    public static final String KEY_COOKIE_AC_NONCE = "__ac_nonce";
    public static final String MS_TOKEN_BASE_STRING = RandomUtil.BASE_CHAR_NUMBER_LOWER + "=_";
    public static final int MS_TOKEN_LENGTH = 107;
    public static final int AC_NONCE_LENGTH = 21;
    public static final String PATTERN_USER_UNIQUE_ID = "\\\\\"user_unique_id\\\\\":\\\\\"(\\d+)\\\\\"";
    public static final String PATTERN_ROOM_ID = "\\\\\"roomId\\\\\":\\\\\"(\\d+)\\\\\"";
    public static final String PATTERN_ROOM_STATUS = "\\\\\"roomInfo\\\\\".+\\\\\"status\\\\\":(\\d+)";

    public static RoomInitResult roomInit(Object roomId, String cookie) {
        Map<String, String> cookieMap = OrLiveChatCookieUtil.parseCookieString(cookie);

        @Cleanup
        HttpResponse response1 = OrLiveChatHttpUtil.createGet("https://live.douyin.com/").cookie(cookie).execute();
        String ttwid = OrLiveChatCookieUtil.getCookieByName(cookieMap, KEY_COOKIE_TTWID, () -> response1.getCookie(KEY_COOKIE_TTWID).getValue());
        String msToken = OrLiveChatCookieUtil.getCookieByName(cookieMap, KEY_COOKIE_MS_TOKEN, () -> RandomUtil.randomString(MS_TOKEN_BASE_STRING, MS_TOKEN_LENGTH));
        String __ac_nonce = OrLiveChatCookieUtil.getCookieByName(cookieMap, KEY_COOKIE_AC_NONCE, () -> RandomUtil.randomString(AC_NONCE_LENGTH));

        @Cleanup
        HttpResponse response2 = OrLiveChatHttpUtil.createGet("https://live.douyin.com/" + roomId)
                .cookie(StrUtil.emptyToDefault(cookie, KEY_COOKIE_TTWID + "=" + ttwid + "; " + KEY_COOKIE_MS_TOKEN + "=" + msToken + "; " + KEY_COOKIE_AC_NONCE + "=" + __ac_nonce))
                .execute();
        if (response2.getStatus() != HttpStatus.HTTP_OK) {
            throw new BaseException("获取" + roomId + "真实房间ID失败");
        }
        String body2 = response2.body();
        String user_unique_id = StrUtil.emptyToDefault(ReUtil.getGroup1(PATTERN_USER_UNIQUE_ID, body2), RandomUtil.randomNumbers(19));
        long realRoomId;
        String realRoomIdString = ReUtil.getGroup1(PATTERN_ROOM_ID, body2);
        try {
            realRoomId = NumberUtil.parseLong(realRoomIdString);
        } catch (Exception e) {
            throw new BaseException("获取" + roomId + "真实房间ID失败");
        }
        int roomStatus;
        String roomStatusString = ReUtil.getGroup1(PATTERN_ROOM_STATUS, body2);
        try {
            roomStatus = NumberUtil.parseInt(roomStatusString);
        } catch (Exception e) {
            throw new BaseException("获取" + roomId + "直播间状态失败");
        }

        return RoomInitResult.builder()
                .ttwid(ttwid)
                .msToken(msToken)
                .acNonce(__ac_nonce)
                .realRoomId(realRoomId)
                .userUniqueId(user_unique_id)
                .roomStatus(DouyinRoomStatusEnum.getByCode(roomStatus))
                .build();
    }

    public static RoomInitResult roomInit(Object roomId) {
        return roomInit(roomId, null);
    }

    /**
     * 计算抖音直播间收到礼物的个数
     *
     * @param msg DouyinGiftMsg
     * @return 礼物个数
     */
    public static int calculateGiftCount(DouyinGiftMsg msg) {
        if (msg == null || msg.getMsg() == null) {
            return 0;
        }

        long giftCount;
        DouyinWebcastGiftMessageMsgOuterClass.DouyinWebcastGiftMessageMsg douyinWebcastGiftMessageMsg = msg.getMsg();
        if (douyinWebcastGiftMessageMsg.getGift().getCombo() && douyinWebcastGiftMessageMsg.getRepeatEnd() != 1) {// 连击中
            return 0;
        }
        long comboCount = douyinWebcastGiftMessageMsg.getComboCount();
        if (douyinWebcastGiftMessageMsg.getGroupCount() != 1L) {// 每点击一次送礼的数量不是1时
            comboCount = douyinWebcastGiftMessageMsg.getGroupCount() * comboCount;
        }
        giftCount = comboCount;
        msg.setCalculatedGiftCount((int) giftCount);
        return (int) giftCount;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomInitResult {
        private String ttwid;
        private String msToken;
        private String acNonce;
        private long realRoomId;
        private String userUniqueId;
        private DouyinRoomStatusEnum roomStatus;
    }
}
