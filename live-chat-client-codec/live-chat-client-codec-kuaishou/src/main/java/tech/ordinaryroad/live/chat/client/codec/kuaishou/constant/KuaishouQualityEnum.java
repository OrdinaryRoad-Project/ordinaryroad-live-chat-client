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

package tech.ordinaryroad.live.chat.client.codec.kuaishou.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStreamQualityEnum;

/**
 * @author mjz
 * @date 2025/1/24
 */
@Getter
@RequiredArgsConstructor
public enum KuaishouQualityEnum {
    Q_STANDARD(1000, "高清"),
    Q_HIGH(2000, "超清"),
    Q_SUPER(4000, "蓝光 4M"),
    Q_BLUE_RAY(8000, "蓝光 质臻"),
    ;
    private final int bitrate;
    private final String name;

    public static KuaishouQualityEnum getByBitrate(int bitrate) {
        for (KuaishouQualityEnum value : values()) {
            if (value.getBitrate() == bitrate) {
                return value;
            }
        }
        return null;
    }

    public static RoomLiveStreamQualityEnum toRoomLiveStreamQualityEnum(KuaishouQualityEnum kuaishouQualityEnum) {
        RoomLiveStreamQualityEnum qualityEnum;
        if (kuaishouQualityEnum == null) {
            qualityEnum = RoomLiveStreamQualityEnum.Q_UNKNOWN;
        } else {
            switch (kuaishouQualityEnum) {
                case Q_STANDARD:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_FLUENT;
                    break;
                case Q_HIGH:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_HIGH;
                    break;
                case Q_SUPER:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_SUPER;
                    break;
                case Q_BLUE_RAY:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_BLUE_RAY;
                    break;
                default:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_UNKNOWN;
            }
        }
        return qualityEnum;
    }
}
