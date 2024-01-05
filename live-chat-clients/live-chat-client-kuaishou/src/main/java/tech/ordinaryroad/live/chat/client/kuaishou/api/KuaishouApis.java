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

package tech.ordinaryroad.live.chat.client.kuaishou.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.*;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;

import java.util.ArrayList;
import java.util.List;

import static tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg.OBJECT_MAPPER;

/**
 * @author mjz
 * @date 2024/1/5
 */
public class KuaishouApis {

    public static final String PATTERN_LIVE_STREAM_ID = "\"liveStream\":\\{\"id\":\"([\\w\\d-]+)\"";

    public static RoomInitResult roomInit(Object roomId, String cookie) {
        @Cleanup
        HttpResponse response = HttpUtil.createGet("https://live.kuaishou.com/u/" + roomId)
                .cookie(cookie)
                .execute();
        String body = response.body();
        String liveStreamId = ReUtil.getGroup1(PATTERN_LIVE_STREAM_ID, body);
        JsonNode websocketinfo = websocketinfo(liveStreamId, cookie);
        if (!websocketinfo.has("token")) {
            throw new BaseException("主播未开播，token获取失败 " + websocketinfo);
        }
        ArrayNode websocketUrls = websocketinfo.withArrayProperty("websocketUrls");
        ArrayList<String> websocketUrlList = CollUtil.newArrayList();
        for (JsonNode websocketUrl : websocketUrls) {
            websocketUrlList.add(websocketUrl.asText());
        }
        return RoomInitResult.builder()
                .token(websocketinfo.required("token").asText())
                .websocketUrls(websocketUrlList)
                .liveStreamId(liveStreamId)
                .build();
    }

    public static RoomInitResult roomInit(Object roomId) {
        return roomInit(roomId, null);
    }

    public static JsonNode websocketinfo(String liveStreamId, String cookie) {
        @Cleanup
        HttpResponse response = HttpUtil.createGet("https://live.kuaishou.com/live_api/liveroom/websocketinfo?liveStreamId=" + liveStreamId)
                .cookie(cookie)
                .execute();
        return responseInterceptor(response.body());
    }

    private static JsonNode responseInterceptor(String responseString) {
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(responseString);
            JsonNode data = jsonNode.required("data");
            if (data.required("result").asInt() != 1) {
                throw new BaseException("接口访问失败，返回结果：" + jsonNode);
            }
            return data;
        } catch (JsonProcessingException e) {
            throw new BaseException(e);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomInitResult {
        private String token;
        private String liveStreamId;
        private List<String> websocketUrls;
    }
}
