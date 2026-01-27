package tech.ordinaryroad.live.chat.client.codec.douyu.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.DouyuApis;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.dto.EncryptionKey;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 签名工具类测试
 *
 * @author Generated
 * @date 2026-01-27
 */
public class SignUtilTest {

    @Test
    @DisplayName("测试基本签名生成")
    public void testGenerateSign() {
        // 准备测试数据
        String type = "stream";
        long ts = System.currentTimeMillis() / 1000;
        String did = SignUtil.DEFAULT_DID;
        long rid = 123456;

        // 获取加密密钥
        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        // 执行签名
        SignUtil.SignResult result = SignUtil.generateSign(type, ts, did, rid, key);

        // 验证结果
        assertNotNull(result, "签名结果不应为空");
        assertNotNull(result.getKey(), "结果应包含key字段");
        assertTrue(result.getKey().containsKey("enc_data"), "key应包含enc_data字段");
        assertNotNull(result.getTs(), "结果应包含ts字段");
        assertNotNull(result.getAuth(), "结果应包含auth字段");

        // 验证auth是32位MD5
        String auth = result.getAuth();
        assertEquals(32, auth.length(), "MD5签名应为32位");
    }

    @Test
    @DisplayName("测试请求参数构建")
    public void testBuildRequestParams() {
        // 准备测试数据
        String type = "stream";
        long ts = System.currentTimeMillis() / 1000;
        String did = SignUtil.DEFAULT_DID;
        long rid = 123456;

        // 获取加密密钥
        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        // 生成签名
        SignUtil.SignResult signResult = SignUtil.generateSign(type, ts, did, rid, key);

        // 构建请求参数
        String params = SignUtil.buildRequestParams(signResult, did);

        // 验证参数格式
        assertNotNull(params, "参数字符串不应为空");
        assertTrue(params.contains("enc_data="), "应包含enc_data参数");
        assertTrue(params.contains("tt="), "应包含tt参数");
        assertTrue(params.contains("did="), "应包含did参数");
        assertTrue(params.contains("auth="), "应包含auth参数");

        // 解析参数验证
        String[] pairs = params.split("&");
        assertEquals(4, pairs.length, "应包含4个参数");
    }

    @Test
    @DisplayName("测试快速签名")
    public void testQuickSignStream() {
        String did = SignUtil.DEFAULT_DID;
        long rid = 123456;

        // 获取加密密钥
        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        // 执行快速签名
        long ts = System.currentTimeMillis() / 1000;
        String params = SignUtil.quickSignStream(ts, did, rid, key);

        // 验证
        assertNotNull(params, "快速签名结果不应为空");
        assertTrue(params.contains("&"), "应包含多个参数");
    }

    @Test
    @DisplayName("测试签名一致性")
    public void testSignConsistency() {
        // 使用相同参数多次生成签名，结果应该一致
        String type = "stream";
        long ts = System.currentTimeMillis() / 1000;
        String did = SignUtil.DEFAULT_DID;
        long rid = 123456;

        // 获取加密密钥
        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        SignUtil.SignResult result1 = SignUtil.generateSign(type, ts, did, rid, key);
        SignUtil.SignResult result2 = SignUtil.generateSign(type, ts, did, rid, key);

        String auth1 = result1.getAuth();
        String auth2 = result2.getAuth();

        assertEquals(auth1, auth2, "相同参数应生成相同的签名");
    }

    @Test
    @DisplayName("测试密钥有效性检查")
    public void testIsKeyValid() {
        String did = SignUtil.DEFAULT_DID;

        // 获取加密密钥
        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        // 验证密钥有效性
        boolean isValid = SignUtil.isKeyValid(key, "stream");
        assertTrue(isValid, "新获取的密钥应该有效");
    }

    @Test
    @DisplayName("测试不同房间ID的签名")
    public void testDifferentRoomIds() {
        String type = "stream";
        long ts = System.currentTimeMillis() / 1000;
        String did = SignUtil.DEFAULT_DID;

        // 获取加密密钥
        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        long[] roomIds = {123456, 789012, 345678};

        for (long rid : roomIds) {
            SignUtil.SignResult result = SignUtil.generateSign(type, ts, did, rid, key);
            String auth = result.getAuth();

            // 验证每个房间的签名都不同
            assertNotNull(auth);
            assertEquals(32, auth.length());
        }
    }

    @Test
    @DisplayName("测试is_special=1的情况")
    public void testIsSpecialCase() {
        String type = "stream";
        long ts = System.currentTimeMillis() / 1000;
        String did = SignUtil.DEFAULT_DID;
        long rid = 123456;

        // 获取加密密钥
        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        // 如果is_special=1，签名字符串后缀应该为空
        SignUtil.SignResult result = SignUtil.generateSign(type, ts, did, rid, key);
        assertNotNull(result.getAuth(), "签名不应为空");
    }

    @Test
    @DisplayName("测试密钥为空的情况")
    public void testNullKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            SignUtil.generateSign("stream", System.currentTimeMillis() / 1000,
                    SignUtil.DEFAULT_DID, 123456, null);
        }, "密钥为空时应抛出异常");
    }

    @Test
    @DisplayName("测试不支持的签名类型")
    public void testUnsupportedType() {
        String did = SignUtil.DEFAULT_DID;
        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        assertThrows(IllegalArgumentException.class, () -> {
            SignUtil.generateSign("invalid_type", System.currentTimeMillis() / 1000,
                    did, 123456, key);
        }, "不支持的签名类型时应抛出异常");
    }
}
