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

package tech.ordinaryroad.live.chat.client.codec.bilibili.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author mjz
 * @date 2024/3/11
 */
@Getter
@RequiredArgsConstructor
public enum BilibiliLiveStatusEnum {

    /**
     * 未开播
     */
    STOPPED(0),

    /**
     * 直播中
     */
    LIVING(1),

    /**
     * 投稿视频轮播
     */
    CAROUSEL(2),
    ;

    @JsonValue
    private final int code;

    public static BilibiliLiveStatusEnum getByCode(int code) {
        for (BilibiliLiveStatusEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
