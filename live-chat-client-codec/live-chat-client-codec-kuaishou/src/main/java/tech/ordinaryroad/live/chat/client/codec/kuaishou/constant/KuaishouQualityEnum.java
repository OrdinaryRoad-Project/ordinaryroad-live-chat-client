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
public enum KuaishouQualityEnum {
    Q_STANDARD(1000, "STANDARD", "高清"),
    Q_HIGH(2000, "HIGH", "超清"),
    Q_SUPER(4000, "SUPER", "蓝光 4M"),
    Q_BLUE_RAY(8000, "BLUE_RAY", "蓝光 质臻"),
    ;
    private final int bitrate;
    private final String qualityType;
    private final String name;

    public static KuaishouQualityEnum getByBitrateOrQualityType(int bitrate, String qualityType) {
        for (KuaishouQualityEnum value : values()) {
            if (value.getBitrate() == bitrate || StrUtil.equalsIgnoreCase(value.getQualityType(), qualityType)) {
                return value;
            }
        }
        return null;
    }

    public static RoomLiveStreamQualityEnum toRoomLiveStreamQualityEnum(KuaishouQualityEnum kuaishouQualityEnum) {
        if (kuaishouQualityEnum == null) {
            return RoomLiveStreamQualityEnum.Q_UNKNOWN;
        } else {
            switch (kuaishouQualityEnum) {
                case Q_STANDARD:
                    return RoomLiveStreamQualityEnum.Q_FLUENT;
                case Q_HIGH:
                    return RoomLiveStreamQualityEnum.Q_HIGH;
                case Q_SUPER:
                    return RoomLiveStreamQualityEnum.Q_SUPER;
                case Q_BLUE_RAY:
                    return RoomLiveStreamQualityEnum.Q_BLUE_RAY;
                default:
                    return RoomLiveStreamQualityEnum.Q_UNKNOWN;
            }
        }
    }
}
