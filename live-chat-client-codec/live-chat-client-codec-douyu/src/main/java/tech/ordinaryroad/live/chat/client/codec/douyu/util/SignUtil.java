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

package tech.ordinaryroad.live.chat.client.codec.douyu.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.dto.EncryptionKey;

import java.util.HashMap;
import java.util.Map;

/**
 * 签名工具类
 * 根据JS加密逻辑实现的Java版本
 * <p>
 * 签名流程：
 * 1. 获取加密密钥（包含key、rand_str、enc_time等）
 * 2. 对于stream类型：
 * - 如果is_special==1，则o为空字符串
 * - 否则o = rid + ts
 * - a = key
 * 3. 签名计算：
 * - u = rand_str
 * - 循环enc_time次：u = MD5(u + a)
 * - 最后：u = MD5(u + a + o)
 * - auth = u
 *
 * @author Generated
 * @date 2026-01-27
 */
@Slf4j
public class SignUtil {

    /**
     * 默认设备ID
     */
    public static final String DEFAULT_DID = "10000000000000000000000000001501";

    /**
     * 算法版本
     */
    private static final String ALG_VER = "1.0";

    /**
     * 生成签名
     * <p>
     * 根据JS代码逻辑：
     * - 对于stream类型：o = (is_special==1 ? "" : rid+ts), a = key.key
     * - u = rand_str
     * - 循环enc_time次：u = MD5(u + a)
     * - 最后：u = MD5(u + a + o)
     *
     * @param type 类型（stream/login/heartbeat）
     * @param ts   时间戳（秒级）
     * @param did  设备ID
     * @param rid  房间ID
     * @param key  加密密钥对象
     * @return 签名结果
     */
    public static SignResult generateSign(String type, long ts, String did, long rid, EncryptionKey key) {
        if (key == null) {
            throw new IllegalArgumentException("加密密钥不能为空");
        }

        String randStr = StrUtil.blankToDefault(key.getRandStr(), "");
        int encTime = key.getEncTime() != null ? key.getEncTime() : 0;
        String keyValue = StrUtil.blankToDefault(key.getKey(), "");
        Integer isSpecial = key.getIsSpecial() != null ? key.getIsSpecial() : 0;

        // 根据类型构建签名字符串
        String o = ""; // 签名字符串后缀
        String a = ""; // 密钥
        String keyVer = ""; // 密钥版本

        if ("stream".equals(type)) {
            // 如果is_special==1，则o为空，否则o = rid + ts
            if (isSpecial != 1) {
                o = rid + String.valueOf(ts);
            }
            a = keyValue;
        } else if ("login".equals(type)) {
            o = rid + did + ts;
            if (key.getCpp() != null && key.getCpp().getDanmu() != null) {
                keyVer = StrUtil.blankToDefault(key.getCpp().getDanmu().getKeyVer(), "");
                a = StrUtil.blankToDefault(key.getCpp().getDanmu().getKey(), "");
            }
        } else if ("heartbeat".equals(type)) {
            o = rid + did + ts;
            if (key.getCpp() != null && key.getCpp().getHeartbeat() != null) {
                keyVer = StrUtil.blankToDefault(key.getCpp().getHeartbeat().getKeyVer(), "");
                a = StrUtil.blankToDefault(key.getCpp().getHeartbeat().getKey(), "");
            }
        } else {
            throw new IllegalArgumentException("不支持的签名类型: " + type);
        }

        // 开始计算签名
        long startTime = System.currentTimeMillis();
        log.debug("开始计算签名({}), 加密次数: {}", type, encTime);

        // u = rand_str
        String u = randStr;

        // 循环enc_time次：u = MD5(u + a)
        for (int i = 0; i < encTime; i++) {
            u = DigestUtil.md5Hex(u + a);
        }

        // 最后：u = MD5(u + a + o)
        u = DigestUtil.md5Hex(u + a + o);

        long endTime = System.currentTimeMillis();
        log.debug("签名计算完成({}), 加密次数: {}, 耗时: {}ms", type, encTime, (endTime - startTime));

        // 构建返回的key对象（排除cpp字段）
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("enc_data", key.getEncData());
        keyMap.put("key", key.getKey());
        keyMap.put("rand_str", key.getRandStr());
        keyMap.put("enc_time", key.getEncTime());
        keyMap.put("is_special", key.getIsSpecial());
        keyMap.put("expire_at", key.getExpireAt());

        return SignResult.builder()
                .key(keyMap)
                .algVer(ALG_VER)
                .keyVer(keyVer)
                .auth(u)
                .ts(ts)
                .build();
    }

    /**
     * 构建完整的请求参数字符串
     * <p>
     * 格式：enc_data=xxx&tt=xxx&did=xxx&auth=xxx
     *
     * @param signResult 签名结果
     * @param did        设备ID
     * @return URL参数字符串
     */
    public static String buildRequestParams(SignResult signResult, String did) {
        if (signResult == null || signResult.getKey() == null) {
            throw new IllegalArgumentException("签名结果不能为空");
        }

        Map<String, String> params = new HashMap<>();
        params.put("enc_data", String.valueOf(signResult.getKey().get("enc_data")));
        params.put("tt", String.valueOf(signResult.getTs()));
        params.put("did", did);
        params.put("auth", signResult.getAuth());

        // 拼接参数
        StringBuilder sb = new StringBuilder();
        params.forEach((k, v) -> {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(k).append("=").append(v);
        });

        return sb.toString();
    }

    /**
     * 快速生成stream类型的签名参数
     *
     * @param ts  时间戳（秒级）
     * @param did 设备ID
     * @param rid 房间ID
     * @param key 加密密钥
     * @return 完整的请求参数字符串
     */
    public static String quickSignStream(long ts, String did, long rid, EncryptionKey key) {
        SignResult signResult = generateSign("stream", ts, did, rid, key);
        return buildRequestParams(signResult, did);
    }

    /**
     * 检查密钥是否有效
     *
     * @param key  加密密钥
     * @param type 类型
     * @return true-有效，false-无效
     */
    public static boolean isKeyValid(EncryptionKey key, String type) {
        if (key == null) {
            return false;
        }

        Long expireAt;
        if ("stream".equals(type)) {
            expireAt = key.getExpireAt();
        } else {
            if (key.getCpp() == null) {
                return false;
            }
            expireAt = key.getCpp().getExpireAt();
        }

        if (expireAt == null) {
            return false;
        }

        // 检查是否过期（expire_at是秒级时间戳）
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        return expireAt > currentTimeSeconds;
    }

    /**
     * 签名结果
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignResult {
        /**
         * 密钥信息（排除cpp字段）
         */
        private Map<String, Object> key;

        /**
         * 算法版本
         */
        private String algVer;

        /**
         * 密钥版本（stream类型为空）
         */
        private String keyVer;

        /**
         * 签名auth
         */
        private String auth;

        /**
         * 时间戳（秒级）
         */
        private Long ts;
    }
}
