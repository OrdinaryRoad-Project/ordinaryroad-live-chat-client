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

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.bilibili.api.request.BilibiliSendMsgRequest;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * B站API简易版
 *
 * @author mjz
 * @date 2023/5/5
 */
@Slf4j
public class BilibiliApis {

    public static final TimedCache<Long, String> giftImgCache = new TimedCache<>(TimeUnit.DAYS.toMillis(1));
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String KEY_COOKIE_CSRF = "bili_jct";

    public static JsonNode roomInit(long roomId, String cookie) {
        @Cleanup
        HttpResponse response = createGetRequest("https://api.live.bilibili.com/room/v1/Room/room_init?id=" + roomId, cookie).execute();
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
    public static JsonNode getDanmuInfo(long roomId, int type, String cookie) {
        @Cleanup
        HttpResponse response = createGetRequest("https://api.live.bilibili.com/xlive/web-room/v1/index/getDanmuInfo?id=" + roomId + "&type=" + type, cookie).execute();
        return responseInterceptor(response.body());
    }

    /**
     * 从网页html中获得映射关系
     */
    public static String getGiftImgById(long giftId) {
        if (giftImgCache.containsKey(giftId)) {
            return giftImgCache.get(giftId);
        }

        String img = null;

        boolean find = false;
        for (String line : FileUtil.readLines(ResourceUtil.getResource("css/gift_background.css"), StandardCharsets.UTF_8)) {
            if (find) {
                img = line.substring(26, 101);
                giftImgCache.put(giftId, img);
                break;
            }
            if (line.startsWith(".gift-" + giftId + "-")) {
                find = true;
            }
        }

        return img;
    }

    public static void sendMsg(BilibiliSendMsgRequest request, String cookie) {
        if (StrUtil.isBlank(cookie)) {
            throw new BaseException("发送弹幕接口cookie不能为空");
        }
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(request);
        @Cleanup HttpResponse execute = HttpUtil.createPost("https://api.live.bilibili.com/msg/send")
                .cookie(cookie)
                .form(stringObjectMap)
                .execute();
        responseInterceptor(execute.body());
    }

    public static void sendMsg(String msg, long roomId, String cookie) {
        JsonNode data = BilibiliApis.roomInit(roomId, cookie);
        String realRoomId = data.get("room_id").asText();
        String biliJct = OrLiveChatCookieUtil.getCookieByName(cookie, KEY_COOKIE_CSRF, () -> {
            throw new BaseException("cookie中缺少参数" + KEY_COOKIE_CSRF);
        });
        BilibiliSendMsgRequest request = new BilibiliSendMsgRequest(msg, StrUtil.toString(ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).toEpochSecond()), realRoomId, biliJct, biliJct);
        sendMsg(request, cookie);
    }

    public static HttpRequest createGetRequest(String url, String cookies) {
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
                throw new BaseException(jsonNode.get("message").asText());
            }
        } catch (JsonProcessingException e) {
            throw new BaseException(e);
        }
    }

}
