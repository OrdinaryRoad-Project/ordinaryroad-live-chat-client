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

package tech.ordinaryroad.live.chat.client.bilibili.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliMsgListener;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.BilibiliApis;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.response.RoomPlayInfoResult;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliCmdEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.*;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseMsgListener;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.commons.client.IBaseLiveChatClient;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.commons.client.plugin.IPlugin;
import tech.ordinaryroad.live.chat.client.plugin.forward.ForwardMsgPlugin;
import tech.ordinaryroad.live.chat.client.websocket.client.WebSocketLiveChatClient;
import tech.ordinaryroad.live.chat.client.websocket.config.WebSocketLiveChatClientConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author mjz
 * @date 2023/8/26
 */
@Slf4j
class BilibiliLiveChatClientTest {

    static Object lock = new Object();
    BilibiliLiveChatClient client;

    @Test
    void example() throws InterruptedException {
        String cookie = System.getenv("cookie");
        log.error("cookie: {}", cookie);
        BilibiliLiveChatClientConfig config = BilibiliLiveChatClientConfig.builder()
                // TODO 浏览器Cookie
                .cookie(cookie)
                .roomId(21852)
                .roomId(7777)
                .roomId(26103248)
                .build();

        client = new BilibiliLiveChatClient(config, new IBilibiliMsgListener() {
            @Override
            public void onDanmuMsg(BilibiliBinaryFrameHandler binaryFrameHandler, DanmuMsgMsg msg) {
                IBilibiliMsgListener.super.onDanmuMsg(binaryFrameHandler, msg);
                log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onGiftMsg(BilibiliBinaryFrameHandler binaryFrameHandler, SendGiftMsg msg) {
                IBilibiliMsgListener.super.onGiftMsg(binaryFrameHandler, msg);
                log.info("{} 收到礼物 {} {}({}) {} {}({})x{}({})", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getData().getAction(), msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice());
            }

            @Override
            public void onSuperChatMsg(BilibiliBinaryFrameHandler binaryFrameHandler, SuperChatMessageMsg msg) {
                IBilibiliMsgListener.super.onSuperChatMsg(binaryFrameHandler, msg);
                log.info("{} 收到醒目留言 {}({})：{}", binaryFrameHandler.getRoomId(), msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onEnterRoomMsg(InteractWordMsg msg) {
                log.info("{} {}({}) 进入直播间", msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid());
            }

            @Override
            public void onLikeMsg(BilibiliBinaryFrameHandler binaryFrameHandler, LikeInfoV3ClickMsg msg) {
                IBilibiliMsgListener.super.onLikeMsg(binaryFrameHandler, msg);
                log.info("{} 收到点赞 [{}] {}({})x{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getClickCount());
            }

            @Override
            public void onLiveStatusMsg(BilibiliBinaryFrameHandler binaryFrameHandler, BilibiliLiveStatusChangeMsg msg) {
                IBilibiliMsgListener.super.onLiveStatusMsg(binaryFrameHandler, msg);
                log.error("{} 状态变化 {}", binaryFrameHandler.getRoomId(), msg.getLiveStatusAction());
            }

            @Override
            public void onRoomStatsMsg(BilibiliBinaryFrameHandler binaryFrameHandler, BilibiliRoomStatsMsg msg) {
                IBilibiliMsgListener.super.onRoomStatsMsg(binaryFrameHandler, msg);
                log.info("{} 统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", binaryFrameHandler.getRoomId(), msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
            }

            @Override
            public void onEntryEffect(BilibiliBinaryFrameHandler binaryFrameHandler, MessageMsg msg) {
                JsonNode data = msg.getData();
                String copyWriting = data.get("copy_writing").asText();
                log.info("入场效果 {}", copyWriting);
            }

            @Override
            public void onMsg(IMsg msg) {
                // log.debug("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(BilibiliCmdEnum cmd, ICmdMsg<BilibiliCmdEnum> cmdMsg) {
                log.debug("收到CMD消息{} {}", cmd, cmdMsg);
            }

            @Override
            public void onOtherCmdMsg(BilibiliCmdEnum cmd, ICmdMsg<BilibiliCmdEnum> cmdMsg) {
                log.debug("收到其他CMD消息 {}", cmd);
                if (!((MessageMsg) cmdMsg).getUnknownProperties().isEmpty()) {
                    int size = ((MessageMsg) cmdMsg).getUnknownProperties().size();
                }
                switch (cmd) {
                    case GUARD_BUY: {
                        // 有人上舰
                        MessageMsg messageMsg = (MessageMsg) cmdMsg;
                        break;
                    }
                    case SUPER_CHAT_MESSAGE_DELETE: {
                        // 删除醒目留言
                        MessageMsg messageMsg = (MessageMsg) cmdMsg;
                        break;
                    }
                }
            }

            @Override
            public void onUnknownCmd(String cmdString, IMsg msg) {
                log.debug("收到未知CMD消息 {}", cmdString);
            }
        });
        client.connect();

        client.addStatusChangeListener((evt, oldStatus, newStatus) -> {
            if (newStatus == ClientStatusEnums.CONNECTED) {
                log.warn("房间直播状态: {}", client.getRoomInitResult().getRoomLiveStatus());
                log.warn("房间直播流地址: {}", client.getRoomInitResult().getRoomLiveStreamUrls());
                if (oldStatus == ClientStatusEnums.CONNECTING) {
                    // 0 未开播，1 直播中，2 投稿视频
                    log.error("开播状态: {}", client.getRoomInitResult().getRoomPlayInfoResult().getLive_status());
                    log.error("直播间标题: {}", client.getRoomInitResult().getRoomTitle());
//                ThreadUtil.execAsync(() -> {
//                    ThreadUtil.sleep(5000);
//                    client.clickLike(5, () -> {
//                        log.warn("为主播点赞成功");
//                    });
//                });
                } else if (oldStatus == ClientStatusEnums.RECONNECTING) {
                    RoomPlayInfoResult roomPlayInfo = BilibiliApis.getRoomPlayInfo(client.getConfig().getRoomId(), client.getConfig().getCookie());
                    BilibiliLiveStatusEnum liveStatus = roomPlayInfo.getLive_status();
                    log.error("重连后的开播状态: {}", liveStatus);
                }
            }
        });

        // 防止测试时直接退出
        while (true) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    @Test
    void liveStatusChangeTest() throws IOException {
        BilibiliLiveChatClient bilibiliLiveChatClient = new BilibiliLiveChatClient(BilibiliLiveChatClientConfig.builder()
                .roomId(8159061)
                .build());
        bilibiliLiveChatClient.addMsgListener(new IBilibiliMsgListener() {
            @Override
            public void onLiveStatusMsg(BilibiliBinaryFrameHandler binaryFrameHandler, BilibiliLiveStatusChangeMsg msg) {
                IBilibiliMsgListener.super.onLiveStatusMsg(binaryFrameHandler, msg);
                log.error("{} 状态变化 {} data: {}", binaryFrameHandler.getRoomId(), msg.getLiveStatusAction(), msg.getData());
            }
        });
        bilibiliLiveChatClient.connect();

        while (true) {
            System.in.read();
        }
    }

    @Test
    void forwardMsgTest() throws Exception {
        String forwardWebsocketUri = "ws://localhost:8080/websocket";
        BilibiliLiveChatClient liveChatClient = new BilibiliLiveChatClient(BilibiliLiveChatClientConfig.builder()
                // TODO 以后的版本将被弃用
                .forwardWebsocketUri(forwardWebsocketUri)
                .roomId(7777)
                .build());

        // 「消息转发插件」，默认用法
        liveChatClient.addPlugin(new ForwardMsgPlugin(forwardWebsocketUri));

        // 「消息转发插件」，自定义消息转发
        liveChatClient.addPlugin(new ForwardMsgPlugin(forwardWebsocketUri, (webSocketLiveChatClient, msg) -> {
            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
            byteBuf.writeCharSequence("自定义消息转发：" + msg.toString(), StandardCharsets.UTF_8);
            webSocketLiveChatClient.send(new BinaryWebSocketFrame(byteBuf));
        }));

        // 自定义插件，实现「消息转发插件」的功能
        liveChatClient.addPlugin(new IPlugin() {
            WebSocketLiveChatClient webSocketLiveChatClient;
            final IBilibiliMsgListener msgListener = new IBilibiliMsgListener() {
                @Override
                public void onDanmuMsg(DanmuMsgMsg msg) {
                    webSocketLiveChatClient.send(new BinaryWebSocketFrame(ByteBufAllocator.DEFAULT.buffer().writeBytes(("弹幕：" + msg.getContent()).getBytes(StandardCharsets.UTF_8))));
                }
            };

            @Override
            public <LiveChatClient extends IBaseLiveChatClient<?, MsgListener>, MsgListener extends IBaseMsgListener<?, ?>> void register(LiveChatClient liveChatClient, Class<MsgListener> msgListenerClass) {
                webSocketLiveChatClient = new WebSocketLiveChatClient(WebSocketLiveChatClientConfig
                        .builder()
                        .websocketUri("ws://localhost:8080/websocket")
                        .build());
                webSocketLiveChatClient.addStatusChangeListener((evt, oldStatus, newStatus) -> {
                    if (newStatus == ClientStatusEnums.CONNECTED) {
                        // TODO 自定义操作，例如发送认证包
                        webSocketLiveChatClient.send(new BinaryWebSocketFrame(ByteBufAllocator.DEFAULT.buffer().writeBytes("认证包".getBytes(StandardCharsets.UTF_8))));
                    }
                });
                webSocketLiveChatClient.connect();
                liveChatClient.addMsgListener((MsgListener) msgListener);
            }

            @Override
            public <LiveChatClient extends IBaseLiveChatClient<?, MsgListener>, MsgListener extends IBaseMsgListener<?, ?>> void unregister(LiveChatClient liveChatClient, Class<MsgListener> msgListenerClass) {
                webSocketLiveChatClient.destroy();
                liveChatClient.removeMsgListener((MsgListener) msgListener);
            }
        });

        // 5秒钟后移除所有插件
        ThreadUtil.execAsync(() -> {
            ThreadUtil.sleep(5000);

            List<IPlugin> plugins = liveChatClient.getPlugins();
            for (IPlugin iPlugin : CollUtil.newArrayList(plugins)) {
                liveChatClient.removePlugin(iPlugin);
            }

            System.out.println("plugins.size(): " + liveChatClient.getPlugins().size());
            System.out.println("msgListeners.size(): " + liveChatClient.getMsgListeners().size());
        });

        System.out.println("plugins.size(): " + liveChatClient.getPlugins().size());
        System.out.println("msgListeners.size(): " + liveChatClient.getMsgListeners().size());

        liveChatClient.connect();

        while (true) {
            System.in.read();
        }
    }
}