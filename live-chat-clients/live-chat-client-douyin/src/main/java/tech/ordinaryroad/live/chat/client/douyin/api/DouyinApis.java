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

package tech.ordinaryroad.live.chat.client.douyin.api;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;

import static tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg.OBJECT_MAPPER;

/**
 * @author mjz
 * @date 2024/1/3
 */
@Slf4j
public class DouyinApis {

    public static JsonNode roomInit(Object roomId) {
        @Cleanup
        HttpResponse response1 = HttpUtil.createGet("https://live.douyin.com/").execute();
        String ttwid = response1.getCookie("ttwid").getValue();
        String msToken = RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER_LOWER + "=_", 107);
        String __ac_nonce = RandomUtil.randomString(21);
        @Cleanup
        HttpResponse response2 = HttpUtil.createGet("https://live.douyin.com/" + roomId)
                .cookie("ttwid=" + ttwid + "; msToken=" + msToken + "; __ac_nonce=" + __ac_nonce)
                .execute();
        if (response2.getStatus() != HttpStatus.HTTP_OK) {
            throw new BaseException("获取" + roomId + "真实房间ID失败");
        }
        long realRoomId;
        String realRoomIdString = ReUtil.getGroup1("\\\\\"roomId\\\\\":\\\\\"(\\d+)\\\\\"", response2.body());
        try {
            realRoomId = NumberUtil.parseLong(realRoomIdString);
        } catch (Exception e) {
            throw new BaseException("获取" + roomId + "真实房间ID失败");
        }

        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
        objectNode.put("ttwid", ttwid);
        objectNode.put("msToken", msToken);
        objectNode.put("__ac_nonce", __ac_nonce);
        objectNode.put("realRoomId", realRoomId);
        objectNode.put("user_unique_id", RandomUtil.randomNumbers(19));
        return objectNode;
    }
}
