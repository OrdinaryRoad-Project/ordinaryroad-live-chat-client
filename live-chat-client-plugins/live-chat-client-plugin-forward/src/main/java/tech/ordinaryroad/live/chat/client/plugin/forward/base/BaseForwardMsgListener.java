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

package tech.ordinaryroad.live.chat.client.plugin.forward.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import tech.ordinaryroad.live.chat.client.commons.base.listener.*;
import tech.ordinaryroad.live.chat.client.commons.client.IBaseLiveChatClient;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.commons.client.listener.IClientStatusChangeListener;
import tech.ordinaryroad.live.chat.client.websocket.client.WebSocketLiveChatClient;
import tech.ordinaryroad.live.chat.client.websocket.config.WebSocketLiveChatClientConfig;

import java.beans.PropertyChangeEvent;
import java.nio.charset.StandardCharsets;

/**
 * @author mjz
 * @date 2025/2/10
 */
public abstract class BaseForwardMsgListener<
        BinaryFrameHandler, CmdEnum extends Enum<CmdEnum>,
        DanmuMsg, GiftMsg, SuperChatMsg, EnterRoomMsg, LikeMsg, LiveStatusChangeMsg, RoomStatsMsg, SocialMsg
        > implements IClientStatusChangeListener,
        IBaseMsgListener<BinaryFrameHandler, CmdEnum>,
        IDanmuMsgListener<BinaryFrameHandler, DanmuMsg>,
        IGiftMsgListener<BinaryFrameHandler, GiftMsg>,
        ISuperChatMsgListener<BinaryFrameHandler, SuperChatMsg>,
        IEnterRoomMsgListener<BinaryFrameHandler, EnterRoomMsg>,
        ILikeMsgListener<BinaryFrameHandler, LikeMsg>,
        ILiveStatusChangeListener<BinaryFrameHandler, LiveStatusChangeMsg>,
        IRoomStatsMsgListener<BinaryFrameHandler, RoomStatsMsg>,
        ISocialMsgListener<BinaryFrameHandler, SocialMsg> {

    protected IBaseLiveChatClient<?, ?> client;
    protected String webSocketUri;
    private WebSocketLiveChatClient webSocketLiveChatClient;

    public BaseForwardMsgListener(IBaseLiveChatClient<?, ?> client, String webSocketUri) {
        this.client = client;
        this.webSocketUri = webSocketUri;

        this.initForwardClient(webSocketUri);
        client.addStatusChangeListener(this);
    }

    @SneakyThrows
    public static <MsgListener extends IBaseMsgListener<?, ?>> void addForwardMsgListener(
            IBaseLiveChatClient<?, MsgListener> liveChatClient,
            Class<MsgListener> clazz,
            String webSocketUri
    ) {
        // 使用 ByteBuddy 动态生成类
        Class<?> dynamicType = new ByteBuddy()
                .subclass(BaseForwardMsgListener.class) // 继承抽象类
                .implement(clazz)   // 实现接口
                .make()
                .load(BaseForwardMsgListener.class.getClassLoader())
                .getLoaded();

        // 创建转发消息监听器，继承自抽象类，实现了对应平台的接口
        MsgListener msgListener = (MsgListener) dynamicType.getDeclaredConstructor(IBaseLiveChatClient.class, String.class)
                .newInstance(liveChatClient, webSocketUri);

        liveChatClient.addMsgListener(msgListener);
    }

    @Override
    public void onDanmuMsg(DanmuMsg msg) {
        this.forward(msg);
    }

    @Override
    public void onGiftMsg(GiftMsg msg) {
        this.forward(msg);
    }

    @Override
    public void onSuperChatMsg(SuperChatMsg msg) {
        this.forward(msg);
    }

    @Override
    public void onEnterRoomMsg(EnterRoomMsg msg) {
        this.forward(msg);
    }

    @Override
    public void onLikeMsg(LikeMsg msg) {
        this.forward(msg);
    }

    @Override
    public void onLiveStatusMsg(LiveStatusChangeMsg msg) {
        this.forward(msg);
    }

    @Override
    public void onRoomStatsMsg(RoomStatsMsg msg) {
        this.forward(msg);
    }

    @Override
    public void onSocialMsg(SocialMsg msg) {
        this.forward(msg);
    }

    public void forward(Object msg) {
        if (webSocketLiveChatClient == null) {
            return;
        }

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeCharSequence(msg.toString(), StandardCharsets.UTF_8);
        webSocketLiveChatClient.send(new BinaryWebSocketFrame(byteBuf));
    }

    public void initForwardClient(String webSocketUri) {
        destroyForwardClient();
        webSocketLiveChatClient = new WebSocketLiveChatClient(
                WebSocketLiveChatClientConfig.builder()
                        .websocketUri(webSocketUri)
                        .build()
        );
        webSocketLiveChatClient.connect();
    }

    public void destroyForwardClient() {
        if (webSocketLiveChatClient != null) {
            webSocketLiveChatClient.destroy();
            webSocketLiveChatClient = null;
        }
    }

    @Override
    public void onStatusChange(PropertyChangeEvent evt, ClientStatusEnums oldStatus, ClientStatusEnums newStatus) {
        switch (newStatus) {
            case DESTROYED:
                this.destroyForwardClient();
                break;
            default:
        }
    }
}
