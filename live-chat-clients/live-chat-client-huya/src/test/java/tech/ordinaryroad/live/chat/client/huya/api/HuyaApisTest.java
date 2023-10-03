package tech.ordinaryroad.live.chat.client.huya.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

/**
 * @author mjz
 * @date 2023/10/1
 */
class HuyaApisTest {

    @Test
    void roomInit() {
        JsonNode jsonNode = HuyaApis.roomInit(189201);
        jsonNode.size();
    }
}