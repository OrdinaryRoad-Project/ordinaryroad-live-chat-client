package tech.ordinaryroad.live.chat.client.douyu.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author mjz
 * @date 2023/8/30
 */
class DouyuApisTest {

    @Test
    void getRealRoomId() {
        assertEquals(DouyuApis.getRealRoomId(92000), 2947432);
        assertEquals(DouyuApis.getRealRoomId(3168536), 3168536);
    }
}