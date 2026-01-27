package tech.ordinaryroad.live.chat.client.codec.douyu.api;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.dto.EncryptionKey;
import tech.ordinaryroad.live.chat.client.codec.douyu.msg.dto.GiftPropSingle;
import tech.ordinaryroad.live.chat.client.codec.douyu.util.SignUtil;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mjz
 * @date 2023/8/30
 */
class DouyuApisTest {

    @Test
    void getRealRoomId() {
        assertEquals(2947432, DouyuApis.getRealRoomId(92000));
        assertEquals(3168536, DouyuApis.getRealRoomId(3168536));
        assertEquals(290935, DouyuApis.getRealRoomId(22222));
        assertEquals(290935, DouyuApis.getRealRoomId(290935));
        assertEquals(520, DouyuApis.getRealRoomId(520));
        assertThrows(RuntimeException.class, () -> DouyuApis.getRealRoomId(-1));
    }

    @Test
    void getGiftInfo() {
        GiftPropSingle giftByPid = DouyuApis.getGiftPropSingleByPid("4");
        assertEquals("赞", giftByPid.getName());
    }

    @Test
    void testRoomTitle() {
        Assert.notBlank(DouyuApis.roomInit(74751L, null).getRoomTitle());
        Assert.notBlank(DouyuApis.roomInit(22222L, null).getRoomTitle());
        Assert.notBlank(DouyuApis.roomInit(6073358L, null).getRoomTitle());
        Assert.notBlank(DouyuApis.roomInit(6525105L, null).getRoomTitle());
    }

    @Test
    void testRoomLiveStatus() {
        assertEquals(RoomLiveStatusEnum.STOPPED, DouyuApis.roomInit(288016L, null).getRoomLiveStatus());
    }

    @Test
    void testRoomLiveStreamUrls() {
        List<IRoomLiveStreamInfo> roomLiveStreamUrls = DouyuApis.roomInit(288016L, null, null).getRoomLiveStreamUrls();
        Assert.notEmpty(roomLiveStreamUrls);
    }

    @Test
    @DisplayName("测试获取加密密钥")
    void testGetEncryptionKey() {
        String did = SignUtil.DEFAULT_DID;

        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        assertNotNull(key, "加密密钥不应为空");
        assertNotNull(key.getEncData(), "enc_data不应为空");
        assertNotNull(key.getKey(), "key不应为空");
        assertNotNull(key.getRandStr(), "rand_str不应为空");
        assertNotNull(key.getEncTime(), "enc_time不应为空");
        assertNotNull(key.getExpireAt(), "expire_at不应为空");
    }

    @Test
    @DisplayName("测试加密密钥缓存功能")
    void testEncryptionKeyCache() {
        String did = SignUtil.DEFAULT_DID;

        // 清空缓存
        DouyuApis.encryptionKeyCache.clear();

        // 第一次获取（应该从服务器获取）
        long startTime1 = System.currentTimeMillis();
        EncryptionKey key1 = DouyuApis.getEncryptionKey(did);
        long time1 = System.currentTimeMillis() - startTime1;
        assertNotNull(key1, "第一次获取的密钥不应为空");

        // 第二次获取（应该从缓存获取）
        long startTime2 = System.currentTimeMillis();
        EncryptionKey key2 = DouyuApis.getEncryptionKey(did);
        long time2 = System.currentTimeMillis() - startTime2;
        assertNotNull(key2, "第二次获取的密钥不应为空");

        // 验证是同一个对象（缓存返回的）
        assertSame(key1, key2, "应该返回缓存的同一个对象");

        // 验证缓存确实有效（第二次应该更快）
        assertTrue(time2 < time1, "缓存获取应该比服务器获取快");
    }

    @Test
    @DisplayName("测试getH5PlayV1请求（使用默认设备ID）")
    @SneakyThrows
    void testGetH5PlayV1WithDefaultDid() {
        // 使用一个已知的房间ID进行测试
        long roomId = 288016L;

        JsonNode result = DouyuApis.getH5PlayV1(roomId);

        assertNotNull(result, "返回结果不应为空");
        // 检查是否包含常见的字段
        assertTrue(result.has("rtmp_url") || result.has("player_1") || result.has("rtmp_live"),
                "返回结果应包含流地址相关字段");
    }

    @Test
    @DisplayName("测试getH5PlayV1请求（指定设备ID和密钥）")
    @SneakyThrows
    void testGetH5PlayV1WithKey() {
        String did = SignUtil.DEFAULT_DID;
        long roomId = 288016L;

        // 先获取密钥
        EncryptionKey key = DouyuApis.getEncryptionKey(did);

        // 使用密钥发送请求
        JsonNode result = DouyuApis.getH5PlayV1(roomId, did, key);

        assertNotNull(result, "返回结果不应为空");
        assertTrue(result.has("rtmp_url") || result.has("player_1") || result.has("rtmp_live"),
                "返回结果应包含流地址相关字段");
    }

    @Test
    @DisplayName("测试签名和请求的完整流程")
    @SneakyThrows
    void testCompleteSignAndRequestFlow() {
        String did = SignUtil.DEFAULT_DID;
        long roomId = 288016L;

        // 1. 获取加密密钥
        EncryptionKey key = DouyuApis.getEncryptionKey(did);
        assertNotNull(key, "密钥不应为空");

        // 2. 生成时间戳
        long ts = System.currentTimeMillis() / 1000;

        // 3. 生成签名
        SignUtil.SignResult signResult = SignUtil.generateSign("stream", ts, did, roomId, key);
        assertNotNull(signResult, "签名结果不应为空");
        assertNotNull(signResult.getAuth(), "签名auth不应为空");

        // 4. 构建请求参数
        String signParams = SignUtil.buildRequestParams(signResult, did);
        assertNotNull(signParams, "请求参数不应为空");
        assertTrue(signParams.contains("auth="), "请求参数应包含auth");

        // 5. 发送请求
        JsonNode result = DouyuApis.getH5PlayV1(roomId, did, key);
        assertNotNull(result, "返回结果不应为空");
    }
}
