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

package tech.ordinaryroad.live.chat.client.codec.huya.room;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ordinaryroad.live.chat.client.codec.huya.api.response.TtPlayerCfg;
import tech.ordinaryroad.live.chat.client.codec.huya.api.response.TtPlayerConf;
import tech.ordinaryroad.live.chat.client.codec.huya.api.response.TtProfileInfo;
import tech.ordinaryroad.live.chat.client.codec.huya.api.response.TtRoomData;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStreamQualityEnum;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;
import tech.ordinaryroad.live.chat.client.commons.base.room.RoomLiveStreamInfo;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatHttpUtil;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatUrlUtil;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HuyaRoomInitResult implements IRoomInitResult {

    private String lSubChannelId;
    private Long lChannelId;
    private Long lYyid;

    private TtRoomData ttRoomData;
    private TtProfileInfo ttProfileInfo;
    private TtPlayerCfg ttPlayerCfg;
    private TtPlayerConf ttPlayerConf;
    private JsonNode hyPlayerConfigStream;

    @Override
    public String getRoomTitle() {
        return ttRoomData.getIntroduction();
    }

    @Override
    public RoomLiveStatusEnum getRoomLiveStatus() {
        RoomLiveStatusEnum roomLiveStatus = null;
        switch (ttRoomData.getState()) {
            case "OFF":
                roomLiveStatus = RoomLiveStatusEnum.STOPPED;
                break;
            case "ON":
                roomLiveStatus = RoomLiveStatusEnum.LIVING;
                break;
            default:
        }
        return roomLiveStatus;
    }

    @Override
    public List<IRoomLiveStreamInfo> getRoomLiveStreamUrls(RoomLiveStreamQualityEnum ...qualities) {
        List<IRoomLiveStreamInfo> liveStreamInfos = new ArrayList<>();
        if (!hyPlayerConfigStream.has("data") || hyPlayerConfigStream.withArray("data").isEmpty()) {
            return liveStreamInfos;
        }
        for (JsonNode data : hyPlayerConfigStream.withArray("data")) {
            if (!data.has("gameStreamInfoList") || data.withArray("gameStreamInfoList").isEmpty()) {
                continue;
            }
            List<String> urls = new ArrayList<>();
            for (JsonNode jsonNode : data.withArray("gameStreamInfoList")) {
                urls.add(buildStreamUrl(jsonNode));
            }
            liveStreamInfos.add(RoomLiveStreamInfo.builder()
                    .quality(RoomLiveStreamQualityEnum.Q_UNKNOWN)
                    .urls(urls)
                    .build()
            );
        }
        return liveStreamInfos;
    }


    /**
     * 构建直播流URL
     *
     * @param stream stream信息的JSON对象
     * @return 完整的直播流URL
     */
    private static String buildStreamUrl(JsonNode stream) {
        String sFlvUrl = stream.get("sFlvUrl").asText();
        String sStreamName = stream.get("sStreamName").asText();
        String sFlvUrlSuffix = stream.get("sFlvUrlSuffix").asText();
        String sFlvAntiCode = stream.get("sFlvAntiCode").asText();

        String queryString;
        try {
            Map<String, String> urlQuery = OrLiveChatHttpUtil.decodeParamMap(sFlvAntiCode, StandardCharsets.UTF_8);

            int platformId = 100;
            long uid = RandomUtil.randomLong(12340000, 12349999);
            long convertUid = (uid << 8 | uid >>> (32 - 8)) & 0xFFFFFFFFL;

            String wsTime = urlQuery.get("wsTime");
            long seqId = uid + Instant.now().toEpochMilli();

            String fm = urlQuery.get("fm");
            String decodedFm = new String(Base64.decode(OrLiveChatUrlUtil.decode(fm)));
            String wsSecretPrefix = decodedFm.split("_")[0];

            String wsSecretHashInput = StrUtil.format("{}|{}|{}", seqId, urlQuery.get("ctype"), platformId);
            String wsSecretHash = DigestUtil.md5Hex(wsSecretHashInput);

            String wsSecretInput = StrUtil.format("{}_{}_{}_{}_{}", wsSecretPrefix, convertUid, sStreamName, wsSecretHash, wsTime);
            String wsSecret = DigestUtil.md5Hex(wsSecretInput);

            queryString = StrUtil.format("wsSecret={}&wsTime={}&seqid={}&ctype={}&ver=1&fs={}&u={}&t={}&sv=202412311405&sdk_sid={}&codec=264&a_block=0",
                    wsSecret, wsTime, seqId, urlQuery.get("ctype"), urlQuery.get("fs"), convertUid, platformId, Instant.now().toEpochMilli());
        } catch (Exception e) {
            throw new BaseException("Failed to generate query string", e);
        }
        return StrUtil.format("{}/{}.{}?{}", sFlvUrl, sStreamName, sFlvUrlSuffix, queryString);
    }
}