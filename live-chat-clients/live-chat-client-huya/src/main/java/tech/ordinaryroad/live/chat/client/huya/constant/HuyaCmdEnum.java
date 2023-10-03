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

package tech.ordinaryroad.live.chat.client.huya.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author mjz
 * @date 2023/10/2
 */
@Getter
@RequiredArgsConstructor
public enum HuyaCmdEnum {
    NobleEnterNotice(1002),
    NobleSpeakBrst(1003),
    // NobleEnterNotice(1005),
    SendItemSubBroadcastPacket(6501),
    SendItemNoticeWordBroadcastPacket(6052),
    EnterPushInfo(6200),
    VipBarListRsp(6210),
    WeekRankListRsp(6220),
    WeekRankEnterBanner(6221),
    FansRankListRsp(6230),
    BadgeInfo(6231),
    BadgeScoreChanged(6232),
    FansInfoNotice(6233),
    UserGiftNotice(6234),
    GiftBarRsp(6250),
    MessageNotice(1400),
    AttendeeCountNotice(8006),
    ;
    private final long code;

    public static HuyaCmdEnum getByCode(long code) {
        for (HuyaCmdEnum value : HuyaCmdEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
