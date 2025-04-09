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
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.request.BilibiliLikeReportV3Request;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.request.BilibiliSendMsgRequest;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.response.RoomPlayInfoResult;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliQualityEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.room.BilibiliRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.util.OrJacksonUtil;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatHttpUtil;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


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
    public static final String PATTERN_WINDOW_NEPTUNE_IS_MY_WAIFU = "<script>window.__NEPTUNE_IS_MY_WAIFU__=(.*?)</script>";

    private static final String API_FRONTEND_FINGER_SPI = "https://api.bilibili.com/x/frontend/finger/spi";
    private static final String API_V = "https://data.bilibili.com/v/";
    private static final String API_WEB_INTERFACE_NAV = "https://api.bilibili.com/x/web-interface/nav";

    // https://api.live.bilibili.com/room/v2/Room/get_by_ids?ids[]=545068
    private static final String API_ROOM_GET_BY_IDS = "https://api.live.bilibili.com/room/v2/Room/get_by_ids";
    private static final String API_ROOM_PLAY_INFO = "https://api.live.bilibili.com/xlive/web-room/v2/index/getRoomPlayInfo";
    // https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=545068&web_location=444.8&w_rid=c1d9bd0905ede21b6a5398d73a475179&wts=1735972204
    // https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=545068&web_location=444.8&w_rid=bf3c3e54df97078fb7b777759d0d1f3f&wts=1735974591
    private static final String API_INFO_BY_ROOM = "https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom";
    private static final String API_DANMU_INFO = "https://api.live.bilibili.com/xlive/web-room/v1/index/getDanmuInfo";
    private static final List<Integer> mixinKeyEncTab = CollUtil.newArrayList(46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49, 33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36, 20, 34, 44, 52);
    private static Pair<String, String> wbiKeys = null;

    @SneakyThrows
    public static BilibiliRoomInitResult roomInit(long roomId, String cookie, BilibiliRoomInitResult roomInitResult) {
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet("https://live.bilibili.com/" + roomId).execute();
        String body = response.body();

        RoomPlayInfoResult roomPlayInfo = getRoomPlayInfo(roomId, 0, cookie);
        long realRoomId = roomPlayInfo.getRoom_id();

        @Cleanup
        HttpResponse roomGetByIdResponse = OrLiveChatHttpUtil.createGet(API_ROOM_GET_BY_IDS + "?ids[]=" + realRoomId).execute();
        JsonNode roomGetByIdData = responseInterceptor(roomGetByIdResponse.body());
        RoomByIds roomByIds = OrJacksonUtil.getInstance().readValue(roomGetByIdData.get(realRoomId + "").toString(), RoomByIds.class);

        /**
         Map<String, String> wbiSign = getWbiSign(StrUtil.format(API_INFO_BY_ROOM + "?room_id={}&web_location=444.8", realRoomId));
         @Cleanup HttpResponse roomInfoResponse = OrLiveChatHttpUtil.createGet(API_INFO_BY_ROOM + "?" + OrLiveChatHttpUtil.toParams(wbiSign))
         .cookie(cookie)
         .header(Header.HOST, "api.live.bilibili.com")
         .header(Header.REFERER, "https://live.bilibili.com/")
         .execute();
         String roomInfoResJson = roomInfoResponse.body();
         RoomInfoRes roomInfoRes = OrJacksonUtil.getInstance().readValue(roomInfoResJson, RoomInfoRes.class);
         **/

        String b_3;
        String uid;
        if (StrUtil.isNotBlank(cookie)) {
            uid = OrLiveChatCookieUtil.getCookieByName(cookie, KEY_UID, () -> {
                throw new BaseException("cookie中缺少参数" + KEY_UID);
            });
            b_3 = OrLiveChatCookieUtil.getCookieByName(cookie, KEY_BUVID3, () -> {
                throw new BaseException("cookie中缺少参数" + KEY_BUVID3);
            });
            webInterfaceNav(String.valueOf(roomId), cookie);
        } else {
            uid = "0";

            JsonNode jsonNode = frontendFingerSpi();
            b_3 = jsonNode.get("b_3").asText();
            String b4 = jsonNode.get("b_4").asText();
            cookie = KEY_BUVID3 + "=" + b_3
                    + "; buvid4=" + b4;
        }

        DanmuinfoResult danmuInfo = getDanmuInfo(String.valueOf(roomId), realRoomId, cookie);

        roomInitResult = Optional.ofNullable(roomInitResult).orElseGet(() -> BilibiliRoomInitResult.builder().build());
        roomInitResult.setBuvid3(b_3);
        roomInitResult.setUid(uid);
        roomInitResult.setDanmuinfoResult(danmuInfo);
        roomInitResult.setRoomPlayInfoResult(roomPlayInfo);
        // roomInitResult.setRoomInfoRes(roomInfoRes);
        roomInitResult.setRoomByIds(roomByIds);
        return roomInitResult;
    }

    @SneakyThrows
    public static BilibiliRoomInitResult roomInit(long roomId, String cookie) {
        return roomInit(roomId, cookie, null);
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
    public static void webInterfaceNav(String roomIdString, String cookie) {
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet(API_WEB_INTERFACE_NAV)
                .header(Header.CACHE_CONTROL, "no-cache")
                .header(Header.ORIGIN, "https://live.bilibili.com")
                .header(Header.REFERER, "https://live.bilibili.com/" + roomIdString)
                .cookie(cookie)
                .execute();
        JsonNode data = OrJacksonUtil.getInstance().readTree(response.body()).get("data");
        String imgUrl = data.get("wbi_img").get("img_url").asText();
        String subUrl = data.get("wbi_img").get("sub_url").asText();

        String imgKey = StrUtil.split(imgUrl.substring(imgUrl.lastIndexOf('/') + 1), '.').get(0);
        String subKey = StrUtil.split(subUrl.substring(imgUrl.lastIndexOf('/') + 1), '.').get(0);
        wbiKeys = Pair.of(imgKey, subKey);
    }

    /**
     * 对 imgKey 和 subKey 进行字符顺序打乱编码
     */
    public static String getMixinKey(String origin) {
        int length = origin.length();
        StringBuilder result = new StringBuilder();
        for (int i : mixinKeyEncTab) {
            if (i < length) { // 确保索引不超出字符串长度
                result.append(origin.charAt(i));
            }
        }
        return result.substring(0, Math.min(result.length(), 32));
    }

    public static Map<String, String> getWbiSign(String url, String roomIdString, String cookie) {
        if (wbiKeys == null) {
            webInterfaceNav(roomIdString, cookie);
        }

        String mixinKey = getMixinKey(wbiKeys.getKey() + wbiKeys.getValue());
        long currentTime = System.currentTimeMillis() / 1000;

        Map<String, String> paramMap = OrLiveChatHttpUtil.decodeParamMap(url, StandardCharsets.UTF_8);
        paramMap.put("wts", currentTime + "");
        paramMap = CollUtil.sort(paramMap, Comparator.naturalOrder());

        Map<String, String> paramMapNew = new TreeMap<>();
        paramMap.forEach((k, v) -> {
            paramMapNew.put(k, v.chars().mapToObj(ch -> String.valueOf((char) ch))
                    .filter(c -> !"!'()*".contains(c))
                    .collect(Collectors.joining()));
        });
        String webSign = MD5.create().digestHex(StrUtil.format("{}{}", OrLiveChatHttpUtil.toParams(paramMapNew), mixinKey), StandardCharsets.UTF_8);
        paramMapNew.put("w_rid", webSign);
        return paramMapNew;
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
    public static DanmuinfoResult getDanmuInfo(String roomIdString, long roomId, int type, String cookie) {
        String url = API_DANMU_INFO + "?id=" + roomId + "&type=" + type;
        Map<String, String> wbiSign = getWbiSign(url, roomIdString, cookie);
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet(url + "&w_rid=" + wbiSign.get("w_rid") + "&wts=" + wbiSign.get("wts"))
                .header(Header.ORIGIN, "https://live.bilibili.com")
                .header(Header.REFERER, "https://live.bilibili.com/blanc/" + roomId + "?liteVersion=true&live_from=62001")
                .cookie(cookie)
                .execute();
        return OrJacksonUtil.getInstance().readValue(responseInterceptor(response.body()).toString(), DanmuinfoResult.class);
    }

    @SneakyThrows
    public static DanmuinfoResult getDanmuInfo(String roomIdString, long roomId, String cookie) {
        return getDanmuInfo(roomIdString, roomId, 0, cookie);
    }

    @SneakyThrows
    public static RoomPlayInfoResult getRoomPlayInfo(long roomId, int no_playurl, BilibiliQualityEnum qnEnum, String cookie) {
        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet(API_ROOM_PLAY_INFO + "?room_id=" + roomId + "&no_playurl=" + no_playurl + "&mask=1&qn=" + qnEnum.getQn() + "&platform=web&protocol=0,1&format=0,1,2&codec=0,1,2&dolby=5&panorama=1&hdr_type=0,1")
                .cookie(cookie)
                .execute();
        return OrJacksonUtil.getInstance().readValue(responseInterceptor(response.body()).toString(), RoomPlayInfoResult.class);
    }

    public static RoomPlayInfoResult getRoomPlayInfo(long roomId, int no_playurl, String cookie) {
        return getRoomPlayInfo(roomId, no_playurl, BilibiliQualityEnum.QN_DOLBY, cookie);
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
     * @param anchor_id  主播Uid {@link RoomPlayInfoResult#getUid()}
     * @param realRoomId 真实房间Id {@link RoomPlayInfoResult#getRoom_id()}
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
    public static class RoomByIds {
        private long roomid;
        private long uid;
        private String uname;
        private String verify;
        private int virtual;
        private String cover;
        private String live_time;
        private int round_status;
        private int on_flag;
        private String title;
        private String tags;
        private String lock_status;
        private String hidden_status;
        private String user_cover;
        private int short_id;
        private int online;
        private int area;
        private int area_v2_id;
        private int area_v2_parent_id;
        private int attentions;
        private String background;
        private int room_silent;
        private int room_shield;
        private String try_time;
        private String area_v2_name;
        private String first_live_time;
        private long live_id;
        private int live_status;
        private String area_v2_parent_name;
        private String link;
    }
}
