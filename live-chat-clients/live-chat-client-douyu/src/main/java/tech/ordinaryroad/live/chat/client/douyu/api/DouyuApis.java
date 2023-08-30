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

package tech.ordinaryroad.live.chat.client.douyu.api;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * API简易版
 *
 * @author mjz
 * @date 2023/5/5
 */
@Slf4j
public class DouyuApis {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static long getRealRoomId(long roomId, String cookie) {
        long realRoomId = roomId;
        @Cleanup
        HttpResponse execute = createGetRequest("https://www.douyu.com/" + roomId, cookie).execute();
        if (HttpStatus.isRedirected(execute.getStatus())) {
            String location = execute.header(Header.LOCATION);
            Map<String, String> paramMap = HttpUtil.decodeParamMap(location, null);
            if (!paramMap.containsKey("rid")) {
                throw new RuntimeException("{} 真实房间ID失败" + roomId);
            }
            realRoomId = NumberUtil.parseLong(paramMap.get("rid"));
        }
        return realRoomId;
    }

    public static long getRealRoomId(long roomId) {
        return getRealRoomId(roomId, null);
    }

    public static HttpRequest createGetRequest(String url, String cookie) {
        return HttpUtil.createGet(url)
                .cookie(cookie);
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

}
