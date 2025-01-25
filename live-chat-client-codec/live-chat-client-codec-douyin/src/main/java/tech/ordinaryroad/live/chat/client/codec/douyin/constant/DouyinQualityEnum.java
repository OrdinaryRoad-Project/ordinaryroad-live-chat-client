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

import java.util.HashMap;
import java.util.Map;

/**
 * @author mjz
 * @date 2025/1/24
 */
@Getter
@RequiredArgsConstructor
public enum DouyinQualityEnum {
    Q_LD(1000000, "ld", "标清", "SD1"),
    Q_SD(1600000, "sd", "高清", "SD2"),
    Q_HD(2000000, "hd", "超清", "HD1"),
    Q_ORIGIN(2731008, "origin", "蓝光", "FULL_HD1"),
    ;
    private final int bitrate;
    private final String sdkKey;
    private final String name;
    private final String pullUrlMapKey;

    private static final Map<DouyinQualityEnum, RoomLiveStreamQualityEnum> QUALITY_MAP_FROM_DOUYIN = new HashMap<DouyinQualityEnum, RoomLiveStreamQualityEnum>() {{
        put(DouyinQualityEnum.Q_LD, RoomLiveStreamQualityEnum.Q_FLUENT);
        put(DouyinQualityEnum.Q_SD, RoomLiveStreamQualityEnum.Q_HIGH);
        put(DouyinQualityEnum.Q_HD, RoomLiveStreamQualityEnum.Q_SUPER);
        put(DouyinQualityEnum.Q_ORIGIN, RoomLiveStreamQualityEnum.Q_BLUE_RAY);
    }};

    private static final Map<RoomLiveStreamQualityEnum, DouyinQualityEnum> QUALITY_MAP_TO_DOUYIN = new HashMap<RoomLiveStreamQualityEnum, DouyinQualityEnum>() {{
        put(RoomLiveStreamQualityEnum.Q_FLUENT, DouyinQualityEnum.Q_LD);
        put(RoomLiveStreamQualityEnum.Q_HIGH, DouyinQualityEnum.Q_SD);
        put(RoomLiveStreamQualityEnum.Q_SUPER, DouyinQualityEnum.Q_HD);
        put(RoomLiveStreamQualityEnum.Q_BLUE_RAY, DouyinQualityEnum.Q_ORIGIN);
    }};

    public static DouyinQualityEnum getByBitrateOrSdkKey(int bitrate, String sdkKey) {
        for (DouyinQualityEnum value : DouyinQualityEnum.values()) {
            if (value.bitrate == bitrate || StrUtil.equalsIgnoreCase(value.getSdkKey(), sdkKey)) {
                return value;
            }
        }
        return null;
    }

    public static DouyinQualityEnum getByPullUrlMapKey(String pullUrlMapKey) {
        for (DouyinQualityEnum value : DouyinQualityEnum.values()) {
            if (StrUtil.equalsIgnoreCase(value.getPullUrlMapKey(), pullUrlMapKey)) {
                return value;
            }
        }
        return null;
    }

    public static RoomLiveStreamQualityEnum toRoomLiveStreamQualityEnum(DouyinQualityEnum douyinQualityEnum) {
        return QUALITY_MAP_FROM_DOUYIN.getOrDefault(douyinQualityEnum, RoomLiveStreamQualityEnum.Q_UNKNOWN);
    }
}
