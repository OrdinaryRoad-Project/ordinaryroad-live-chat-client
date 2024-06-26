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

package tech.ordinaryroad.live.chat.client.douyin.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.JavaInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinGiftCountCalculationTimeEnum;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatHttpUtil;
import tech.ordinaryroad.live.chat.client.servers.netty.client.config.BaseNettyClientConfig;

import javax.script.ScriptEngine;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author mjz
 * @date 2024/1/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class DouyinLiveChatClientConfig extends BaseNettyClientConfig {
    private static final ScriptEngine DEFAULT_ENGINE = createScriptEngine();

    public static final List<String> WEB_SOCKET_URIS = CollUtil.newArrayList("wss://webcast5-ws-web-lq.douyin.com/webcast/im/push/v2/", "wss://webcast5-ws-web-lf.douyin.com/webcast/im/push/v2/", "wss://webcast5-ws-web-hl.douyin.com/webcast/im/push/v2/");

    @Builder.Default
    private long heartbeatInitialDelay = 5;

    @Builder.Default
    private long heartbeatPeriod = 10;

    @Builder.Default
    private int aggregatorMaxContentLength = 64 * 1024 * 1024;

    @Builder.Default
    private int maxFramePayloadLength = 64 * 1024 * 1024;

    @Builder.Default
    private String versionCode = "180800";

    @Builder.Default
    private String webcastSdkVersion = "1.0.14-beta.0";

    @Builder.Default
    private String updateVersionCode = "1.0.14-beta.0";
    @Builder.Default
    private String userAgent = OrLiveChatHttpUtil.USER_AGENT;

    public String getBrowserVersion() {
        return StrUtil.removePrefix(getUserAgent(), "Mozilla/");
    }

    @Builder.Default
    private ScriptEngine scriptEngine = DEFAULT_ENGINE;

    @Builder.Default
    private DouyinGiftCountCalculationTimeEnum giftCountCalculationTime = DouyinGiftCountCalculationTimeEnum.IMMEDIATELY;

    @SneakyThrows
    public static ScriptEngine createScriptEngine() {
        JavaInfo javaInfo = new JavaInfo();
        Class<?> nashornScriptEngineFactoryClass;
        if (javaInfo.getVersionFloat() >= 11) {
            nashornScriptEngineFactoryClass = Class.forName("org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory");
        } else {
            nashornScriptEngineFactoryClass = Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory");
        }
        Object factory = nashornScriptEngineFactoryClass.getConstructor().newInstance();
        Method getScriptEngine = nashornScriptEngineFactoryClass.getDeclaredMethod("getScriptEngine", String[].class);
        return (ScriptEngine) getScriptEngine.invoke(factory, (Object) new String[]{"--language=es6"});
    }

    /**
     * 示例
     * wss://webcast5-ws-web-lf.douyin.com/webcast/im/push/v2/
     * ?app_name=douyin_web
     * &version_code=180800
     * &webcast_sdk_version=1.0.12
     * &update_version_code=1.0.12
     * &compress=gzip
     * &device_platform=web
     * &cookie_enabled=true
     * &screen_width=1512
     * &screen_height=982
     * &browser_language=zh-CN
     * &browser_platform=MacIntel
     * &browser_name=Mozilla
     * &browser_version=5.0%20(Macintosh;%20Intel%20Mac%20OS%20X%2010_15_7)%20AppleWebKit/537.36%20(KHTML,%20like%20Gecko)%20Chrome/118.0.0.0%20Safari/537.36
     * &browser_online=true
     * &tz_name=Asia/Shanghai
     * &cursor=u-1_h-1_t-1704202376885_r-1_d-1
     * &internal_ext=internal_src:dim|wss_push_room_id:7319486720022301449|wss_push_did:7319492411867170356|dim_log_id:20240102213256AAA5B735ADBE992BEF6A|first_req_ms:1704202376757|fetch_time:1704202376885|seq:1|wss_info:0-1704202376885-0-0|wrds_kvs:WebcastActivityEmojiGroupsMessage-1704200830782138545_WebcastRoomRankMessage-1704202270876589607_WebcastRoomStatsMessage-1704202372842388781
     * &host=https://live.douyin.com
     * &aid=6383
     * &live_id=1
     * &did_rule=3
     * &endpoint=live_pc
     * &support_wrds=1
     * &user_unique_id=7319492411867170356
     * &im_path=/webcast/im/fetch/
     * &identity=audience
     * &need_persist_msg_count=15
     * &room_id=7319486720022301449
     * &heartbeatDuration=0
     * &signature=Wk407jV1/WbnoIGk
     */

}
