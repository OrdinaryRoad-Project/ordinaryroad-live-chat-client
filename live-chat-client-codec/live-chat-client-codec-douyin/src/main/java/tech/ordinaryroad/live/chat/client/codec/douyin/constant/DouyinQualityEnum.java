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

package tech.ordinaryroad.live.chat.client.codec.douyin.constant;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStreamQualityEnum;

/**
 * @author mjz
 * @date 2025/1/24
 */
@Getter
@RequiredArgsConstructor
public enum DouyinQualityEnum {
    Q_LD(1000000, "ld", "标清"),
    Q_SD(1600000, "sd", "高清"),
    Q_HD(2000000, "hd", "超清"),
    Q_ORIGIN(2731008, "origin", "蓝光"),
    ;
    private final int bitrate;
    private final String sdkKey;
    private final String name;

    public static DouyinQualityEnum getByBitrateOrSdkKey(int bitrate, String sdkKey) {
        for (DouyinQualityEnum value : DouyinQualityEnum.values()) {
            if (value.bitrate == bitrate || StrUtil.equalsIgnoreCase(value.getSdkKey(), sdkKey)) {
                return value;
            }
        }
        return null;
    }

    public static RoomLiveStreamQualityEnum toRoomLiveStreamQualityEnum(DouyinQualityEnum douyinQualityEnum) {
        if (douyinQualityEnum == null) {
            return RoomLiveStreamQualityEnum.Q_UNKNOWN;
        }
        switch (douyinQualityEnum) {
            case Q_LD:
                return RoomLiveStreamQualityEnum.Q_FLUENT;
            case Q_SD:
                return RoomLiveStreamQualityEnum.Q_HIGH;
            case Q_HD:
                return RoomLiveStreamQualityEnum.Q_SUPER;
            case Q_ORIGIN:
                return RoomLiveStreamQualityEnum.Q_BLUE_RAY;
            default:
                return RoomLiveStreamQualityEnum.Q_UNKNOWN;
        }
    }
}
