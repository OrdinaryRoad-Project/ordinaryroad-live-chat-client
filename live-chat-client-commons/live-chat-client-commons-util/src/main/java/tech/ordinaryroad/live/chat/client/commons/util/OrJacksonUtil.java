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

package tech.ordinaryroad.live.chat.client.commons.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author mjz
 * @date 2024/4/25
 */
public class OrJacksonUtil extends ObjectMapper {

    private static final OrJacksonUtil INSTANCE = new OrJacksonUtil() {{
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }};

    public static OrJacksonUtil getInstance() {
        return INSTANCE;
    }

    public static String getTextOrDefault(JsonNode jsonNode, String property, String defaultValue) {
        if (jsonNode.has(property)) {
            return jsonNode.get(property).asText(defaultValue);
        } else {
            return defaultValue;
        }
    }
//
//    /**
//     * 无法处理有转义的
//     */
//    public static String extractJson(String s, String key) {
//        int i = s.indexOf(key);
//        if (i == -1) {
//            return null; // 如果没有找到 '{'，则返回 null
//        }
//
//        int count = 1; // 当前嵌套深度，即还没闭合的 '{' 个数
//        int j = i + key.length(); // 从 '{' 的下一个字符开始遍历
//
//        // 遍历字符串以找到闭合的 '}'
//        while (j < s.length()) {
//            char c = s.charAt(j);
//            if (c == '}') {
//                count--;
//                if (count == 0) {
//                    break; // 找到匹配的 '}'，结束循环
//                }
//            } else if (c == '{') {
//                count++;
//            }
//            j++;
//        }
//
//        // 检查是否找到了匹配的 '}'
//        if (count != 0) {
//            return null; // 如果没有找到匹配的 '}'，则返回 null
//        }
//
//        // 返回提取的 JSON 字符串
//        return s.substring(i, j + 1);
//    }
//
//    public static void main(String[] args) {
//        // TODO 匹配
//        // 示例字符串，包含多个闭合的大括号
//        String input = "roomInfoRes\":{\"code\":0,\"message\":\"0\",\"ttl\":1,\"data\":{\"room_info\":{\"uid\":8739477,\"room_id\":545068,\"short_id\":7777,\"title\":\"德云色 先训练赛！在抨击排队！\\}\",\"cover\":\"https:\\u002F\\u002Fi0.hdslb.com\\u002Fbfs\\u002Flive\\u002Fnew_room_cover\\u002F5b194702d6c96774e049d5f7ab2d859ec5009755.jpg\",\"tags\":\"\",\"background\":\"\",\"description\":\"\",\"live_status\":1,\"live_start_time\":1733313550,\"live_screen_type\":0,\"lock_status\":0,\"lock_time\":0,\"hidden_status\":0,\"hidden_time\":0,\"area_id\":86,\"area_name\":\"英雄联盟\",\"parent_area_id\":2,\"parent_area_name\":\"网游\",\"keyframe\":\"https:\\u002F\\u002Fi0.hdslb.com\\u002Fbfs\\u002Flive-key-frame\\u002Fkeyframe12042042000000545068fn537i.jpg\",\"special_type\":0,\"up_session\":\"562088297082933548\",\"pk_status\":0,\"is_studio\":false,\"pendants\":{\"frame\":{\"name\":\"星界传奇\",\"value\":\"https:\\u002F\\u002Fi0.hdslb.com\\u002Fbfs\\u002Flive\\u002Fc982fb64215cb43289cd6027cd6e291e7d0cf224.png\",\"desc\":\"\"}},\"on_voice_join\":0,\"online\":479249,\"room_type\":{\"3-21\":0,\"3-50\":1},\"sub_session_key\":\"562088297082933548sub_time:1733313550\",\"live_id\":562088297082933570,\"live_id_str\":\"562088297082933548\",\"official_room_id\":0,\"official_room_info\":null,\"voice_background\":\"\"},\"anchor_info\":{\"";
//        // 提取 JSON 字符串
//        String jsonString = extractJson(input, "room_info\":{");
//
//        // 打印结果
//        if (jsonString != null) {
//            System.out.println("Extracted JSON: " + jsonString);
//        } else {
//            System.out.println("Failed to extract JSON from the input string.");
//        }
//    }
}
