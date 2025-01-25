package tech.ordinaryroad.live.chat.client.codec.douyu.api;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.codec.douyu.msg.dto.GiftPropSingle;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
//        List<IRoomLiveStreamInfo> roomLiveStreamUrls = DouyuApis.roomInit(6512L, null, null).getRoomLiveStreamUrls();
        Assert.notEmpty(roomLiveStreamUrls);
    }
}