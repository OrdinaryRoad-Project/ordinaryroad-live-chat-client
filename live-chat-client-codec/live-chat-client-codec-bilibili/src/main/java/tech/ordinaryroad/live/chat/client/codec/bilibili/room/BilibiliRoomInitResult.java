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

package tech.ordinaryroad.live.chat.client.codec.bilibili.room;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.*;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.BilibiliApis;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.dto.*;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.response.RoomPlayInfoResult;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliQualityEnum;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStreamQualityEnum;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;
import tech.ordinaryroad.live.chat.client.commons.base.room.RoomLiveStreamInfo;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BilibiliRoomInitResult implements IRoomInitResult {
    private String buvid3;
    private String uid;

    private BilibiliApis.RoomByIds roomByIds;
    private BilibiliApis.DanmuinfoResult danmuinfoResult = new BilibiliApis.DanmuinfoResult();
    private RoomPlayInfoResult roomPlayInfoResult = new RoomPlayInfoResult();

    /**
     * 直播间标题
     */
    @Override
    public String getRoomTitle() {
        return roomByIds.getTitle();
    }

    @Override
    public RoomLiveStatusEnum getRoomLiveStatus() {
        RoomLiveStatusEnum roomLiveStatus = null;
        switch (roomPlayInfoResult.getLive_status()) {
            case LIVING:
                roomLiveStatus = RoomLiveStatusEnum.LIVING;
                break;
            case STOPPED:
                roomLiveStatus = RoomLiveStatusEnum.STOPPED;
                break;
            default:
        }
        return roomLiveStatus;
    }

    @Override
    public List<IRoomLiveStreamInfo> getRoomLiveStreamUrls(RoomLiveStreamQualityEnum... qualities) {
        List<IRoomLiveStreamInfo> roomStreamInfoList = new ArrayList<>();
        Playurl_info playurlInfo = roomPlayInfoResult.getPlayurl_info();
        if (playurlInfo == null || playurlInfo.getPlayurl() == null || playurlInfo.getPlayurl().getStream() == null) {
            return roomStreamInfoList;
        }
        for (Stream stream : playurlInfo.getPlayurl().getStream()) {
            if (stream.getFormat() == null) {
                continue;
            }
            for (Format format : stream.getFormat()) {
                if (format.getCodec() == null) {
                    continue;
                }
                for (Codec codec : format.getCodec()) {
                    if (StrUtil.isEmpty(codec.getBase_url()) || codec.getUrl_info() == null) {
                        continue;
                    }

                    for (Url_info urlInfo : codec.getUrl_info()) {
                        String url = urlInfo.getHost() + codec.getBase_url() + urlInfo.getExtra();
                        RoomLiveStreamQualityEnum roomLiveStreamQualityEnum = BilibiliQualityEnum.toRoomLiveStreamQualityEnum(BilibiliQualityEnum.getByQn(codec.getCurrent_qn()));
                        IRoomLiveStreamInfo roomLiveStreamInfoByQuality = CollUtil.findOneByField(roomStreamInfoList, "quality", roomLiveStreamQualityEnum);
                        if (roomLiveStreamInfoByQuality == null) {
                            roomStreamInfoList.add(RoomLiveStreamInfo.builder()
                                    .quality(roomLiveStreamQualityEnum)
                                    .urls(CollUtil.newArrayList(url))
                                    .build());
                        } else {
                            roomLiveStreamInfoByQuality.getUrls().add(url);
                        }
                    }
                }
            }
        }
        RoomLiveStreamQualityEnum.filterQualities(roomStreamInfoList, qualities);
        return roomStreamInfoList;
    }
}