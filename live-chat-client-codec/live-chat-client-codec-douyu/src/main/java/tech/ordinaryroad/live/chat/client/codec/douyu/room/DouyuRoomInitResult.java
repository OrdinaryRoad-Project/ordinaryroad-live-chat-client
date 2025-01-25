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

package tech.ordinaryroad.live.chat.client.codec.douyu.room;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.DouyuApis;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.response.BetardResponse;
import tech.ordinaryroad.live.chat.client.codec.douyu.constant.DouyuQualityEnum;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStreamQualityEnum;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;
import tech.ordinaryroad.live.chat.client.commons.base.room.RoomLiveStreamInfo;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatHttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DouyuRoomInitResult implements IRoomInitResult {

    private BetardResponse betardResponse;

    private Long realRoomId;
    private long uid;
    private String did;

    private String cookie;
    private String streamUrlRequestVer;
    private String signJavaScriptsString;

    @Override
    public String getRoomTitle() {
        return betardResponse.getRoom().getRoom_name();
    }

    @Override
    public RoomLiveStatusEnum getRoomLiveStatus() {
        RoomLiveStatusEnum roomLiveStatus = null;
        switch (betardResponse.getRoom().getShow_status()) {
            case 1:
                roomLiveStatus = RoomLiveStatusEnum.LIVING;
                break;
            case 2:
                roomLiveStatus = RoomLiveStatusEnum.STOPPED;
                break;
            default:
        }
        return roomLiveStatus;
    }

    @Override
    public List<IRoomLiveStreamInfo> getRoomLiveStreamUrls(RoomLiveStreamQualityEnum... qualities) {
        List<IRoomLiveStreamInfo> roomLiveStreamInfos = new ArrayList<>();
        if (qualities.length == 0) {
            updateRoomLiveStreamInfosByQuality(RoomLiveStreamQualityEnum.Q_UNKNOWN, roomLiveStreamInfos);
        } else {
            for (RoomLiveStreamQualityEnum quality : qualities) {
                updateRoomLiveStreamInfosByQuality(quality, roomLiveStreamInfos);
                // 防止请求频率过高
                ThreadUtil.sleep(RandomUtil.randomInt(500, 1000));
            }
        }
        return roomLiveStreamInfos;
    }

    /**
     * 根据清晰度更新直播流地址
     *
     * @param quality             清晰度
     * @param roomLiveStreamInfos 待更新的直播流地址列表
     */
    private void updateRoomLiveStreamInfosByQuality(RoomLiveStreamQualityEnum quality, List<IRoomLiveStreamInfo> roomLiveStreamInfos) {
        DouyuQualityEnum douyuQualityEnum = DouyuQualityEnum.fromRoomLiveStreamQualityEnum(quality);

        Map<String, String> streamUrlRequestSignMap = DouyuApis.getStreamUrlRequestSignParams(signJavaScriptsString, realRoomId, did);
        streamUrlRequestSignMap.put("iar", "0");
        streamUrlRequestSignMap.put("ive", "0");
        streamUrlRequestSignMap.put("sov", "0");
        streamUrlRequestSignMap.put("hevc", "1");
        streamUrlRequestSignMap.put("aid", "web-alone");
        streamUrlRequestSignMap.put("ver", streamUrlRequestVer);
        streamUrlRequestSignMap.put("cdn", "");
        streamUrlRequestSignMap.put("rate", "-1");
        streamUrlRequestSignMap.put("uid", NumberUtil.toStr(uid));

        @Cleanup
        HttpResponse liveDataResponse = OrLiveChatHttpUtil.createGet("https://playweb.douyu.com/lapi/live/getH5Play/" + realRoomId)
                .cookie(cookie).formStr(streamUrlRequestSignMap).execute();

        try {
            JsonNode liveDataJsonNode = DouyuApis.responseInterceptor(liveDataResponse.body());

            RoomLiveStreamQualityEnum roomLiveStreamQualityEnum = DouyuQualityEnum.toRoomLiveStreamQualityEnum(douyuQualityEnum);
            String url = liveDataJsonNode.get("rtmp_url").asText() + "/" + liveDataJsonNode.get("rtmp_live").asText();

            IRoomLiveStreamInfo roomLiveStreamInfoByQuality = CollUtil.findOneByField(roomLiveStreamInfos, "quality", roomLiveStreamQualityEnum);
            if (roomLiveStreamInfoByQuality == null) {
                roomLiveStreamInfos.add(RoomLiveStreamInfo.builder()
                        .quality(roomLiveStreamQualityEnum)
                        .urls(CollUtil.newArrayList(url))
                        .build());
            } else {
                roomLiveStreamInfoByQuality.getUrls().add(url);
            }
        } catch (Exception e) {
            log.error("获取直播流地址失败", e);
        }
    }
}