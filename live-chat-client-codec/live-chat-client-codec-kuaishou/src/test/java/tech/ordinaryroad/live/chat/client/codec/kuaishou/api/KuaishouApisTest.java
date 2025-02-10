package tech.ordinaryroad.live.chat.client.codec.kuaishou.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.constant.RoomInfoGetTypeEnum;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.room.KuaishouRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomLiveStreamInfo;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author mjz
 * @date 2024/1/6
 */
@Slf4j
class KuaishouApisTest {

    @Test
    void allgifts() {
        Map<String, KuaishouApis.GiftInfo> allgifts = KuaishouApis.allgifts();
        assertNotEquals(0, allgifts.size());
    }

    @Test
    void getGiftInfoById() {
        KuaishouApis.GiftInfo giftInfoById = KuaishouApis.getGiftInfoById("1");
        assertEquals("荧光棒", giftInfoById.getGiftName());

    }

    @Test
    void sendComment() {
        String roomId = "KPL704668133";
        KuaishouRoomInitResult roomInitResult = KuaishouApis.roomInit(roomId);

        System.out.println(KuaishouApis.sendComment(System.getenv("cookie"),
                roomId,
                KuaishouApis.SendCommentRequest
                        .builder()
                        .liveStreamId(roomInitResult.getLiveStreamId())
                        .content("66666a")
                        .build()
        ));
    }

    @Test
    void testRoomTitle() {
        KuaishouApis.roomInit("KPL704668133", RoomInfoGetTypeEnum.NOT_COOKIE, null, null).getRoomTitle();
        KuaishouApis.roomInit("3x6pb6bcmjrarvs", RoomInfoGetTypeEnum.NOT_COOKIE, null, null).getRoomTitle();
        KuaishouApis.roomInit("t8888888", RoomInfoGetTypeEnum.NOT_COOKIE, null, null).getRoomTitle();
        KuaishouApis.roomInit("3x3gjx4jfca4zfs", RoomInfoGetTypeEnum.NOT_COOKIE, null, null).getRoomTitle();
        KuaishouApis.roomInit("3xkz5pb2kx3q4u6", RoomInfoGetTypeEnum.NOT_COOKIE, null, null).getRoomTitle();
        KuaishouApis.roomInit("kslibai66", RoomInfoGetTypeEnum.NOT_COOKIE, null, null).getRoomTitle();
    }

    @Test
    void testRoomLiveStatus() {
        assertEquals(RoomLiveStatusEnum.LIVING, KuaishouApis.roomInit("KPL704668133", RoomInfoGetTypeEnum.NOT_COOKIE, null, null).getRoomLiveStatus());
        assertEquals(RoomLiveStatusEnum.STOPPED, KuaishouApis.roomInit("t8888888", RoomInfoGetTypeEnum.NOT_COOKIE, null, null).getRoomLiveStatus());
    }

    @Test
    void testRoomLiveStreamUrls() {
        KuaishouRoomInitResult roomInitResult = KuaishouApis.roomInit("KPL704668133", RoomInfoGetTypeEnum.NOT_COOKIE, null, null);
        List<IRoomLiveStreamInfo> roomLiveStreamUrls = roomInitResult.getRoomLiveStreamUrls();
        assertNotEquals(0, roomLiveStreamUrls.size());
    }
}