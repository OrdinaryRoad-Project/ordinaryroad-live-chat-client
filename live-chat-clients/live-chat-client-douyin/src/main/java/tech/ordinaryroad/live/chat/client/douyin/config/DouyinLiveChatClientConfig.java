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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinGiftCountCalculationTimeEnum;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatHttpUtil;
import tech.ordinaryroad.live.chat.client.servers.netty.client.config.BaseNettyClientConfig;

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
    private DouyinGiftCountCalculationTimeEnum giftCountCalculationTime = DouyinGiftCountCalculationTimeEnum.IMMEDIATELY;
}
