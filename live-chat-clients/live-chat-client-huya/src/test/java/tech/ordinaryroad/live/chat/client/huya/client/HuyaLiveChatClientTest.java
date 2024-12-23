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

package tech.ordinaryroad.live.chat.client.huya.client;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.qq.tars.protocol.tars.TarsInputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.codec.huya.constant.HuyaCmdEnum;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.MessageNoticeMsg;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.SendItemSubBroadcastPacketMsg;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.VipEnterBannerMsg;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.WSPushMessage;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.dto.WSMsgItem;
import tech.ordinaryroad.live.chat.client.codec.huya.util.HuyaCodecUtil;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.huya.config.HuyaLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.huya.listener.IHuyaMsgListener;
import tech.ordinaryroad.live.chat.client.huya.netty.handler.HuyaBinaryFrameHandler;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author mjz
 * @date 2023/9/5
 */
@Slf4j
class HuyaLiveChatClientTest {

    static Object lock = new Object();
    HuyaLiveChatClient client;

    @Test
    void example() throws InterruptedException {
        HuyaLiveChatClientConfig config = HuyaLiveChatClientConfig.builder()

//                .forwardWebsocketUri("ws://127.0.0.1:8080/websocket")

                .roomId(353322)
                .roomId(390001)
                .roomId(527988)
                .roomId(1995)
                .roomId(116)

                .roomId("bagea")

                .roomId(333003)
//                .roomId(575757)

                .roomId(29785782)

                .roomId(333003)
                // bagea
                .roomId(189201)
                .roomId("bagea")
                .build();

        client = new HuyaLiveChatClient(config, new IHuyaMsgListener() {
            @Override
            public void onDanmuMsg(HuyaBinaryFrameHandler binaryFrameHandler, MessageNoticeMsg msg) {
                log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onGiftMsg(HuyaBinaryFrameHandler binaryFrameHandler, SendItemSubBroadcastPacketMsg msg) {
                long lPayTotal = msg.getLPayTotal();
                if (lPayTotal != 0) {
                    int giftPrice = msg.getGiftPrice();
                }
                log.info("{} 收到礼物 {}({}) {} {}({})x{}({})", binaryFrameHandler.getRoomId(), msg.getUsername(), msg.getUid(), "赠送", msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice());
            }

            @Override
            public void onEnterRoomMsg(HuyaBinaryFrameHandler binaryFrameHandler, VipEnterBannerMsg msg) {
                // 虎牙目前只支持监听VIP用户的入房消息
                log.info("{} {}({}) 进入直播间", msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid());
            }

            @Override
            public void onMsg(HuyaBinaryFrameHandler binaryFrameHandler, IMsg msg) {
                log.debug("{} 收到{}消息 {}", binaryFrameHandler.getRoomId(), msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(HuyaBinaryFrameHandler binaryFrameHandler, HuyaCmdEnum cmd, ICmdMsg<HuyaCmdEnum> cmdMsg) {
                switch (cmd) {
                    case BeginLiveNotice: {
                        // 开始直播
                        //18:16:59.622 [nioEventLoopGroup-2-1] INFO tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClientTest -- 29785782 收到CMD消息BeginLiveNotice {"smsg":"AmQypb8RDtEmBumfs+S5kDxABVkAAQoGAkFMHCJkMqW/MmQypb9CZDKlv1ZAMTY4MTA0MDgzMS0xNjgxMDQwODMxLTcyMjAwMTUzOTIzODU2NjI5NzYtMzM2MjIwNTExOC0xMDA1Ny1BLTAtMWYfaHR0cDovL2FsLmZsdi5odXlhLmNvbS9odXlhbGl2ZXYDZmx2hnF3c1NlY3JldD1mMGU4MzY0MjNjNDA5YzEzMzM4N2FhYzA1OGFhNzY3YiZ3c1RpbWU9NjVlZDg4OWImZm09UkZkeE9FSmpTak5vTmtSS2REWlVXVjhrTUY4a01WOGtNbDhrTXclM0QlM0QmZnM9YmdjdJYfaHR0cDovL2FsLmhscy5odXlhLmNvbS9odXlhbGl2ZaYEbTN1OLZxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3TAA9AB4GTwD2TwEGT5EQz8EvYTH2h0dHA6Ly9hbC5wMnAuaHV5YS5jb20vaHV5YWxpdmX2FAVzbGljZfYVcXdzU2VjcmV0PWYwZTgzNjQyM2M0MDljMTMzMzg3YWFjMDU4YWE3NjdiJndzVGltZT02NWVkODg5YiZmbT1SRmR4T0VKalNqTm9Oa1JLZERaVVdWOGtNRjhrTVY4a01sOGtNdyUzRCUzRCZmcz1iZ2N08BYC/Bf5GAz4GQz8GvwbC2kAAQYCQUxzZe2IluaXt0OMnKy8xhM/U3RydWdnbGXjgpvmi7zmkI8/0nIb+gbs/A/wEAH5EQABCgYG6LaF5riFHCw8QP9QAWx2AIYAlgCmALzMC/YSDOmaj+S+v+WQrOWQrPwT9hRgaHR0cDovL2h1eWFpbWcubXNzdGF0aWMuY29tL2F2YXRhci8xMDE4LzFlL2ZjYzRhMjNkZjlmOTc0NmZiNTEwNTcxOTY3NjBjY18xODBfMTM1LmpwZz8xNDgyNjYzMzI58BUG9hYA9hcA8hhl7YiX8hlkMqW/8hpkMqW/9hsA8BwB8B0I+B4ADgYKQXBwVmVyc2lvbhYHMTIuMC4yMgYIQmFzZWJhbmQWElY4MTYuMC4yNC4yLjI2LkRFVgYKRGV2aWNlTmFtZRYJTTIxMDJKMlNDBgpIVVlBX01BSVhVFgEyBhJIdXlhQXVkaW9BQ1FFbmFibGUWATEGCExpdmVNb2RlFgprU2NyZWVuQ0FQBgdOZXR3b3JrFgR3aWZpBgpTREtWZXJzaW9uFgAGDVN5c3RlbVZlcnNpb24WAjE0BghUaW1lWm9uZRYNQXNpYS9TaGFuZ2hhaQYQZW5hYmxlX211bHRpbGluaxYBMAYJZW5hYmxlX3BrFgEwBg9rZXlfY2FzdGVyX2xpdmUWATEGB3JlYWxfdWEWHGFkcl9saXZlX2tpd2kmNS40MC4zMCZ4aWFvbWn8H/IgAcZ+tvwh8CIB/CP8JPwl","lmsgId":1374002143939868673,"cmd":"8000","cmdEnum":"BeginLiveNotice","luri":8000}
                        //18:16:59.622 [nioEventLoopGroup-2-1] DEBUG tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClientTest -- 29785782 收到其他CMD消息 BeginLiveNotice
                        //18:16:59.622 [nioEventLoopGroup-2-1] INFO tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClientTest -- 29785782 收到CMD消息BeginLiveNotice {"smsg":"AmQypb8RDtEmBumfs+S5kDxABVkAAgoGAkFMHCJkMqW/MmQypb9CZDKlv1ZAMTY4MTA0MDgzMS0xNjgxMDQwODMxLTcyMjAwMTUzOTIzODU2NjI5NzYtMzM2MjIwNTExOC0xMDA1Ny1BLTAtMWYfaHR0cDovL2FsLmZsdi5odXlhLmNvbS9odXlhbGl2ZXYDZmx2hnF3c1NlY3JldD1mMGU4MzY0MjNjNDA5YzEzMzM4N2FhYzA1OGFhNzY3YiZ3c1RpbWU9NjVlZDg4OWImZm09UkZkeE9FSmpTak5vTmtSS2REWlVXVjhrTUY4a01WOGtNbDhrTXclM0QlM0QmZnM9YmdjdJYfaHR0cDovL2FsLmhscy5odXlhLmNvbS9odXlhbGl2ZaYEbTN1OLZxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3TAA9AB4GTwD2TwEGT5EQz8EvYTH2h0dHA6Ly9hbC5wMnAuaHV5YS5jb20vaHV5YWxpdmX2FAVzbGljZfYVcXdzU2VjcmV0PWYwZTgzNjQyM2M0MDljMTMzMzg3YWFjMDU4YWE3NjdiJndzVGltZT02NWVkODg5YiZmbT1SRmR4T0VKalNqTm9Oa1JLZERaVVdWOGtNRjhrTVY4a01sOGtNdyUzRCUzRCZmcz1iZ2N08BYC/Bf5GAz4GQz8GvwbCwoGAkhTHCJkMqW/MmQypb9CZDKlv1ZAMTY4MTA0MDgzMS0xNjgxMDQwODMxLTcyMjAwMTUzOTIzODU2NjI5NzYtMzM2MjIwNTExOC0xMDA1Ny1BLTAtMWYaaHR0cDovL2hzLmZsdi5odXlhLmNvbS9zcmN2A2ZsdoZxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3SWGmh0dHA6Ly9ocy5obHMuaHV5YS5jb20vc3JjpgRtM3U4tnF3c1NlY3JldD1mMGU4MzY0MjNjNDA5YzEzMzM4N2FhYzA1OGFhNzY3YiZ3c1RpbWU9NjVlZDg4OWImZm09UkZkeE9FSmpTak5vTmtSS2REWlVXVjhrTUY4a01WOGtNbDhrTXclM0QlM0QmZnM9YmdjdMAO0AHs/A/8EPkRDPwS9hMfaHR0cDovL2hzLnAycC5odXlhLmNvbS9odXlhbGl2ZfYUBXNsaWNl9hVxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3T8FvwX+RgM+BkM/Br8GwtpAAIGAkFMBgJIU3Nl7YiW5pe3Q4ycrLzGEz9TdHJ1Z2dsZeOCm+aLvOaQjz/Schv6Buz8D/AQAfkRAAEKBgbotoXmuIUcLDxA/1ABbHYAhgCWAKYAvMwL9hIM6ZqP5L6/5ZCs5ZCs/BP2FGBodHRwOi8vaHV5YWltZy5tc3N0YXRpYy5jb20vYXZhdGFyLzEwMTgvMWUvZmNjNGEyM2RmOWY5NzQ2ZmI1MTA1NzE5Njc2MGNjXzE4MF8xMzUuanBnPzE0ODI2NjMzMjnwFQb2FgD2FwDyGGXtiJfyGWQypb/yGmQypb/2GwDwHAHwHQj4HgAOBgpBcHBWZXJzaW9uFgcxMi4wLjIyBghCYXNlYmFuZBYSVjgxNi4wLjI0LjIuMjYuREVWBgpEZXZpY2VOYW1lFglNMjEwMkoyU0MGCkhVWUFfTUFJWFUWATIGEkh1eWFBdWRpb0FDUUVuYWJsZRYBMQYITGl2ZU1vZGUWCmtTY3JlZW5DQVAGB05ldHdvcmsWBHdpZmkGClNES1ZlcnNpb24WAAYNU3lzdGVtVmVyc2lvbhYCMTQGCFRpbWVab25lFg1Bc2lhL1NoYW5naGFpBhBlbmFibGVfbXVsdGlsaW5rFgEwBgllbmFibGVfcGsWATAGD2tleV9jYXN0ZXJfbGl2ZRYBMQYHcmVhbF91YRYcYWRyX2xpdmVfa2l3aSY1LjQwLjMwJnhpYW9tafwf8iABxn62/CHwIgH8I/wk/CU=","lmsgId":1374002144099236864,"cmd":"8000","cmdEnum":"BeginLiveNotice","luri":8000}
                        //18:16:59.623 [nioEventLoopGroup-2-1] DEBUG tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClientTest -- 29785782 收到其他CMD消息 BeginLiveNotice
                        //18:16:59.623 [nioEventLoopGroup-2-1] INFO tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClientTest -- 29785782 收到CMD消息BeginLiveNotice {"smsg":"AmQypb8RDtEmBumfs+S5kDxABVkAAgoGAkFMHCJkMqW/MmQypb9CZDKlv1ZAMTY4MTA0MDgzMS0xNjgxMDQwODMxLTcyMjAwMTUzOTIzODU2NjI5NzYtMzM2MjIwNTExOC0xMDA1Ny1BLTAtMWYfaHR0cDovL2FsLmZsdi5odXlhLmNvbS9odXlhbGl2ZXYDZmx2hnF3c1NlY3JldD1mMGU4MzY0MjNjNDA5YzEzMzM4N2FhYzA1OGFhNzY3YiZ3c1RpbWU9NjVlZDg4OWImZm09UkZkeE9FSmpTak5vTmtSS2REWlVXVjhrTUY4a01WOGtNbDhrTXclM0QlM0QmZnM9YmdjdJYfaHR0cDovL2FsLmhscy5odXlhLmNvbS9odXlhbGl2ZaYEbTN1OLZxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3TAA9AB4GTwD2TwEGT5EQz8EvYTH2h0dHA6Ly9hbC5wMnAuaHV5YS5jb20vaHV5YWxpdmX2FAVzbGljZfYVcXdzU2VjcmV0PWYwZTgzNjQyM2M0MDljMTMzMzg3YWFjMDU4YWE3NjdiJndzVGltZT02NWVkODg5YiZmbT1SRmR4T0VKalNqTm9Oa1JLZERaVVdWOGtNRjhrTVY4a01sOGtNdyUzRCUzRCZmcz1iZ2N08BYC/Bf5GAz4GQz8GvwbCwoGAkhTHCJkMqW/MmQypb9CZDKlv1ZAMTY4MTA0MDgzMS0xNjgxMDQwODMxLTcyMjAwMTUzOTIzODU2NjI5NzYtMzM2MjIwNTExOC0xMDA1Ny1BLTAtMWYaaHR0cDovL2hzLmZsdi5odXlhLmNvbS9zcmN2A2ZsdoZxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3SWGmh0dHA6Ly9ocy5obHMuaHV5YS5jb20vc3JjpgRtM3U4tnF3c1NlY3JldD1mMGU4MzY0MjNjNDA5YzEzMzM4N2FhYzA1OGFhNzY3YiZ3c1RpbWU9NjVlZDg4OWImZm09UkZkeE9FSmpTak5vTmtSS2REWlVXVjhrTUY4a01WOGtNbDhrTXclM0QlM0QmZnM9YmdjdMAO0AHs/A/8EPkRDPwS9hMfaHR0cDovL2hzLnAycC5odXlhLmNvbS9odXlhbGl2ZfYUBXNsaWNl9hVxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3T8FvwX+RgM+BkM/Br8GwtpAAMGAkFMBgJIVwYCSFNzZe2IluaXt0OMnKy8xhM/U3RydWdnbGXjgpvmi7zmkI8/0nIb+gbs/A/wEAH5EQABCgYG6LaF5riFHCw8QP9QAWx2AIYAlgCmALzMC/YSDOmaj+S+v+WQrOWQrPwT9hRgaHR0cDovL2h1eWFpbWcubXNzdGF0aWMuY29tL2F2YXRhci8xMDE4LzFlL2ZjYzRhMjNkZjlmOTc0NmZiNTEwNTcxOTY3NjBjY18xODBfMTM1LmpwZz8xNDgyNjYzMzI58BUG9hYA9hcA8hhl7YiX8hlkMqW/8hpkMqW/9hsA8BwB8B0I+B4ADgYKQXBwVmVyc2lvbhYHMTIuMC4yMgYIQmFzZWJhbmQWElY4MTYuMC4yNC4yLjI2LkRFVgYKRGV2aWNlTmFtZRYJTTIxMDJKMlNDBgpIVVlBX01BSVhVFgEyBhJIdXlhQXVkaW9BQ1FFbmFibGUWATEGCExpdmVNb2RlFgprU2NyZWVuQ0FQBgdOZXR3b3JrFgR3aWZpBgpTREtWZXJzaW9uFgAGDVN5c3RlbVZlcnNpb24WAjE0BghUaW1lWm9uZRYNQXNpYS9TaGFuZ2hhaQYQZW5hYmxlX211bHRpbGluaxYBMAYJZW5hYmxlX3BrFgEwBg9rZXlfY2FzdGVyX2xpdmUWATEGB3JlYWxfdWEWHGFkcl9saXZlX2tpd2kmNS40MC4zMCZ4aWFvbWn8H/IgAcZ+tvwh8CIB/CP8JPwl","lmsgId":1374002144132795392,"cmd":"8000","cmdEnum":"BeginLiveNotice","luri":8000}
                        //18:16:59.624 [nioEventLoopGroup-2-1] DEBUG tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClientTest -- 29785782 收到其他CMD消息 BeginLiveNotice
                        //18:16:59.624 [nioEventLoopGroup-2-1] INFO tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClientTest -- 29785782 收到CMD消息BeginLiveNotice {"smsg":"AmQypb8RDtEmBumfs+S5kDxABVkAAgoGAkFMHCJkMqW/MmQypb9CZDKlv1ZAMTY4MTA0MDgzMS0xNjgxMDQwODMxLTcyMjAwMTUzOTIzODU2NjI5NzYtMzM2MjIwNTExOC0xMDA1Ny1BLTAtMWYfaHR0cDovL2FsLmZsdi5odXlhLmNvbS9odXlhbGl2ZXYDZmx2hnF3c1NlY3JldD1mMGU4MzY0MjNjNDA5YzEzMzM4N2FhYzA1OGFhNzY3YiZ3c1RpbWU9NjVlZDg4OWImZm09UkZkeE9FSmpTak5vTmtSS2REWlVXVjhrTUY4a01WOGtNbDhrTXclM0QlM0QmZnM9YmdjdJYfaHR0cDovL2FsLmhscy5odXlhLmNvbS9odXlhbGl2ZaYEbTN1OLZxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3TAA9AB4GTwD2TwEGT5EQz8EvYTH2h0dHA6Ly9hbC5wMnAuaHV5YS5jb20vaHV5YWxpdmX2FAVzbGljZfYVcXdzU2VjcmV0PWYwZTgzNjQyM2M0MDljMTMzMzg3YWFjMDU4YWE3NjdiJndzVGltZT02NWVkODg5YiZmbT1SRmR4T0VKalNqTm9Oa1JLZERaVVdWOGtNRjhrTVY4a01sOGtNdyUzRCUzRCZmcz1iZ2N08BYC/Bf5GAz4GQz8GvwbCwoGAkhTHCJkMqW/MmQypb9CZDKlv1ZAMTY4MTA0MDgzMS0xNjgxMDQwODMxLTcyMjAwMTUzOTIzODU2NjI5NzYtMzM2MjIwNTExOC0xMDA1Ny1BLTAtMWYaaHR0cDovL2hzLmZsdi5odXlhLmNvbS9zcmN2A2ZsdoZxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3SWGmh0dHA6Ly9ocy5obHMuaHV5YS5jb20vc3JjpgRtM3U4tnF3c1NlY3JldD1mMGU4MzY0MjNjNDA5YzEzMzM4N2FhYzA1OGFhNzY3YiZ3c1RpbWU9NjVlZDg4OWImZm09UkZkeE9FSmpTak5vTmtSS2REWlVXVjhrTUY4a01WOGtNbDhrTXclM0QlM0QmZnM9YmdjdMAO0AHs/A/8EPkRDPwS9hMfaHR0cDovL2hzLnAycC5odXlhLmNvbS9odXlhbGl2ZfYUBXNsaWNl9hVxd3NTZWNyZXQ9ZjBlODM2NDIzYzQwOWMxMzMzODdhYWMwNThhYTc2N2Imd3NUaW1lPTY1ZWQ4ODliJmZtPVJGZHhPRUpqU2pOb05rUktkRFpVV1Y4a01GOGtNVjhrTWw4a013JTNEJTNEJmZzPWJnY3T8FvwX+RgM+BkM/Br8GwtpAAQGAkFMBgJUWAYCSFcGAkhTc2XtiJbml7dDjJysvMYTP1N0cnVnZ2xl44Kb5ou85pCPP9JyG/oG7PwP8BAB+REAAQoGBui2hea4hRwsPED/UAFsdgCGAJYApgC8zAv2Egzpmo/kvr/lkKzlkKz8E/YUYGh0dHA6Ly9odXlhaW1nLm1zc3RhdGljLmNvbS9hdmF0YXIvMTAxOC8xZS9mY2M0YTIzZGY5Zjk3NDZmYjUxMDU3MTk2NzYwY2NfMTgwXzEzNS5qcGc/MTQ4MjY2MzMyOfAVBvYWAPYXAPIYZe2Il/IZZDKlv/IaZDKlv/YbAPAcAfAdCPgeAA4GCkFwcFZlcnNpb24WBzEyLjAuMjIGCEJhc2ViYW5kFhJWODE2LjAuMjQuMi4yNi5ERVYGCkRldmljZU5hbWUWCU0yMTAySjJTQwYKSFVZQV9NQUlYVRYBMgYSSHV5YUF1ZGlvQUNRRW5hYmxlFgExBghMaXZlTW9kZRYKa1NjcmVlbkNBUAYHTmV0d29yaxYEd2lmaQYKU0RLVmVyc2lvbhYABg1TeXN0ZW1WZXJzaW9uFgIxNAYIVGltZVpvbmUWDUFzaWEvU2hhbmdoYWkGEGVuYWJsZV9tdWx0aWxpbmsWATAGCWVuYWJsZV9waxYBMAYPa2V5X2Nhc3Rlcl9saXZlFgExBgdyZWFsX3VhFhxhZHJfbGl2ZV9raXdpJjUuNDAuMzAmeGlhb21p/B/yIAHGfrb8IfAiAfwj/CT8JQ==","lmsgId":1374002144233469953,"cmd":"8000","cmdEnum":"BeginLiveNotice","luri":8000}
                        break;
                    }
                    case CheckRoomStatus: {
                        // 点击关闭直播
                        //18:17:33.446 [nioEventLoopGroup-2-1] INFO tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClientTest -- 29785782 收到CMD消息CheckRoomStatus {"smsg":"CQw=","lmsgId":1374002423406325760,"cmd":"6340","cmdEnum":"CheckRoomStatus","luri":6340}
                        break;
                    }
                }
                log.info("{} 收到CMD消息{} {}", binaryFrameHandler.getRoomId(), cmd, cmdMsg);
            }

            @Override
            public void onOtherCmdMsg(HuyaBinaryFrameHandler binaryFrameHandler, HuyaCmdEnum cmd, ICmdMsg<HuyaCmdEnum> cmdMsg) {
                byte[] dataBytes;
                if (cmdMsg instanceof WSPushMessage) {
                    WSPushMessage wsPushMessage = (WSPushMessage) cmdMsg;
                    dataBytes = wsPushMessage.getDataBytes();
                } else if (cmdMsg instanceof WSMsgItem) {
                    WSMsgItem wsMsgItem = (WSMsgItem) cmdMsg;
                    dataBytes = wsMsgItem.getSMsg();
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("非HuyaCmdMsg {}", cmdMsg.getClass());
                    }
                    return;
                }
                TarsInputStream tarsInputStream = HuyaCodecUtil.newUtf8TarsInputStream(dataBytes);
                String string = new String(tarsInputStream.getBs().array(), StandardCharsets.UTF_8);
                log.debug("{} 收到其他CMD消息 {}", binaryFrameHandler.getRoomId(), cmd);
                // log.debug("tars: {}", string);
            }

            @Override
            public void onUnknownCmd(HuyaBinaryFrameHandler binaryFrameHandler, String cmdString, IMsg msg) {
                log.debug("{} 收到未知CMD消息 {}", binaryFrameHandler.getRoomId(), cmdString);
            }
        });
        client.addStatusChangeListener((evt, oldStatus, newStatus) -> {
            if (newStatus == ClientStatusEnums.CONNECTED) {
                log.warn("{} 已连接", client.getConfig().getRoomId());
                log.warn("直播间标题：{}", client.getRoomInitResult().getRoomTitle());
            }
        });

        client.connect();

        // 防止测试时直接退出
        while (true) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    @Test
    void sendDanmuTest() throws InterruptedException {
        String cookie = System.getenv("cookie");
        assertTrue(StrUtil.isNotBlank(cookie));
        log.error("cookie: {}", cookie);

        HuyaLiveChatClientConfig config = HuyaLiveChatClientConfig.builder()
                .cookie(cookie)
                .roomId(189201)
                .build();

        client = new HuyaLiveChatClient(config);
        client.connect(() -> {
            String danmu = "66666" + RandomUtil.randomNumber();
            log.info("连接成功，5s后发送弹幕{}", danmu);
            ThreadUtil.sleep(5000);
            client.sendDanmu(danmu);
        });
        client.addMsgListener(new IHuyaMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
//                log.info("收到消息{}", msg);
            }

            @Override
            public void onDanmuMsg(MessageNoticeMsg messageNoticeMsg) {
                log.info("收到弹幕{}:{}", messageNoticeMsg.getUsername(), messageNoticeMsg.getContent());
            }
        });

        // 防止测试时直接退出
        while (true) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

}