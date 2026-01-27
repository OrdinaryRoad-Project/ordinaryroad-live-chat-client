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

package tech.ordinaryroad.live.chat.client.codec.douyu.api;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.dto.EncryptionKey;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.response.BetardResponse;
import tech.ordinaryroad.live.chat.client.codec.douyu.msg.dto.GiftListInfo;
import tech.ordinaryroad.live.chat.client.codec.douyu.msg.dto.GiftPropSingle;
import tech.ordinaryroad.live.chat.client.codec.douyu.room.DouyuRoomInitResult;
import tech.ordinaryroad.live.chat.client.codec.douyu.util.SignUtil;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * API简易版
 *
 * @author mjz
 * @date 2023/5/5
 */
@Slf4j
public class DouyuApis {

    /**
     * 通用礼物缓存，过期时间1天
     * pid,Info
     */
    public static final TimedCache<String, GiftPropSingle> giftMap = new TimedCache<>(TimeUnit.DAYS.toMillis(1));
    /**
     * 房间礼物缓存，过期时间1天
     * realRoomId,(giftId,Info)
     */
    public static final TimedCache<String, Map<Long, GiftListInfo>> roomGiftMap = new TimedCache<>(TimeUnit.DAYS.toMillis(1), new HashMap<>());
    /**
     * 加密密钥缓存
     * did -> EncryptionKey
     * 使用动态过期时间，根据密钥的expire_at字段设置，提前5分钟过期以确保安全
     */
    public static final TimedCache<String, EncryptionKey> encryptionKeyCache = new TimedCache<>(TimeUnit.HOURS.toMillis(24));

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String PATTERN_BODY_ROOM_ID = "\\\\\"room_id\\\\\"\\D+(\\d+)";
    public static final String KEY_REDIRECT_LOCATION_RID = "rid";
    public static final String KEY_COOKIE_DY_DID = "dy_did";
    public static final String KEY_COOKIE_ACF_UID = "acf_uid";
    public static final String KEY_COOKIE_ACF_STK = "acf_stk";
    public static final String KEY_COOKIE_ACF_LTKID = "acf_ltkid";
    public static final String API_AVATAR = "https://apic.douyucdn.cn/upload/";
    // https://webconf.douyucdn.cn/resource/common/gift/flash/gift_effect.json
    // https://webconf.douyucdn.cn/resource/common/gift/common_config_v2.json
    // https://webconf.douyucdn.cn/resource/common/prop_gift_list/prop_gift_config.json
    // 用PID查询礼物信息：https://gift.douyucdn.cn/api/prop/v1/web/single?pid=
    // 查询房间礼物列表：https://gift.douyucdn.cn/api/gift/v3/web/list?rid=
    public static final String API_GIFT_LIST = "https://gift.douyucdn.cn/api/gift/v3/web/list?rid=";
    public static final String API_PROP_SINGLE = "https://gift.douyucdn.cn/api/prop/v1/web/single?pid=";
    public static final String API_SERVER_INFO = "https://www.douyu.com/lapi/live/gateway/web/";
    // https://www.douyu.com/betard/${realRoomId}
    public static final String API_BETARD = "https://www.douyu.com/betard/";
    public static final String API_AVATAR_PREFIX_SMALL = "_small.jpg";
    public static final String API_AVATAR_PREFIX_MIDDLE = "_middle.jpg";
    public static final String API_AVATAR_PREFIX_BIG = "_big.jpg";
    // 参考： <a href="https://cjting.me/2020/07/01/douyu-crawler-and-font-anti-crawling">斗鱼关注人数爬取 ── 字体反爬的攻与防</a>
    public static final String vk_secret = "r5*^5;}2#${XF[h+;'./.Q'1;,-]f'p[";
    /**
     * 获取加密密钥API
     */
    public static final String API_GET_ENCRYPTION = "https://www.douyu.com/wgapi/livenc/liveweb/websec/getEncryption";
    /**
     * 获取H5播放流API
     */
    public static final String API_GET_H5_PLAY_V1 = "https://www.douyu.com/lapi/live/getH5PlayV1/";

    public static String getAvatarUrl(List<String> list, String prefix) {
        if (CollUtil.isEmpty(list) || list.size() < 3) {
            return StrUtil.EMPTY;
        }
        return API_AVATAR + CollUtil.join(list, "/") + prefix;
    }

    public static String getSmallAvatarUrl(List<String> list) {
        return getAvatarUrl(list, API_AVATAR_PREFIX_SMALL);
    }

    public static long getRealRoomId(Object roomId, String cookie) {
        @Cleanup
        HttpResponse execute = OrLiveChatHttpUtil.createGet("https://www.douyu.com/" + roomId)
                .cookie(cookie)
                .execute();
        return getRealRoomIdByRoomPageResponse(roomId, execute);
    }

    private static long getRealRoomIdByRoomPageResponse(Object roomId, HttpResponse execute) {
        String realRoomIdString = null;
        if (execute.getStatus() == HttpStatus.HTTP_NOT_FOUND) {
            throw new BaseException("获取" + roomId + "真实房间ID失败");
        }
        if (HttpStatus.isRedirected(execute.getStatus())) {
            String location = execute.header(Header.LOCATION);
            Map<String, String> paramMap = OrLiveChatHttpUtil.decodeParamMap(location, null);
            if (paramMap.containsKey(KEY_REDIRECT_LOCATION_RID)) {
                realRoomIdString = paramMap.get(KEY_REDIRECT_LOCATION_RID);
            }
        }
        if (StrUtil.isBlank(realRoomIdString)) {
            String body = execute.body();
            String matchString = ReUtil.get(PATTERN_BODY_ROOM_ID, body, 1);
            if (StrUtil.isNotBlank(matchString)) {
                realRoomIdString = matchString;
            }
        }
        long realRoomId;
        try {
            // 正则或者传入的参数
            realRoomId = NumberUtil.parseLong(StrUtil.blankToDefault(realRoomIdString, StrUtil.toString(roomId)));
        } catch (Exception e) {
            throw new BaseException("获取" + roomId + "真实房间ID失败");
        }
        return realRoomId;
    }

    public static long getRealRoomId(Object roomId) {
        return getRealRoomId(roomId, null);
    }

    @SneakyThrows
    public static ServerInfoResult getServerInfo(long roomId, String cookie) {
        @Cleanup
        HttpResponse execute = OrLiveChatHttpUtil.createPost(API_SERVER_INFO + roomId + "?isH5=1")
                .cookie(cookie)
                .execute();
        JsonNode jsonNode = responseInterceptor(execute.body());
        return OrJacksonUtil.getInstance().readValue(jsonNode.toString(), ServerInfoResult.class);
    }

    public static ServerInfoResult getServerInfo(long roomId) {
        return getServerInfo(roomId, null);
    }

    public static String getRandomWssUri(long roomId) {
        ServerInfoResult serverInfoResult = getServerInfo(roomId);
        List<Wss> wssList = serverInfoResult.getWss();
        Wss randomWss = OrLiveChatCollUtil.getRandom(wssList);
        return "wss://" + randomWss.getDomain() + ":" + randomWss.getPort();
    }

    @SneakyThrows
    public static GiftListResult getGiftList(long roomId) {
        @Cleanup
        HttpResponse execute = OrLiveChatHttpUtil.createGet(API_GIFT_LIST + roomId).execute();
        JsonNode jsonNode = responseInterceptor(execute.body());
        return OrJacksonUtil.getInstance().readValue(jsonNode.toString(), GiftListResult.class);
    }

    @SneakyThrows
    public static GiftPropSingle getGiftPropSingleByPid(String pid) {
        @Cleanup
        HttpResponse execute = OrLiveChatHttpUtil.createGet(API_PROP_SINGLE + pid).execute();
        JsonNode jsonNode = responseInterceptor(execute.body());
        return OrJacksonUtil.getInstance().readValue(jsonNode.toString(), GiftPropSingle.class);
    }

    /**
     * 参考： <a href="https://cjting.me/2020/07/01/douyu-crawler-and-font-anti-crawling">斗鱼关注人数爬取 ── 字体反爬的攻与防</a>
     *
     * @param currentTimeSecs
     * @param did
     * @return
     */
    public static String generateVk(long currentTimeSecs, String did) {
        return MD5.create().digestHex(currentTimeSecs + vk_secret + did);
    }

    public static String generateVk(String did) {
        return generateVk(OrLiveChatLocalDateTimeUtil.zonedCurrentTimeSecs(), did);
    }

    /**
     * 更新房间礼物缓存
     *
     * @param roomId 房间号
     */
    public static Map<Long, GiftListInfo> updateRoomGiftMapCache(long roomId) {
        Map<Long, GiftListInfo> map = new HashMap<>();
        List<GiftListInfo> giftList = DouyuApis.getGiftList(roomId).getGiftList();
        CollUtil.forEach(giftList, (giftListInfo, index) -> {
            try {
                map.put(giftListInfo.getId(), giftListInfo);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("获取房间礼物列表异常", e);
                }
                // ignore
            }
        });
        DouyuApis.roomGiftMap.put(String.valueOf(DouyuApis.getRealRoomId(roomId)), map);
        return map;
    }

    public static JsonNode responseInterceptor(String responseString) {
        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(responseString);
            int code = jsonNode.get("error").asInt();
            if (code == 0) {
                // 成功
                return jsonNode.get("data");
            } else {
                throw new BaseException(jsonNode.get("msg").asText());
            }
        } catch (JsonProcessingException e) {
            throw new BaseException(e);
        }
    }

    @SneakyThrows
    public static DouyuRoomInitResult roomInit(Object roomId, String cookie, DouyuRoomInitResult roomInitResult) {
        Map<String, String> cookieMap = OrLiveChatCookieUtil.parseCookieString(cookie);

        long uid;
        String did;
        if (cookieMap.isEmpty()) {
            uid = RandomUtil.randomInt(10000, 19999);
            did = UUID.fastUUID().toString(true);
        } else {
            uid = NumberUtil.parseLong(OrLiveChatCookieUtil.getCookieByName(cookieMap, DouyuApis.KEY_COOKIE_ACF_UID, () -> {
                throw new BaseException("Cookie中缺少字段" + DouyuApis.KEY_COOKIE_ACF_UID);
            }));
            did = OrLiveChatCookieUtil.getCookieByName(cookieMap, DouyuApis.KEY_COOKIE_DY_DID, () -> {
                throw new BaseException("Cookie中缺少字段" + DouyuApis.KEY_COOKIE_DY_DID);
            });
        }

        @Cleanup
        HttpResponse execute = OrLiveChatHttpUtil.createGet("https://www.douyu.com/" + roomId, true).cookie(cookie).execute();
        long realRoomId = getRealRoomIdByRoomPageResponse(roomId, execute);

        @Cleanup
        HttpResponse response = OrLiveChatHttpUtil.createGet(API_BETARD + realRoomId).cookie(cookie).execute();
        String body = response.body();
        BetardResponse betardResponse = OrJacksonUtil.getInstance().readValue(body, BetardResponse.class);

        roomInitResult = Optional.ofNullable(roomInitResult).orElse(DouyuRoomInitResult.builder().build());
        roomInitResult.setRealRoomId(realRoomId);
        roomInitResult.setUid(uid);
        roomInitResult.setDid(did);
        roomInitResult.setCookie(cookie);
        roomInitResult.setBetardResponse(betardResponse);
        return roomInitResult;
    }

    public static DouyuRoomInitResult roomInit(Long roomId, DouyuRoomInitResult roomInitResult) {
        return roomInit(roomId, null, roomInitResult);
    }

    public static DouyuRoomInitResult roomInit(Long roomId) {
        return roomInit(roomId, null, null);
    }

    /**
     * 获取加密密钥（带缓存）
     * <p>
     * 先从缓存获取，如果缓存中没有或已过期，则从服务器获取并缓存
     *
     * @param did 设备ID
     * @return 加密密钥对象
     */
    @SneakyThrows
    public static EncryptionKey getEncryptionKey(String did) {
        // 先从缓存获取
        EncryptionKey cachedKey = encryptionKeyCache.get(did, false);
        if (SignUtil.isKeyValid(cachedKey, "stream")) {
            log.debug("从缓存获取加密密钥，did: {}", did);
            return cachedKey;
        }

        // 缓存中没有或已过期，从服务器获取
        log.debug("从服务器获取加密密钥，did: {}", did);
        String url = API_GET_ENCRYPTION + "?did=" + did;
        @Cleanup
        HttpResponse execute = OrLiveChatHttpUtil.createGet(url)
                .header(Header.REFERER, "https://www.douyu.com/")
                .execute();
        JsonNode jsonNode = responseInterceptor(execute.body());
        EncryptionKey key = OrJacksonUtil.getInstance().readValue(jsonNode.toString(), EncryptionKey.class);

        // 设置cpp的过期时间（24小时后）
        if (key.getCpp() != null) {
            long expireAt = System.currentTimeMillis() / 1000 + 86400;
            EncryptionKey.CppConfig cppConfig = key.getCpp();
            cppConfig.setExpireAt(expireAt);
        }

        // 计算缓存过期时间：根据密钥的expire_at，提前5分钟过期以确保安全
        long cacheExpireTime;
        if (key.getExpireAt() != null) {
            long currentTimeSeconds = System.currentTimeMillis() / 1000;
            long remainingSeconds = key.getExpireAt() - currentTimeSeconds;
            // 提前5分钟过期，但至少保留1分钟
            long cacheSeconds = Math.max(remainingSeconds - 300, 60);
            cacheExpireTime = TimeUnit.SECONDS.toMillis(cacheSeconds);
        } else {
            // 如果没有过期时间，默认缓存23小时
            cacheExpireTime = TimeUnit.HOURS.toMillis(23);
        }

        // 存入缓存
        encryptionKeyCache.put(did, key, cacheExpireTime);
        log.debug("加密密钥已缓存，did: {}, 缓存过期时间: {}ms", did, cacheExpireTime);

        return key;
    }

    /**
     * 获取H5播放流信息（使用新的签名方式）
     *
     * @param roomId 房间ID
     * @param did    设备ID
     * @param key    加密密钥（如果为null，会自动获取）
     * @return 播放流信息JSON
     */
    @SneakyThrows
    public static JsonNode getH5PlayV1(long roomId, String did, EncryptionKey key) {
        // 如果密钥为空或无效，则获取新密钥
        if (!SignUtil.isKeyValid(key, "stream")) {
            key = getEncryptionKey(did);
        }

        // 生成时间戳
        long ts = System.currentTimeMillis() / 1000;

        // 生成签名
        SignUtil.SignResult signResult = SignUtil.generateSign("stream", ts, did, roomId, key);

        // 构建请求参数
        String signParams = SignUtil.buildRequestParams(signResult, did);

        // 添加其他参数
        Map<String, String> params = new HashMap<>();
        params.put("cdn", "");
        params.put("ver", "Douyu_new");
        params.put("rate", "-1");
        params.put("hevc", "0");
        params.put("fa", "0");
        params.put("ive", "0");

        // 合并签名参数和其他参数
        StringBuilder body = new StringBuilder(signParams);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            body.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }

        // 发送POST请求
        @Cleanup
        HttpResponse execute = OrLiveChatHttpUtil.createPost(API_GET_H5_PLAY_V1 + roomId)
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .header(Header.REFERER, "https://www.douyu.com/")
                .body(body.toString())
                .execute();

        return responseInterceptor(execute.body());
    }

    /**
     * 获取H5播放流信息（使用默认设备ID）
     *
     * @param roomId 房间ID
     * @return 播放流信息JSON
     */
    public static JsonNode getH5PlayV1(long roomId) {
        return getH5PlayV1(roomId, SignUtil.DEFAULT_DID, null);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GiftListResult {
        private List<GiftListInfo> giftList;
        private long onceLimit;
        private JsonNode skinData;
        private JsonNode tabs;
        private String tid;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServerInfoResult {
        private List<Gateway> gateway;
        private List<Wss> wss;
        private int ir;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Gateway {
        private String ip;
        private int port;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Wss {
        private String domain;
        private int port;
    }
}
