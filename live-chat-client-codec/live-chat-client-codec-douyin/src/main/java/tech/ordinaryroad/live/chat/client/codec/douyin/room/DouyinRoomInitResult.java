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

package tech.ordinaryroad.live.chat.client.codec.douyin.room;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinQualityEnum;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinRoomStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStreamQualityEnum;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;
import tech.ordinaryroad.live.chat.client.commons.base.room.RoomLiveStreamInfo;

import java.util.*;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DouyinRoomInitResult implements IRoomInitResult {
    private String ttwid;
    private String msToken;
    private String acNonce;
    private long realRoomId;
    private String userUniqueId;
    private DouyinRoomStatusEnum roomStatus;
    private JsonNode roomInfoJsonNode;

    @Override
    public String getRoomTitle() {
        String roomTitle = null;
        try {
            roomTitle = roomInfoJsonNode.get("room").get("title").asText();
        } catch (Exception e) {
            // ignored
        }
        return roomTitle;
    }

    @Override
    public RoomLiveStatusEnum getRoomLiveStatus() {
        RoomLiveStatusEnum roomLiveStatus = null;
        switch (roomStatus) {
            case STOPPED:
                roomLiveStatus = RoomLiveStatusEnum.STOPPED;
                break;
            case LIVING:
                roomLiveStatus = RoomLiveStatusEnum.LIVING;
                break;
            default:
        }
        return roomLiveStatus;
    }

    @Override
    public List<IRoomLiveStreamInfo> getRoomLiveStreamUrls(RoomLiveStreamQualityEnum... qualities) {
        List<IRoomLiveStreamInfo> roomLiveStreamInfos = new ArrayList<>();

        Map<RoomLiveStreamQualityEnum, List<String>> qualityUrlListMap = new HashMap<>();
        if (roomInfoJsonNode.has("room") && roomInfoJsonNode.get("room").has("stream_url")) {
            JsonNode streamUrlJsonNode = roomInfoJsonNode.get("room").get("stream_url");
            qualityUrlListMap = processStreamUrlNode(streamUrlJsonNode, qualityUrlListMap);
        }
        if (roomInfoJsonNode.has("web_stream_url")) {
            JsonNode webStreamUrlJsonNode = roomInfoJsonNode.get("web_stream_url");
            qualityUrlListMap = processStreamUrlNode(webStreamUrlJsonNode, qualityUrlListMap);
        }
        qualityUrlListMap.forEach((k, v) -> {
            roomLiveStreamInfos.add(RoomLiveStreamInfo.builder()
                    .quality(k)
                    .urls(v)
                    .build());
        });
        RoomLiveStreamQualityEnum.filterQualities(roomLiveStreamInfos, qualities);
        return roomLiveStreamInfos;
    }

    private static Map<RoomLiveStreamQualityEnum, List<String>> processStreamUrlNode(JsonNode streamUrlJsonNode) {
        return processStreamUrlNode(streamUrlJsonNode, null);
    }

    private static Map<RoomLiveStreamQualityEnum, List<String>> processStreamUrlNode(JsonNode streamUrlJsonNode, Map<RoomLiveStreamQualityEnum, List<String>> defaultRoomStreamUrlsMap) {
        if (streamUrlJsonNode.has("flv_pull_url")) {
            defaultRoomStreamUrlsMap = processPullUrls(streamUrlJsonNode.get("flv_pull_url"), defaultRoomStreamUrlsMap);
        }
        if (streamUrlJsonNode.has("hls_pull_url_map")) {
            defaultRoomStreamUrlsMap = processPullUrls(streamUrlJsonNode.get("hls_pull_url_map"), defaultRoomStreamUrlsMap);
        }
        return defaultRoomStreamUrlsMap;
    }

    private static Map<RoomLiveStreamQualityEnum, List<String>> processPullUrls(JsonNode flvPullUrlJsonNode, Map<RoomLiveStreamQualityEnum, List<String>> defaultRoomStreamUrlsMap) {
        Map<RoomLiveStreamQualityEnum, List<String>> roomStreamUrlsMap = Optional.ofNullable(defaultRoomStreamUrlsMap).orElseGet(HashMap::new);
        Map<DouyinQualityEnum, List<String>> map = processPullUrlMap(flvPullUrlJsonNode);
        map.forEach((k, v) -> {
            RoomLiveStreamQualityEnum roomLiveStreamQualityEnum = DouyinQualityEnum.toRoomLiveStreamQualityEnum(k);
            roomStreamUrlsMap.computeIfAbsent(roomLiveStreamQualityEnum, key -> new ArrayList<>()).addAll(v);
        });
        return roomStreamUrlsMap;
    }

    private static Map<RoomLiveStreamQualityEnum, List<String>> processPullUrls(JsonNode flvPullUrlJsonNode) {
        return processPullUrls(flvPullUrlJsonNode, null);
    }

    private static Map<DouyinQualityEnum, List<String>> processPullUrlMap(JsonNode pullUrlMap) {
        Map<DouyinQualityEnum, List<String>> map = new HashMap<>();
        for (Map.Entry<String, JsonNode> property : pullUrlMap.properties()) {
            String key = property.getKey();
            String value = property.getValue().asText();
            DouyinQualityEnum douyinQualityEnum = DouyinQualityEnum.getByPullUrlMapKey(key);
            if (douyinQualityEnum == null) {
                log.warn("未知的抖音直播流质量 {}", key);
                continue;
            }
            map.computeIfAbsent(douyinQualityEnum, k -> new ArrayList<>()).add(value);
        }
        return map;
    }
}