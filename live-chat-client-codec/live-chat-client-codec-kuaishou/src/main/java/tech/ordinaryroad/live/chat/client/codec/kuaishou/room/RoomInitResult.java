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

package tech.ordinaryroad.live.chat.client.codec.kuaishou.room;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.constant.KuaishouQualityEnum;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStreamQualityEnum;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;
import tech.ordinaryroad.live.chat.client.commons.base.room.RoomLiveStreamInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomInitResult implements IRoomInitResult {
    private String token;
    private String liveStreamId;
    private List<String> websocketUrls;
    private JsonNode livedetailJsonNode;

    @Override
    public String getRoomTitle() {
        return livedetailJsonNode.get("author").get("name").asText();
    }

    @Override
    public String getRoomDescription() {
        return livedetailJsonNode.get("author").get("description").asText();
    }

    @Override
    public RoomLiveStatusEnum getRoomLiveStatus() {
        if (livedetailJsonNode.has("liveStream") && livedetailJsonNode.get("liveStream").has("id")) {
            return RoomLiveStatusEnum.LIVING;
        } else {
            return RoomLiveStatusEnum.STOPPED;
        }
    }

    @Override
    public List<IRoomLiveStreamInfo> getRoomLiveStreamUrls() {
        List<IRoomLiveStreamInfo> roomLiveStreamInfos = CollUtil.newArrayList();

        if (!livedetailJsonNode.has("liveStream") || !livedetailJsonNode.get("liveStream").has("playUrls")) {
            return roomLiveStreamInfos;
        }

        JsonNode playUrlsJsonNode = livedetailJsonNode.get("liveStream").get("playUrls");

        Map<KuaishouQualityEnum, List<JsonNode>> map = new HashMap<>();
        for (JsonNode playUrl : playUrlsJsonNode) {
            if (!playUrl.has("adaptationSet") || !playUrl.get("adaptationSet").has("representation")) {
                continue;
            }

            JsonNode representationArray = playUrl.get("adaptationSet").withArray("representation");
            for (JsonNode representation : representationArray) {
                if (!representation.has("bitrate")) {
                    continue;
                }

                int bitrate = representation.get("bitrate").asInt();
                String qualityType = representation.get("qualityType").asText();
                KuaishouQualityEnum kuaishouQualityEnum = KuaishouQualityEnum.getByBitrateOrQualityType(bitrate, qualityType);
                map.computeIfAbsent(kuaishouQualityEnum, k -> CollUtil.newArrayList()).add(representation);
            }
        }

        map.forEach(((kuaishouQualityEnum, representations) -> {
            RoomLiveStreamQualityEnum roomLiveStreamQualityEnum = KuaishouQualityEnum.toRoomLiveStreamQualityEnum(kuaishouQualityEnum);
            ArrayList<String> urls = CollUtil.newArrayList();
            for (JsonNode representation : representations) {
                if (!representation.has("url")) {
                    continue;
                }

                urls.add(representation.get("url").asText());
            }
            roomLiveStreamInfos.add(RoomLiveStreamInfo.builder().quality(roomLiveStreamQualityEnum).urls(urls).build());
        }));

        return roomLiveStreamInfos;
    }
}