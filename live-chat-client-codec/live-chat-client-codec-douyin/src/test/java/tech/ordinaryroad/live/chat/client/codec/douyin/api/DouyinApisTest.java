package tech.ordinaryroad.live.chat.client.codec.douyin.api;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * @author mjz
 * @date 2024/1/3
 */
class DouyinApisTest {

    @Test
    void roomInit() {
        DouyinApis.roomInit("886419461662");
    }

    @Test
    void testRoomTitle() {
        Assert.notBlank(DouyinApis.roomInit(886419461662L).getRoomTitle());
        Assert.notBlank(DouyinApis.roomInit("renyixu1989").getRoomTitle());
        Assert.notBlank(DouyinApis.roomInit("358618109921").getRoomTitle());
        Assert.notBlank(DouyinApis.roomInit("47761745807").getRoomTitle());
        Assert.notBlank(DouyinApis.roomInit("646454278948").getRoomTitle());
    }
}