package tech.ordinaryroad.live.chat.client.bilibili.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void getRoomPlayInfo() {
        assertEquals(545068, BilibiliApis.getRoomPlayInfo(7777, null).getRoom_id());
        assertEquals(7734200, BilibiliApis.getRoomPlayInfo(6, null).getRoom_id());
    }
}