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

package tech.ordinaryroad.live.chat.client.codec.douyin.constant;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author mjz
 * @date 2024/3/10
 */
@Getter
@RequiredArgsConstructor
public enum DouyinPayloadTypeEnum {

    /**
     * 心跳、心跳回复
     */
    HB("hb"),

    /**
     * 消息
     */
    MSG("msg"),

    ACK("ack"),
    ;

    private final String code;

    public static DouyinPayloadTypeEnum getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        for (DouyinPayloadTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
