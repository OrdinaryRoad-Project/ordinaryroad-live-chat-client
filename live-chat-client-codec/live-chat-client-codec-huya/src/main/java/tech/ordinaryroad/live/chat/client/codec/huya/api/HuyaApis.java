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

package tech.ordinaryroad.live.chat.client.codec.huya.api;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.huya.api.response.TtPlayerCfg;
import tech.ordinaryroad.live.chat.client.codec.huya.api.response.TtPlayerConf;
import tech.ordinaryroad.live.chat.client.codec.huya.api.response.TtProfileInfo;
import tech.ordinaryroad.live.chat.client.codec.huya.api.response.TtRoomData;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.dto.PropsItem;
import tech.ordinaryroad.live.chat.client.codec.huya.room.RoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.util.OrJacksonUtil;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatHttpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * API简易版
 * <br/>
 * <a href="https://a.msstatic.com/huya/h5player/room/2309271152/vplayerUI.js">vplayerUI.js</a>
 * <br/>
 * <a href="https://hd2.huya.com/fedbasic/huyabaselibs/taf-signal/taf-signal.global.0.0.4.prod.js">taf-signal.global.0.0.4.prod.js</a>
 *
 * @author mjz
 * @date 2023/9/5
 */
@Slf4j
public class HuyaApis {

    // TODO TimedCache
    public static final Map<Integer, PropsItem> GIFT_ITEMS = new HashMap<>();

    private static final String PATTERN_TT_ROOM_DATA = "TT_ROOM_DATA = (\\{.*?\\});";
    private static final String PATTERN_TT_PROFILE_INFO = "TT_PROFILE_INFO = (\\{.*\\}?);";
    private static final String PATTERN_TT_PLAYER_CFG = "TT_PLAYER_CFG = (\\{.*?\\});";
    private static final String PATTERN_TT_PLAYER_CONF = "TT_PLAYER_CONF = (\\{.*?\\});";
    private static final String PATTERN_HY_PLAYER_CONFIG_STREAM = "hyPlayerConfig\\s*=\\s*\\{[\\s\\S]*?stream:\\s*(\\{[\\s\\S]*?\\})\\s*\\}";

    public static RoomInitResult roomInit(Object roomId) {
        return roomInit(roomId, null);
    }

    public static RoomInitResult roomInit(Object roomId, String cookie) {
        return roomInit(roomId, cookie, null);
    }

    public static RoomInitResult roomInit(Object roomId, String cookie, RoomInitResult roomInitResult) {
        @Cleanup
        HttpResponse response = createGetRequest("https://www.huya.com/" + roomId, cookie).execute();
        if (response.getStatus() != HttpStatus.HTTP_OK) {
            throw new BaseException("获取" + roomId + "真实房间ID失败");
        }
        String body = response.body();
        String lSubChannelId = ReUtil.getGroup1("\"lp\"\\D+(\\d+)", body);
        String lChannelId = ReUtil.getGroup1("\"lp\"\\D+(\\d+)", body);
        String lYyid = ReUtil.getGroup1("\"yyid\"\\D+(\\d+)", body);

        String ttRoomDataJsonString = ReUtil.getGroup1(PATTERN_TT_ROOM_DATA, body);
        String ttProfileInfoJsonString = ReUtil.getGroup1(PATTERN_TT_PROFILE_INFO, body);
        String ttPlayerCfgJsonString = ReUtil.getGroup1(PATTERN_TT_PLAYER_CFG, body);
        String ttPlayerConfJsonString = ReUtil.getGroup1(PATTERN_TT_PLAYER_CONF, body);
        String hyPlayerConfigStreamJsonString = ReUtil.getGroup1(PATTERN_HY_PLAYER_CONFIG_STREAM, body);
        TtRoomData ttRoomData = null;
        TtProfileInfo ttProfileInfo = null;
        TtPlayerCfg ttPlayerCfg = null;
        TtPlayerConf ttPlayerConf = null;
        JsonNode hyPlayerConfigStream = null;
        try {
            ttRoomData = OrJacksonUtil.getInstance().readValue(ttRoomDataJsonString, TtRoomData.class);
            ttProfileInfo = OrJacksonUtil.getInstance().readValue(ttProfileInfoJsonString, TtProfileInfo.class);
            ttPlayerCfg = OrJacksonUtil.getInstance().readValue(ttPlayerCfgJsonString, TtPlayerCfg.class);
            ttPlayerConf = OrJacksonUtil.getInstance().readValue(ttPlayerConfJsonString, TtPlayerConf.class);
            hyPlayerConfigStream = OrJacksonUtil.getInstance().readTree(hyPlayerConfigStreamJsonString);
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("直播间信息解析失败", e);
            }
        }

        roomInitResult = Optional.ofNullable(roomInitResult).orElse(RoomInitResult.builder().build());
        roomInitResult.setLSubChannelId(StrUtil.emptyToDefault(lSubChannelId, "0"));
        roomInitResult.setLChannelId(NumberUtil.parseLong(lChannelId, 0L));
        roomInitResult.setLYyid(NumberUtil.parseLong(lYyid, 0L));
        roomInitResult.setTtRoomData(ttRoomData);
        roomInitResult.setTtProfileInfo(ttProfileInfo);
        roomInitResult.setTtPlayerCfg(ttPlayerCfg);
        roomInitResult.setTtPlayerConf(ttPlayerConf);
        roomInitResult.setHyPlayerConfigStream(hyPlayerConfigStream);
        return roomInitResult;
    }

    public static HttpRequest createGetRequest(String url, String cookies) {
        return OrLiveChatHttpUtil.createGet(url)
                .cookie(cookies);
    }
}
