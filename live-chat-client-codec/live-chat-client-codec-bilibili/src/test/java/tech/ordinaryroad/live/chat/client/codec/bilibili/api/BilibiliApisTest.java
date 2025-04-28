package tech.ordinaryroad.live.chat.client.codec.bilibili.api;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author mjz
 * @date 2023/9/7
 */
class BilibiliApisTest {

    @Test
    void sendMsg() {
        String cookie = System.getenv("cookie");
        BilibiliApis.sendMsg("666", 545068, cookie);
    }

    @Test
    void sendMsgWithReply() {
        String cookie = System.getenv("cookie");
        long replyUid = 0L;
        BilibiliApis.sendMsg("666666A", 545068, replyUid, cookie);
    }

    @Test
    void getRoomPlayInfo() {
        assertEquals(545068, BilibiliApis.getRoomPlayInfo(7777, null).getRoom_id());
        assertEquals(7734200, BilibiliApis.getRoomPlayInfo(6, null).getRoom_id());
    }

    @Test
    void testRoomTitle() {
        Assert.notBlank(BilibiliApis.roomInit(7777, null).getRoomTitle());
        Assert.notBlank(BilibiliApis.roomInit(6, null).getRoomTitle());
        Assert.notBlank(BilibiliApis.roomInit(666, null).getRoomTitle());
        Assert.notBlank(BilibiliApis.roomInit(30951561, null).getRoomTitle());
        Assert.notBlank(BilibiliApis.roomInit(1964561642, null).getRoomTitle());
        Assert.notBlank(BilibiliApis.roomInit(26103248, null).getRoomTitle());
    }

    @Test
    void testRoomLiveStatus() {
        assertEquals(RoomLiveStatusEnum.LIVING, BilibiliApis.roomInit(6, null).getRoomLiveStatus());
    }

    @Test
    void testRoomLiveStreamUrls() {
        List<IRoomLiveStreamInfo> roomLiveStreamUrls = BilibiliApis.roomInit(6, null).getRoomLiveStreamUrls();
        assertNotEquals(0, roomLiveStreamUrls.size());
    }
}