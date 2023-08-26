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

package tech.ordinaryroad.live.chat.client.bilibili.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * B站API简易版
 *
 * @author mjz
 * @date 2023/5/5
 */
@Slf4j
public class BilibiliApis {

    public static String cookies;
    public static Map<String, String> cookieMap;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonNode roomInit(long roomId) {
        @Cleanup
        HttpResponse response = createGetRequest("https://api.live.bilibili.com/room/v1/Room/room_init?id=" + roomId).execute();
        return responseInterceptor(response.body());
    }

    /**
     * @param roomId
     * @param type   直播间用0
     * @return <pre>{@code
     * {
     * 	"group": "live",
     * 	"business_id": 0,
     * 	"refresh_row_factor": 0.125,
     * 	"refresh_rate": 100,
     * 	"max_delay": 5000,
     * 	"token": "-wm5-Qo4BBAztd1qp5ZJpgyTMRBhCc7yikz5d9rAd63PV46G9BMwl0R10kMM8Ilb-UieZGjLtipPrz4Cvi0DdhGFwOi8PJpFN9K-LoXh6Z_4yjEIwgRerDiMIstHzJ80J3B7wnRisAYkWA==",
     * 	"host_list": [{
     * 		"host": "ali-bj-live-comet-09.chat.bilibili.com",
     * 		"port": 2243,
     * 		"wss_port": 443,
     * 		"ws_port": 2244
     *        }, {
     * 		"host": "ali-gz-live-comet-02.chat.bilibili.com",
     * 		"port": 2243,
     * 		"wss_port": 443,
     * 		"ws_port": 2244
     *    }, {
     * 		"host": "broadcastlv.chat.bilibili.com",
     * 		"port": 2243,
     * 		"wss_port": 443,
     * 		"ws_port": 2244
     *    }]
     * }
     * }</pre>
     */
    public static JsonNode getDanmuInfo(long roomId, int type) {
        @Cleanup
        HttpResponse response = createGetRequest("https://api.live.bilibili.com/xlive/web-room/v1/index/getDanmuInfo?id=" + roomId + "&type=" + type).execute();
        return responseInterceptor(response.body());
    }

    public static HttpRequest createGetRequest(String url) {
        return HttpUtil.createGet(url)
                .cookie(cookies);
    }

    private static JsonNode responseInterceptor(String responseString) {
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(responseString);
            int code = jsonNode.get("code").asInt();
            if (code == 0) {
                // 成功
                return jsonNode.get("data");
            } else {
                throw new RuntimeException(jsonNode.get("message").asText());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCookie(String name, Supplier<String> defaultValue) {
        if (cookieMap == null) {
            Map<String, String> map = new HashMap<>();
            if (StrUtil.isNotBlank(cookies) && !StrUtil.isNullOrUndefined(cookies)) {
                try {
                    String[] split = cookies.split("; ");
                    for (String s : split) {
                        String[] split1 = s.split("=");
                        map.put(split1[0], split1[1]);
                    }
                } catch (Exception e) {
                    log.error("cookie解析失败 " + cookies, e);
                }
            }
            cookieMap = map;
        }
        return cookieMap.getOrDefault(name, defaultValue != null ? defaultValue.get() : null);
    }

    public static String getCookie(String name) {
        return getCookie(name, null);
    }

}
