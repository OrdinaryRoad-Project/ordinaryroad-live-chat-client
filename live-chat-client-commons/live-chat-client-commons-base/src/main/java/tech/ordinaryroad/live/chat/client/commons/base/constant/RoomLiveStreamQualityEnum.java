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

package tech.ordinaryroad.live.chat.client.commons.base.constant;

import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;

import java.util.Iterator;
import java.util.List;

/**
 * 直播流清晰度
 *
 * @author mjz
 * @date 2025/1/17
 */
public enum RoomLiveStreamQualityEnum {
    Q_DOLBY,
    Q_4K,
    Q_4K_HDR,
    Q_ORIGIN,
    Q_ORIGIN_HDR,
    Q_BLUE_RAY,
    Q_BLUE_RAY_HDR,
    Q_SUPER,
    Q_HIGH,
    Q_FLUENT,
    Q_UNKNOWN,
    ;

    /**
     * 过滤直播流清晰度，直接在原列表上修改
     *
     * @param roomStreamInfoList 直播流信息列表
     * @param qualities          要保留的清晰度，为空则不过滤
     */
    public static void filterQualities(List<IRoomLiveStreamInfo> roomStreamInfoList, RoomLiveStreamQualityEnum... qualities) {
        if (qualities == null || qualities.length == 0) {
            return;
        }
        Iterator<IRoomLiveStreamInfo> iterator = roomStreamInfoList.iterator();
        while (iterator.hasNext()) {
            IRoomLiveStreamInfo roomLiveStreamInfo = iterator.next();
            boolean needRemove = false;
            for (RoomLiveStreamQualityEnum quality : qualities) {
                if (roomLiveStreamInfo.getQuality() == quality) {
                    needRemove = true;
                    break;
                }
            }
            if (needRemove) {
                iterator.remove();
            }
        }
    }
}
