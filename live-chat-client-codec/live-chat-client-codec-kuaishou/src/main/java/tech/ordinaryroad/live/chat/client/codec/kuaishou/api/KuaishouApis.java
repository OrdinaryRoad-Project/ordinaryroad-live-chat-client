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

package tech.ordinaryroad.live.chat.client.codec.kuaishou.api;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.*;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.constant.RoomInfoGetTypeEnum;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouGiftMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.LiveAudienceStateOuterClass;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.WebGiftFeedOuterClass;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.util.OrJacksonUtil;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatHttpUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author mjz
 * @date 2024/1/5
 */
public class KuaishouApis {

    /**
     * 接口返回结果缓存
     * {@link #KEY_RESULT_CACHE_GIFT_ITEMS}：所有礼物信息
     */
    public static final TimedCache<String, Map<String, GiftInfo>> RESULT_CACHE = new TimedCache<>(TimeUnit.DAYS.toMillis(1));
    public static final String KEY_RESULT_CACHE_GIFT_ITEMS = "GIFT_ITEMS";
    public static final String PATTERN_LIVE_STREAM_ID = "\"liveStream\":\\{\"id\":\"([\\w\\d-_]+)\"";
    public static final String PATTERN_ROOM_TITLE = "\"author\".*?[\"name\"]{1}:(.*?),\"description\"";
    public static final String USER_AGENT = GlobalHeaders.INSTANCE.header(Header.USER_AGENT).replace("Hutool", "");
    /**
     * 礼物连击缓存
     */
    private static final TimedCache<String, WebGiftFeedOuterClass.WebGiftFeed> WEB_GIFT_FEED_CACHE = new TimedCache<>(300 * 1000L, new ConcurrentHashMap<>());

    public static RoomInitResult roomInitSetCookie(Object roomId, String cookie, RoomInitResult roomInitResult) {
        @Cleanup
        HttpResponse response = createGetRequest("https://live.kuaishou.com/u/" + roomId, cookie)
                .execute();

        if (StrUtil.isBlank(cookie)) {
            cookie = OrLiveChatCookieUtil.toString(response.getCookies());
        }

        String body = response.body();
        String liveStreamId = ReUtil.getGroup1(PATTERN_LIVE_STREAM_ID, body);
        JsonNode websocketinfo = websocketinfo(roomId, liveStreamId, cookie);
        if (!websocketinfo.has("token")) {
            throwExceptionWithTip("主播未开播，token获取失败 " + websocketinfo);
        }
        ArrayNode websocketUrls = websocketinfo.withArray("websocketUrls");
        ArrayList<String> websocketUrlList = CollUtil.newArrayList();
        for (JsonNode websocketUrl : websocketUrls) {
            websocketUrlList.add(websocketUrl.asText());
        }

        roomInitResult = Optional.ofNullable(roomInitResult).orElseGet(() -> RoomInitResult.builder().build());
        roomInitResult.setToken(websocketinfo.required("token").asText());
        roomInitResult.setWebsocketUrls(websocketUrlList);
        roomInitResult.setLiveStreamId(liveStreamId);
        roomInitResult.set_roomTitle(ReUtil.getGroup1(PATTERN_ROOM_TITLE, body));
        return roomInitResult;
    }

    public static RoomInitResult roomInitSetCookie(Object roomId, String cookie) {
        return roomInitSetCookie(roomId, cookie, null);
    }

    public static RoomInitResult roomInitGet(Object roomId, RoomInitResult roomInitResult) {
        @Cleanup
        HttpResponse response = createGetRequest("https://live.kuaishou.com/live_api/liveroom/livedetail?principalId=" + roomId, StrUtil.EMPTY)
                .execute();

        JsonNode livedetailJsonNode = responseInterceptor(response.body());
        JsonNode websocketInfoNode = livedetailJsonNode.get("websocketInfo");

        String liveStreamId = livedetailJsonNode.get("liveStream").required("id").asText(StrUtil.EMPTY);
        if (StrUtil.isBlankIfStr(liveStreamId)) {
            throwExceptionWithTip("主播未开播，liveStreamId为空");
        }
        String token = websocketInfoNode.required("token").asText(StrUtil.EMPTY);
        if (StrUtil.isBlankIfStr(token)) {
            throwExceptionWithTip("主播未开播，token获取失败");
        }
        JsonNode webSocketAddressesNode = websocketInfoNode.get("webSocketAddresses");
        List<String> websocketUrlList = new ArrayList<>(webSocketAddressesNode.size());
        for (JsonNode tempJsonNode : webSocketAddressesNode) {
            websocketUrlList.add(tempJsonNode.asText());
        }

        roomInitResult = Optional.ofNullable(roomInitResult).orElseGet(() -> RoomInitResult.builder().build());
        roomInitResult.setToken(token);
        roomInitResult.setWebsocketUrls(websocketUrlList);
        roomInitResult.setLiveStreamId(liveStreamId);
        roomInitResult.setLivedetailJsonNode(livedetailJsonNode);
        return roomInitResult;
    }

    public static RoomInitResult roomInitGet(Object roomId) {
        return roomInitGet(roomId, null);
    }

    public static RoomInitResult roomInit(Object roomId, RoomInfoGetTypeEnum roomInfoGetType, String cookie, RoomInitResult roomInitResult) {
        switch (roomInfoGetType) {
            case COOKIE: {
                return roomInitSetCookie(roomId, cookie, roomInitResult);
            }
            case NOT_COOKIE: {
                return roomInitGet(roomId, roomInitResult);
            }
            default: {
                throwExceptionWithTip("错误获取类型");
                return null;
            }
        }
    }

    public static RoomInitResult roomInit(Object roomId, RoomInfoGetTypeEnum roomInfoGetType, String cookie) {
        return roomInit(roomId, roomInfoGetType, cookie, null);
    }

    public static RoomInitResult roomInit(Object roomId) {
        return roomInit(roomId, RoomInfoGetTypeEnum.NOT_COOKIE, null);
    }

    public static RoomInitResult roomInit(Object roomId, RoomInitResult roomInitResult) {
        return roomInit(roomId, RoomInfoGetTypeEnum.NOT_COOKIE, null, roomInitResult);
    }

    public static JsonNode websocketinfo(Object roomId, String liveStreamId, String cookie) {
        if (StrUtil.isBlank(liveStreamId)) {
            throwExceptionWithTip("主播未开播，liveStreamId为空");
        }
        @Cleanup
        HttpResponse response = createGetRequest("https://live.kuaishou.com/live_api/liveroom/websocketinfo?liveStreamId=" + liveStreamId, cookie)
                .header(Header.REFERER, "https://live.kuaishou.com/u/" + roomId)
                .execute();
        return responseInterceptor(response.body());
    }

    public static Map<String, GiftInfo> allgifts() {
        Map<String, GiftInfo> map = new HashMap<>();
        @Cleanup
        HttpResponse response = createGetRequest("https://live.kuaishou.com/live_api/emoji/allgifts", null).execute();
        JsonNode jsonNode = responseInterceptor(response.body());
        jsonNode.fields().forEachRemaining(stringJsonNodeEntry -> map.put(stringJsonNodeEntry.getKey(), OrJacksonUtil.getInstance().convertValue(stringJsonNodeEntry.getValue(), GiftInfo.class)));
        return map;
    }

    /**
     * 根据礼物ID获取礼物信息
     *
     * @param id 礼物ID
     * @return 礼物信息
     */
    public static GiftInfo getGiftInfoById(String id) {
        if (!RESULT_CACHE.containsKey(KEY_RESULT_CACHE_GIFT_ITEMS)) {
            RESULT_CACHE.put(KEY_RESULT_CACHE_GIFT_ITEMS, allgifts());
        }
        return RESULT_CACHE.get(KEY_RESULT_CACHE_GIFT_ITEMS).get(id);
    }

    @SneakyThrows
    public static JsonNode sendComment(String cookie, Object roomId, SendCommentRequest request) {
        @Cleanup
        HttpResponse response = createPostRequest("https://live.kuaishou.com/live_api/liveroom/sendComment", cookie)
                .body(OrJacksonUtil.getInstance().writeValueAsString(request), ContentType.JSON.getValue())
                .header(Header.REFERER, "https://live.kuaishou.com/u/" + roomId)
                .execute();
        return responseInterceptor(response.body());
    }

    @SneakyThrows
    public static JsonNode clickLike(String cookie, Object roomId, String liveStreamId, int count) {
        @Cleanup
        HttpResponse response = createPostRequest("https://live.kuaishou.com/live_api/liveroom/like", cookie)
                .body(OrJacksonUtil.getInstance().createObjectNode()
                        .put("liveStreamId", liveStreamId)
                        .put("count", count)
                        .toString(), ContentType.JSON.getValue()
                )
                .header(Header.ORIGIN, "https://live.kuaishou.com")
                .header(Header.REFERER, "https://live.kuaishou.com/u/" + roomId)
                .execute();
        return responseInterceptor(response.body());
    }

    public static HttpRequest createRequest(Method method, String url, String cookie) {
        return OrLiveChatHttpUtil.createRequest(method, url)
                .cookie(cookie)
                .header(Header.HOST, URLUtil.url(url).getHost())
                .header(Header.USER_AGENT, USER_AGENT);
    }

    public static HttpRequest createGetRequest(String url, String cookie) {
        return createRequest(Method.GET, url, cookie);
    }

    public static HttpRequest createPostRequest(String url, String cookie) {
        return createRequest(Method.POST, url, cookie);
    }

    private static JsonNode responseInterceptor(String responseString) {
        try {
            JsonNode jsonNode = OrJacksonUtil.getInstance().readTree(responseString);
            JsonNode data = jsonNode.required("data");
            if (data.has("result")) {
                int result = data.get("result").asInt();
                if (result != 1) {
                    String message = "";
                    switch (result) {
                        case 2: {
                            message = "请求过快，请稍后重试";
                            break;
                        }
                        case 400002: {
                            message = "需要进行验证";
                            break;
                        }
                        default: {
                            message = "";
                        }
                    }
                    throwExceptionWithTip("接口访问失败：" + message + "，返回结果：" + jsonNode);
                }
            }
            return data;
        } catch (JsonProcessingException e) {
            throw new BaseException(e);
        }
    }

    private static void throwExceptionWithTip(String message) {
        throw new BaseException("『可能已触发滑块验证，建议配置Cookie或打开浏览器进行滑块验证后重试』" + message);
    }

    /**
     * 计算快手直播间收到礼物的个数
     *
     * @param msg KuaishouGiftMsg
     * @return 礼物个数
     */
    public static int calculateGiftCount(KuaishouGiftMsg msg) {
        if (msg == null || msg.getMsg() == null) {
            return 0;
        }

        int giftCount;
        WebGiftFeedOuterClass.WebGiftFeed webGiftFeed = msg.getMsg();
        String mergeKey = webGiftFeed.getMergeKey();
        if (WEB_GIFT_FEED_CACHE.containsKey(mergeKey)) {
            WebGiftFeedOuterClass.WebGiftFeed webGiftFeedByMergeKey = WEB_GIFT_FEED_CACHE.get(mergeKey);
            int comboCountByMergeKey = webGiftFeedByMergeKey.getComboCount();
            giftCount = webGiftFeed.getComboCount() - comboCountByMergeKey;
        } else {
            int batchSize = webGiftFeed.getBatchSize();
            int comboCount = webGiftFeed.getComboCount();
            if (comboCount == 1) {
                giftCount = batchSize;
            } else {
                giftCount = comboCount;
            }
        }
        WEB_GIFT_FEED_CACHE.put(mergeKey, webGiftFeed);

        msg.setCalculatedGiftCount(giftCount);
        return giftCount;
    }

    /**
     * 获取粉丝牌名称
     */
    public static String getBadgeName(LiveAudienceStateOuterClass.LiveAudienceState liveAudienceState) {
        String badgeName = null;
        try {
            for (LiveAudienceStateOuterClass.LiveAudienceState.LiveAudienceState_11 liveAudienceState11 : liveAudienceState.getLiveAudienceState11List()) {
                String badgeIcon = liveAudienceState11.getLiveAudienceState111().getBadgeIcon();
                if (StrUtil.startWithIgnoreCase(badgeIcon, "fans")) {
                    badgeName = liveAudienceState11.getLiveAudienceState111().getBadgeName();
                    break;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return badgeName;
    }

    /**
     * 获取粉丝牌等级
     */
    public static byte getBadgeLevel(LiveAudienceStateOuterClass.LiveAudienceState liveAudienceState) {
        byte badgeLevel = 0;
        try {
            badgeLevel = (byte) liveAudienceState.getLiveFansGroupState().getIntimacyLevel();
        } catch (Exception e) {
            // ignore
        }
        return badgeLevel;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SendCommentRequest {
        private String liveStreamId;
        private String content;
        private String color;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomInitResult {
        private String token;
        private String liveStreamId;
        private List<String> websocketUrls;
        private JsonNode livedetailJsonNode;

        // TODO REFACTOR THIS
        private String _roomTitle;

        public String getRoomTitle() {
            if (StrUtil.isNotBlank(_roomTitle)) {
                return _roomTitle;
            } else {
                return livedetailJsonNode.get("author").get("name").asText();
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GiftInfo {
        private String giftName;
        private String giftUrl;
    }
}
