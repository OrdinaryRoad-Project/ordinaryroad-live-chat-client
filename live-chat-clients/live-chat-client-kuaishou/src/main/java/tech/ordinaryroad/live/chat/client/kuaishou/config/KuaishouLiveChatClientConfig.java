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

package tech.ordinaryroad.live.chat.client.kuaishou.config;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.constant.RoomInfoGetTypeEnum;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil;
import tech.ordinaryroad.live.chat.client.servers.netty.client.config.BaseNettyClientConfig;

/**
 * @author mjz
 * @date 2024/1/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class KuaishouLiveChatClientConfig extends BaseNettyClientConfig {

    /**
     * 来源：快手网站 localstorage 中的 kwfv1 参数，或者请求头中 Kww 参数对应的值
     *
     * @deprecated 暂不需要显式配置该配置，会从cookie中读取kwfv1字段内容；如果配置了则优先使用配置的内容
     */
    private String kww;

    @Builder.Default
    private long heartbeatPeriod = 20;

    /**
     * {@link RoomInfoGetTypeEnum#COOKIE}：
     * 使用Config中配置的Cookie初始化直播间信息<br>
     * 如果出现“主播未开播，token获取失败”报错，可能已触发风控，需要更新Cookie，如果不需要Cookie相关功能（发送弹幕、为主播点赞等）可尝试改为{@link RoomInfoGetTypeEnum#NOT_COOKIE}解决
     * <p>
     * {@link RoomInfoGetTypeEnum#NOT_COOKIE}：
     * 不使用Config中配置的Cookie初始化直播间信息
     * <p>
     * 默认：{@link RoomInfoGetTypeEnum#COOKIE}
     *
     * @see RoomInfoGetTypeEnum
     */
    @Builder.Default
    private RoomInfoGetTypeEnum roomInfoGetType = RoomInfoGetTypeEnum.COOKIE;

    public void setRoomInfoGetType(RoomInfoGetTypeEnum roomInfoGetType) {
        RoomInfoGetTypeEnum oldValue = this.roomInfoGetType;
        this.roomInfoGetType = roomInfoGetType;
        super.propertyChangeSupport.firePropertyChange("roomInfoGetType", oldValue, roomInfoGetType);
    }

    /**
     * @deprecated 暂不需要显式配置该配置，会从cookie中读取kwfv1字段内容；如果配置了则优先使用配置的内容
     */
    @Deprecated
    public String getKww() {
        return StrUtil.blankToDefault(kww, OrLiveChatCookieUtil.getCookieByName(getCookie(), "kwfv1", () -> null));
    }
}
