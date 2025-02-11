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
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.listener.*;
import tech.ordinaryroad.live.chat.client.commons.base.msg.*;
import tech.ordinaryroad.live.chat.client.commons.client.IBaseLiveChatClient;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.commons.client.listener.IClientStatusChangeListener;
import tech.ordinaryroad.live.chat.client.commons.client.plugin.BasePlugin;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.BaseBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.websocket.client.WebSocketLiveChatClient;
import tech.ordinaryroad.live.chat.client.websocket.config.WebSocketLiveChatClientConfig;

import java.beans.PropertyChangeEvent;
import java.nio.charset.StandardCharsets;

/**
 * @author mjz
 * @date 2025/2/10
 */
@Slf4j
public abstract class BaseForwardMsgPlugin extends BasePlugin
        implements IClientStatusChangeListener,
        IDanmuMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, IDanmuMsg>,
        IGiftMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, IGiftMsg>,
        ISuperChatMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, ISuperChatMsg>,
        IEnterRoomMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, IEnterRoomMsg>,
        ILikeMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, ILikeMsg>,
        ILiveStatusChangeListener<BaseBinaryFrameHandler<?, ?, ?, ?>, ILiveStatusChangeMsg>,
        IRoomStatsMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, IRoomStatsMsg>,
        ISocialMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, ISocialMsg> {

    protected String webSocketUri;
    private WebSocketLiveChatClient webSocketLiveChatClient;

    public BaseForwardMsgPlugin(String webSocketUri) {
        this.webSocketUri = webSocketUri;
        // 初始化转发 WebSocket客户端
        this.initForwardClient(webSocketUri);
    }

    @Override
    public void onDanmuMsg(IDanmuMsg msg) {
        this.doForward(msg);
    }

    @Override
    public void onGiftMsg(IGiftMsg msg) {
        this.doForward(msg);
    }

    @Override
    public void onSuperChatMsg(ISuperChatMsg msg) {
        this.doForward(msg);
    }

    @Override
    public void onEnterRoomMsg(IEnterRoomMsg msg) {
        this.doForward(msg);
    }

    @Override
    public void onLikeMsg(ILikeMsg msg) {
        this.doForward(msg);
    }

    @Override
    public void onLiveStatusMsg(ILiveStatusChangeMsg msg) {
        this.doForward(msg);
    }

    @Override
    public void onRoomStatsMsg(IRoomStatsMsg msg) {
        this.doForward(msg);
    }

    @Override
    public void onSocialMsg(ISocialMsg msg) {
        this.doForward(msg);
    }

    private void doForward(IMsg msg) {
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

    @Override
    public <LiveCharClient extends IBaseLiveChatClient<?, MsgListener>, MsgListener extends IBaseMsgListener<?, ?>> void register(LiveCharClient liveChatClient, Class<MsgListener> msgListenerClass) {
        log.debug("插件注册中：消息转发");
        // 使用 ByteBuddy 动态生成实现了指定MsgListener接口的插件实现类
        Class<?> dynamicType = new ByteBuddy()
                .subclass(BaseForwardMsgPlugin.class) // 继承插件抽象类
                .implement(msgListenerClass)   // 实现MsgListener接口
                .make()
                .load(BaseForwardMsgPlugin.class.getClassLoader())
                .getLoaded();
        try {
            // 创建转发消息监听器，继承自抽象类，实现了对应平台的接口
            MsgListener msgListener = (MsgListener) dynamicType.getDeclaredConstructor(String.class).newInstance(webSocketUri);

            liveChatClient.addMsgListener(msgListener);
            liveChatClient.addStatusChangeListener((IClientStatusChangeListener) msgListener);
            if (log.isDebugEnabled()) {
                log.debug("插件注册成功：消息转发，转发地址: {}", webSocketUri);
            }
        } catch (Exception e) {
            log.error("插件注册失败：消息转发", e);
            throw new BaseException(e);
        }
    }

    @Override
    public <LiveCharClient extends IBaseLiveChatClient<?, MsgListener>, MsgListener extends IBaseMsgListener<?, ?>> void unregister(LiveCharClient liveChatClient, Class<MsgListener> msgListenerClass) {
        // TODO 销毁该插件
        System.out.println("TODO 销毁该插件");
    }
}
