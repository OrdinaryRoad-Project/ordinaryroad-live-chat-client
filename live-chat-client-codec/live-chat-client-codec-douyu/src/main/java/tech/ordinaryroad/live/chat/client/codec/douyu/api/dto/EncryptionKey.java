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

package tech.ordinaryroad.live.chat.client.codec.douyu.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 斗鱼加密密钥数据类
 *
 * @author Generated
 * @date 2026-01-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncryptionKey {

    /**
     * 加密数据
     */
    @JsonProperty("enc_data")
    private String encData;

    /**
     * 密钥
     */
    @JsonProperty("key")
    private String key;

    /**
     * 随机字符串
     */
    @JsonProperty("rand_str")
    private String randStr;

    /**
     * 加密次数
     */
    @JsonProperty("enc_time")
    private Integer encTime;

    /**
     * 是否特殊
     */
    @JsonProperty("is_special")
    private Integer isSpecial;

    /**
     * 过期时间（秒级时间戳）
     */
    @JsonProperty("expire_at")
    private Long expireAt;

    /**
     * C++相关配置
     */
    @JsonProperty("cpp")
    private CppConfig cpp;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CppConfig {
        /**
         * 过期时间（秒级时间戳）
         */
        @JsonProperty("expire_at")
        private Long expireAt;

        /**
         * 弹幕配置
         */
        @JsonProperty("danmu")
        private KeyConfig danmu;

        /**
         * 心跳配置
         */
        @JsonProperty("heartbeat")
        private KeyConfig heartbeat;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyConfig {
        /**
         * 密钥版本
         */
        @JsonProperty("key_ver")
        private String keyVer;

        /**
         * 密钥
         */
        @JsonProperty("key")
        private String key;
    }
}
