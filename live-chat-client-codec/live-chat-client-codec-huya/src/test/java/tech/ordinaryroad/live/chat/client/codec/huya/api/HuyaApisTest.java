package tech.ordinaryroad.live.chat.client.codec.huya.api;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.util.OrJacksonUtil;

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

    @Test
    void testRoomLiveStatus() {
        System.out.println(HuyaApis.roomInit("bagea", null).getRoomLiveStatus());
    }

    @Test
    void testRoomLiveStreamUrls() throws JsonProcessingException {
        System.out.println(OrJacksonUtil.getInstance().writeValueAsString(HuyaApis.roomInit("bagea", "game_did=ouA5cOoS9BHyQbXbC7shM9rb9eoS58o-SPP; udb_guiddata=c61979e4a2ea4031b9a420a0c72b3702; udb_deviceid=w_819289226423468032; sdidshorttest=test; alphaValue=0.80; isInLiveRoom=true; videoBitRate=2000; sdidtest=0UnHUgv0_qmfD4KAKlwzhqaxCtay5GnESflkD4HWQgxcQZxPw4QmZhIBAgYWfBJY4fFwxpS97G1_EfGHlfwNl8CHxaHmrdrf1s-EBLGgm5srWVkn9LtfFJw_Qo4kgKr8OZHDqNnuwg612sGyflFn1dts_a7qhl4OXeU6xxqTPeT4Cka-9avAofRj-5YWz19dd; guid=0a7d0763cd01f3654a0179f07af0cb65; _qimei_uuid42=18c17141f291006a6109b6c367c946aab0ee626e06; _qimei_h38=2c45d9406109b6c367c946aa03000006518c17; hdid=a5d933b230f5bdaf32764ca50855b4841478b75f; __yamid_new=CB0B25947A600001CF9C95C369304FD0; __yamid_tt1=0.23880671242491247; SoundValue=0.32; huya_ua=wap&1.0.0&huya&adr; guid=0a7d0763cd01f3654a0179f07af0cb65; sdid=0UnHUgv0_qmfD4KAKlwzhqX3qL60GaIdeayQ79ILsmPx0Qpg0f4Iw3LqieEkcw4HAOTUzmr5TwOYAAQuDoEX4nmxGxe9qERGb7NniJUnQChbWVkn9LtfFJw_Qo4kgKr8OZHDqNnuwg612sGyflFn1dts_a7qhl4OXeU6xxqTPeT4Cka-9avAofRj-5YWz19dd; udb_passdata=3; _qimei_fingerprint=b0e5099d260f172d586fb805cc0f81cf; __yasmid=0.23880671242491247; _yasids=__rootsid%3DCB0E54E3F4B00001CA49153047A01412; Hm_lvt_51700b6c722f5bb4cf39906a596ea41f=1737097489,1737256196,1737256965,1737257345; Hm_lpvt_51700b6c722f5bb4cf39906a596ea41f=1737257345; HMACCOUNT=F943EC297A4FB9D2; huya_flash_rep_cnt=14; huya_web_rep_cnt=28").getRoomLiveStreamUrls()));
    }
}