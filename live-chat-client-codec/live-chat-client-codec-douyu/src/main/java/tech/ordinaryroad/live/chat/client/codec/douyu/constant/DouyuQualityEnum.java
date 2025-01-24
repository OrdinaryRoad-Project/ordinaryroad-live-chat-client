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

package tech.ordinaryroad.live.chat.client.codec.douyu.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStreamQualityEnum;

/**
 * @author mjz
 * @date 2025/1/24
 */
@Getter
@RequiredArgsConstructor
public enum DouyuQualityEnum {
    Q_ORIGIN(0, -1, "原画"),
    Q_BLUE_RAY_8(8, 8000, "蓝光8M"),
    Q_BLUE_RAY_4(4, 4000, "蓝光4M"),
    Q_SD(3, 2000, "超清"),
    Q_HD(2, 900, "高清"),
    ;
    private final int rate;
    private final int bit;
    private final String name;

    public static DouyuQualityEnum getByRateOrBit(int rate, int bit) {
        for (DouyuQualityEnum value : DouyuQualityEnum.values()) {
            if (value.rate == rate || value.bit == bit) {
                return value;
            }
        }
        return null;
    }

    public static RoomLiveStreamQualityEnum toRoomLiveStreamQualityEnum(DouyuQualityEnum douyuQualityEnum) {
        if (douyuQualityEnum == null) {
            return RoomLiveStreamQualityEnum.Q_UNKNOWN;
        }
        switch (douyuQualityEnum) {
            case Q_ORIGIN:
                return RoomLiveStreamQualityEnum.Q_ORIGIN;
            case Q_BLUE_RAY_8:
                return RoomLiveStreamQualityEnum.Q_BLUE_RAY;
            case Q_BLUE_RAY_4:
                return RoomLiveStreamQualityEnum.Q_SUPER;
            case Q_SD:
                return RoomLiveStreamQualityEnum.Q_HIGH;
            case Q_HD:
                return RoomLiveStreamQualityEnum.Q_FLUENT;
            default:
                return RoomLiveStreamQualityEnum.Q_UNKNOWN;
        }
    }
}
