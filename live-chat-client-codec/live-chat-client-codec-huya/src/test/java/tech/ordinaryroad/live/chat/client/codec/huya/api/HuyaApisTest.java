package tech.ordinaryroad.live.chat.client.codec.huya.api;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author mjz
 * @date 2023/10/1
 */
class HuyaApisTest {

    @Test
    void roomInit() {
//        assertEquals(HuyaApis.roomInit(189201).getTtRoomData(), 3);
        assertThrows(BaseException.class, () -> HuyaApis.roomInit(-1));
    }

    @Test
    void testRoomTitle() {
        Assert.notBlank(HuyaApis.roomInit(189201, null).getRoomTitle());
        Assert.notBlank(HuyaApis.roomInit(333003, null).getRoomTitle());
        Assert.notBlank(HuyaApis.roomInit("bagea", null).getRoomTitle());
        Assert.notBlank(HuyaApis.roomInit("527988", null).getRoomTitle());
        Assert.notBlank(HuyaApis.roomInit(1995, null).getRoomTitle());
        Assert.notBlank(HuyaApis.roomInit(116, null).getRoomTitle());
    }
}