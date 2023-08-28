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

package tech.ordinaryroad.bilibili.live.chat.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient;

/**
 * @author mjz
 * @date 2023/8/21
 */
@RestController
@RequestMapping("client")
public class LiveChatClientController {

    @Autowired
    private BilibiliLiveChatClient client;

    @GetMapping("connect")
    public void connect() {
        client.connect();
    }

    @GetMapping("autoReconnect/{autoReconnect}")
    public void autoReconnect(@PathVariable Boolean autoReconnect) {
        client.getConfig().setAutoReconnect(autoReconnect);
    }

    @GetMapping("roomId/{roomId}")
    public void roomId(@PathVariable Long roomId) {
        client.getConfig().setRoomId(roomId);
    }

    @GetMapping("disconnect/{cancelReconnect}")
    public void disconnect(@PathVariable Boolean cancelReconnect) {
        client.disconnect(cancelReconnect);
    }

    @GetMapping("cookie")
    public void cookie(@RequestParam String cookie) {
        client.getConfig().setCookie(cookie);
    }

}
