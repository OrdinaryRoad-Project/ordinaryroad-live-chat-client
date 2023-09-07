package tech.ordinaryroad.live.chat.client.douyu.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author mjz
 * @date 2023/8/30
 */
class DouyuApisTest {

    @Test
    void getRealRoomId() {
        assertEquals(DouyuApis.getRealRoomId(92000), 2947432);
        assertEquals(DouyuApis.getRealRoomId(3168536), 3168536);
        assertEquals(DouyuApis.getRealRoomId(22222), 290935);
        assertEquals(DouyuApis.getRealRoomId(290935), 290935);
        assertEquals(DouyuApis.getRealRoomId(520), 520);
        assertThrows(RuntimeException.class, () -> DouyuApis.getRealRoomId(-1));
    }
}