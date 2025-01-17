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

/**
 * @author mjz
 * @date 2025/1/17
 */
@Getter
@RequiredArgsConstructor
public enum BilibiliQnEnum {
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

    public static BilibiliQnEnum getByQn(int qn) {
        for (BilibiliQnEnum value : values()) {
            if (value.qn == qn) {
                return value;
            }
        }
        return null;
    }

    public static RoomLiveStreamQualityEnum toRoomLiveStreamQualityEnum(BilibiliQnEnum qnEnum) {
        RoomLiveStreamQualityEnum qualityEnum;
        if (qnEnum == null) {
            qualityEnum = RoomLiveStreamQualityEnum.Q_UNKNOWN;
        } else {
            switch (qnEnum) {
                case QN_DOLBY:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_DOLBY;
                    break;
                case QN_4K:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_4K;
                    break;
                case QN_4K_HDR:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_4K_HDR;
                    break;
                case QN_ORIGIN:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_ORIGIN;
                    break;
                case QN_ORIGIN_HDR:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_ORIGIN_HDR;
                    break;
                case QN_BLUE_RAY:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_BLUE_RAY;
                    break;
                case QN_BLUE_RAY_HDR:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_BLUE_RAY_HDR;
                    break;
                case QN_SUPER:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_SUPER;
                    break;
                case QN_HIGH:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_HIGH;
                    break;
                case QN_FLUENT:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_FLUENT;
                    break;
                default:
                    qualityEnum = RoomLiveStreamQualityEnum.Q_UNKNOWN;
            }
        }
        return qualityEnum;
    }
}
