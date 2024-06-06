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

package tech.ordinaryroad.live.chat.client.codec.bilibili.api;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.request.BilibiliLikeReportV3Request;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.request.BilibiliSendMsgRequest;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.util.OrJacksonUtil;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatHttpUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
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

    public static final TimedCache<Long, String> GIFT_IMG_CACHE = new TimedCache<>(TimeUnit.DAYS.toMillis(1));
    public static final String KEY_COOKIE_CSRF = "bili_jct";
    public static final String KEY_UID = "DedeUserID";
    public static final String KEY_BUVID3 = "buvid3";
    public static final String PATTERN_REAL_ROOM_ID = "\\\"roomInitRes\\\".+\\{\\\"room_id\\\":(\\d+)";

    private static final String API_FRONTEND_FINGER_SPI = "https://api.bilibili.com/x/frontend/finger/spi";
    private static final String API_V = "https://data.bilibili.com/v/";
    private static final String API_WEB_INTERFACE_NAV = "https://api.bilibili.com/x/web-interface/nav";
    private static final String API_DANMU_INFO = "https://api.live.bilibili.com/xlive/web-room/v1/index/getDanmuInfo";
    private static final String API_ROOM_PLAY_INFO = "https://api.live.bilibili.com/xlive/web-room/v2/index/getRoomPlayInfo";

    @SneakyThrows
    public static RoomInitResult roomInit(long roomId, String cookie) {
        RoomPlayInfoResult roomPlayInfo = getRoomPlayInfo(roomId, cookie);
        long realRoomId = roomPlayInfo.room_id;

        String b_3;
        String uid;
        if (StrUtil.isNotBlank(cookie)) {
            uid = OrLiveChatCookieUtil.getCookieByName(cookie, KEY_UID, () -> {
                throw new BaseException("cookie中缺少参数" + KEY_UID);
            });
            b_3 = OrLiveChatCookieUtil.getCookieByName(cookie, KEY_BUVID3, () -> {
                throw new BaseException("cookie中缺少参数" + KEY_BUVID3);
            });
            // webInterfaceNav(cookie);
        } else {
            uid = "0";

            JsonNode jsonNode = frontendFingerSpi();
            b_3 = jsonNode.get("b_3").asText();
            String b4 = jsonNode.get("b_4").asText();
            cookie = KEY_BUVID3 + "=" + b_3
                    + "; buvid4=" + b4;
        }

        DanmuinfoResult danmuInfo = getDanmuInfo(realRoomId, cookie);
        return new RoomInitResult.RoomInitResultBuilder()
                .buvid3(b_3)
                .uid(uid)
                .danmuinfoResult(danmuInfo)
                .roomPlayInfoResult(roomPlayInfo)
                .build();
    }

    public static JsonNode roomGiftConfig(long roomId, String cookie) {
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet("https://api.live.bilibili.com/xlive/web-room/v1/giftPanel/roomGiftConfig?platform=pc&source=live&build=0&global_version=0&room_id=" + roomId)
                .cookie(cookie)
                .execute();
        return responseInterceptor(response.body());
    }

    /**
     * {
     * "code": 0,
     * "data": {
     * "b_3": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxBAC9C3C049561infoc",
     * "b_4": "xxxxxxx-xxxx-xxxx-xxxx-xxxx0EE06EC549561-xxxx32009-xxxxgQAMrcCGKkaytpzZwg=="
     * },
     * "message": "ok"
     * }
     */
    @SneakyThrows
    public static JsonNode frontendFingerSpi() {
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet(API_FRONTEND_FINGER_SPI).execute();
        return responseInterceptor(response.body());
    }

    /**
     * 返回buvid3
     */
    @SneakyThrows
    public static String v() {
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet(API_V).execute();
        return response.getCookieValue("buvid3");
    }

    @SneakyThrows
    public static void webInterfaceNav(String cookie) {
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet(API_WEB_INTERFACE_NAV)
                .cookie(cookie)
                .execute();
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
    @SneakyThrows
    public static DanmuinfoResult getDanmuInfo(long roomId, int type, String cookie) {
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet(API_DANMU_INFO + "?id=" + roomId + "&type=" + type)
                .cookie(cookie)
                .execute();
        return OrJacksonUtil.getInstance().readValue(responseInterceptor(response.body()).toString(), DanmuinfoResult.class);
    }

    @SneakyThrows
    public static DanmuinfoResult getDanmuInfo(long roomId, String cookie) {
        return getDanmuInfo(roomId, 0, cookie);
    }

    @SneakyThrows
    public static RoomPlayInfoResult getRoomPlayInfo(long roomId, int no_playurl, String cookie) {
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet(API_ROOM_PLAY_INFO + "?room_id=" + roomId + "&no_playurl=" + no_playurl + "&mask=1&qn=0&platform=web&protocol=0,1&format=0,1,2&codec=0,1,2&dolby=5&panorama=1")
                .cookie(cookie)
                .execute();
        return OrJacksonUtil.getInstance().readValue(responseInterceptor(response.body()).toString(), RoomPlayInfoResult.class);
    }

    @SneakyThrows
    public static RoomPlayInfoResult getRoomPlayInfo(long roomId, String cookie) {
        return getRoomPlayInfo(roomId, 1, cookie);
    }

    public static String getGiftImgById(long giftId, long roomId) {
        if (!GIFT_IMG_CACHE.containsKey(giftId)) {
            ThreadUtil.execAsync(() -> {
                updateGiftImgCache(roomId, null);
            });
        }

        return GIFT_IMG_CACHE.get(giftId);
    }

    /**
     * 更新礼物图片缓存
     */
    public static void updateGiftImgCache(long roomId, String cookie) {
        JsonNode jsonNode = roomGiftConfig(roomId, cookie);
        for (JsonNode node : jsonNode.get("global_gift").get("list")) {
            long giftId = node.get("id").asLong();
            String giftImgUrl = node.get("webp").asText();
            GIFT_IMG_CACHE.put(giftId, giftImgUrl);
        }
    }

    /**
     * 发送弹幕
     *
     * @param request {@link BilibiliSendMsgRequest}
     * @param cookie  Cookie
     */
    public static void sendMsg(BilibiliSendMsgRequest request, String cookie) {
        if (StrUtil.isBlank(cookie)) {
            throw new BaseException("发送弹幕接口cookie不能为空");
        }
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(request);
        @Cleanup HttpResponse execute = OrLiveChatHttpUtil.createPost("https://api.live.bilibili.com/msg/send")
                .cookie(cookie)
                .form(stringObjectMap)
                .execute();
        responseInterceptor(execute.body());
    }

    /**
     * 发送弹幕
     *
     * @param msg        内容
     * @param realRoomId 真实房间id
     * @param cookie     Cookie
     */
    public static void sendMsg(String msg, long realRoomId, String cookie) {
        String biliJct = OrLiveChatCookieUtil.getCookieByName(cookie, KEY_COOKIE_CSRF, () -> {
            throw new BaseException("cookie中缺少参数" + KEY_COOKIE_CSRF);
        });
        BilibiliSendMsgRequest request = new BilibiliSendMsgRequest(msg, StrUtil.toString(ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).toEpochSecond()), realRoomId, biliJct, biliJct);
        sendMsg(request, cookie);
    }

    /**
     * 为主播点赞
     *
     * @param request {@link BilibiliLikeReportV3Request}
     * @param cookie  Cookie
     */
    public static void likeReportV3(BilibiliLikeReportV3Request request, String cookie) {
        if (StrUtil.isBlank(cookie)) {
            throw new BaseException("为主播点赞接口cookie不能为空");
        }
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(request);
        @Cleanup HttpResponse execute = OrLiveChatHttpUtil.createPost("https://api.live.bilibili.com/xlive/app-ucenter/v1/like_info_v3/like/likeReportV3")
                .cookie(cookie)
                .form(stringObjectMap)
                .execute();
        responseInterceptor(execute.body());
    }

    /**
     * 为主播点赞
     *
     * @param anchor_id  主播Uid {@link RoomPlayInfoResult#uid}
     * @param realRoomId 真实房间Id {@link RoomPlayInfoResult#room_id}
     * @param cookie     Cookie
     */
    public static void likeReportV3(long anchor_id, long realRoomId, String cookie) {
        String uid = OrLiveChatCookieUtil.getCookieByName(cookie, KEY_UID, () -> {
            throw new BaseException("cookie中缺少参数" + KEY_UID);
        });
        String biliJct = OrLiveChatCookieUtil.getCookieByName(cookie, KEY_COOKIE_CSRF, () -> {
            throw new BaseException("cookie中缺少参数" + KEY_COOKIE_CSRF);
        });
        BilibiliLikeReportV3Request request = new BilibiliLikeReportV3Request(realRoomId, uid, anchor_id, biliJct, biliJct);
        likeReportV3(request, cookie);
    }

    private static JsonNode responseInterceptor(String responseString) {
        try {
            JsonNode jsonNode = OrJacksonUtil.getInstance().readTree(responseString);
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DanmuinfoResult {
        private String group;
        private int business_id;
        private double refresh_row_factor;
        private int refresh_rate;
        private int max_delay;
        private String token;
        private List<Host_list> host_list;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Host_list {
        private String host;
        private int port;
        private int wss_port;
        private int ws_port;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomPlayInfoResult {
        private long room_id;
        private int short_id;
        private long uid;
        private boolean is_hidden;
        private boolean is_locked;
        private boolean is_portrait;
        private BilibiliLiveStatusEnum live_status;
        private int hidden_till;
        private int lock_till;
        private boolean encrypted;
        private boolean pwd_verified;
        private long live_time;
        private int room_shield;
        private List<Integer> all_special_types;
        private JsonNode playurl_info;
        private int official_type;
        private int official_room_id;
        private int risk_with_delay;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomInitResult {
        private String buvid3;
        private String uid;

        private DanmuinfoResult danmuinfoResult = new DanmuinfoResult();
        private RoomPlayInfoResult roomPlayInfoResult = new RoomPlayInfoResult();
    }
}
