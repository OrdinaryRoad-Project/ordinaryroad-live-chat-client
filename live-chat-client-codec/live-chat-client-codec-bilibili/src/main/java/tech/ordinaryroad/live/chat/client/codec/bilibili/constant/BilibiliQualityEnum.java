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

package tech.ordinaryroad.live.chat.client.codec.bilibili.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStreamQualityEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mjz
 * @date 2025/1/17
 */
@Getter
@RequiredArgsConstructor
public enum BilibiliQualityEnum {
    QN_DOLBY(30000, "杜比", 0),
    QN_4K(20000, "4K", 0),
    QN_4K_HDR(20000, "4K/HDR", 1),
    QN_ORIGIN(10000, "原画", 0),
    QN_ORIGIN_HDR(10000, "原画/HDR", 1),
    QN_BLUE_RAY(400, "蓝光", 0),
    QN_BLUE_RAY_HDR(400, "蓝光/HDR", 1),
    QN_SUPER(250, "超清", 0),
    QN_HIGH(150, "高清", 0),
    QN_FLUENT(80, "流畅", 0),
    ;

    private final int qn;
    private final String desc;
    private final int hdrType;

    private static final Map<BilibiliQualityEnum, RoomLiveStreamQualityEnum> QUALITY_MAP_FROM_BILIBILI = new HashMap<BilibiliQualityEnum, RoomLiveStreamQualityEnum>() {{
        put(BilibiliQualityEnum.QN_DOLBY, RoomLiveStreamQualityEnum.Q_DOLBY);
        put(BilibiliQualityEnum.QN_4K, RoomLiveStreamQualityEnum.Q_4K);
        put(BilibiliQualityEnum.QN_4K_HDR, RoomLiveStreamQualityEnum.Q_4K_HDR);
        put(BilibiliQualityEnum.QN_ORIGIN, RoomLiveStreamQualityEnum.Q_ORIGIN);
        put(BilibiliQualityEnum.QN_ORIGIN_HDR, RoomLiveStreamQualityEnum.Q_ORIGIN_HDR);
        put(BilibiliQualityEnum.QN_BLUE_RAY, RoomLiveStreamQualityEnum.Q_BLUE_RAY);
        put(BilibiliQualityEnum.QN_BLUE_RAY_HDR, RoomLiveStreamQualityEnum.Q_BLUE_RAY_HDR);
        put(BilibiliQualityEnum.QN_SUPER, RoomLiveStreamQualityEnum.Q_SUPER);
        put(BilibiliQualityEnum.QN_HIGH, RoomLiveStreamQualityEnum.Q_HIGH);
        put(BilibiliQualityEnum.QN_FLUENT, RoomLiveStreamQualityEnum.Q_FLUENT);
    }};

    private static final Map<RoomLiveStreamQualityEnum, BilibiliQualityEnum> QUALITY_MAP_TO_BILIBILI = new HashMap<RoomLiveStreamQualityEnum, BilibiliQualityEnum>() {{
        put(RoomLiveStreamQualityEnum.Q_DOLBY, BilibiliQualityEnum.QN_DOLBY);
        put(RoomLiveStreamQualityEnum.Q_4K, BilibiliQualityEnum.QN_4K);
        put(RoomLiveStreamQualityEnum.Q_4K_HDR, BilibiliQualityEnum.QN_4K_HDR);
        put(RoomLiveStreamQualityEnum.Q_ORIGIN, BilibiliQualityEnum.QN_ORIGIN);
        put(RoomLiveStreamQualityEnum.Q_ORIGIN_HDR, BilibiliQualityEnum.QN_ORIGIN_HDR);
        put(RoomLiveStreamQualityEnum.Q_BLUE_RAY, BilibiliQualityEnum.QN_BLUE_RAY);
        put(RoomLiveStreamQualityEnum.Q_BLUE_RAY_HDR, BilibiliQualityEnum.QN_BLUE_RAY_HDR);
        put(RoomLiveStreamQualityEnum.Q_SUPER, BilibiliQualityEnum.QN_SUPER);
        put(RoomLiveStreamQualityEnum.Q_HIGH, BilibiliQualityEnum.QN_HIGH);
        put(RoomLiveStreamQualityEnum.Q_FLUENT, BilibiliQualityEnum.QN_FLUENT);
    }};

    public static BilibiliQualityEnum getByQn(int qn) {
        for (BilibiliQualityEnum value : values()) {
            if (value.qn == qn) {
                return value;
            }
        }
        return null;
    }

    public static RoomLiveStreamQualityEnum toRoomLiveStreamQualityEnum(BilibiliQualityEnum bilibiliQualityEnum) {
        return QUALITY_MAP_FROM_BILIBILI.getOrDefault(bilibiliQualityEnum, RoomLiveStreamQualityEnum.Q_UNKNOWN);
    }

    public static BilibiliQualityEnum fromRoomLiveStreamQualityEnum(RoomLiveStreamQualityEnum qualityEnum) {
        return QUALITY_MAP_TO_BILIBILI.getOrDefault(qualityEnum, BilibiliQualityEnum.QN_ORIGIN);
    }
}
