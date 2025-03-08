/*
 * MIT License
 *
 * Copyright (c) 2023 OrdinaryRoad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tech.ordinaryroad.live.chat.client.example.client.controller;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.servers.netty.client.base.BaseNettyClient;

import java.util.Map;

/**
 * @author mjz
 * @date 2023/8/21
 */
@RestController
@RequestMapping("client")
public class LiveChatClientController {

    @Autowired
    Map<String, BaseNettyClient> clientMap;

    @GetMapping("connect")
    public void connect(@RequestParam String platform) {
        getClient(platform).connect();
    }

    @GetMapping("autoReconnect/{autoReconnect}")
    public boolean autoReconnect(@RequestParam String platform, @PathVariable Boolean autoReconnect) {
        getClient(platform).getConfig().setAutoReconnect(autoReconnect);
        return getClient(platform).getConfig().isAutoReconnect();
    }

    @GetMapping("roomId/{roomId}")
    public Object roomId(@RequestParam String platform, @PathVariable String roomId) {
        getClient(platform).getConfig().setRoomId(roomId);
        return getClient(platform).getConfig().getRoomId();
    }

    @GetMapping("disconnect/{cancelReconnect}")
    public void disconnect(@RequestParam String platform, @PathVariable Boolean cancelReconnect) {
        getClient(platform).disconnect(cancelReconnect);
    }

    @GetMapping("cookie")
    public String cookie(@RequestParam String platform, @RequestParam String cookie) {
        getClient(platform).getConfig().setCookie(cookie);
        return getClient(platform).getConfig().getCookie();
    }

    @GetMapping("sendDanmu/{danmu}")
    public void sendDanmu(@RequestParam String platform, @PathVariable String danmu) {
        getClient(platform).sendDanmu(danmu + RandomUtil.randomNumbers(1));
    }

    private <Client extends BaseNettyClient> Client getClient(String platform) {
        String key = platform + "LiveChatClient";
        if (!clientMap.containsKey(key)) {
            throw new BaseException("暂不支持 " + platform);
        }
        return (Client) clientMap.get(key);
    }
}
