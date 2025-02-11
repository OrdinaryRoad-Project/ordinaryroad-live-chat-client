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

import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.listener.*;
import tech.ordinaryroad.live.chat.client.commons.base.msg.*;
import tech.ordinaryroad.live.chat.client.plugin.forward.DefaultForwardMsgHandler;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.BaseBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.websocket.client.WebSocketLiveChatClient;
import tech.ordinaryroad.live.chat.client.websocket.config.WebSocketLiveChatClientConfig;

/**
 * @author mjz
 * @date 2025/2/10
 */
@Slf4j
public abstract class AbstractForwardMsgListener implements
        IDanmuMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, IDanmuMsg>,
        IGiftMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, IGiftMsg>,
        ISuperChatMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, ISuperChatMsg>,
        IEnterRoomMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, IEnterRoomMsg>,
        ILikeMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, ILikeMsg>,
        ILiveStatusChangeListener<BaseBinaryFrameHandler<?, ?, ?, ?>, ILiveStatusChangeMsg>,
        IRoomStatsMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, IRoomStatsMsg>,
        ISocialMsgListener<BaseBinaryFrameHandler<?, ?, ?, ?>, ISocialMsg> {

    private final IForwardMsgHandler forwardMsgHandler;
    private WebSocketLiveChatClient webSocketLiveChatClient;

    public AbstractForwardMsgListener(String webSocketUri) {
        this(webSocketUri, new DefaultForwardMsgHandler());
    }

    public AbstractForwardMsgListener(String webSocketUri, IForwardMsgHandler forwardMsgHandler) {
        this.forwardMsgHandler = forwardMsgHandler;
        // 初始化转发 WebSocket客户端
        this.initForwardClient(webSocketUri);
    }

    @Override
    public void onDanmuMsg(IDanmuMsg msg) {
        this.forwardMsgHandler.doForward(webSocketLiveChatClient, msg);
    }

    @Override
    public void onGiftMsg(IGiftMsg msg) {
        this.forwardMsgHandler.doForward(webSocketLiveChatClient, msg);
    }

    @Override
    public void onSuperChatMsg(ISuperChatMsg msg) {
        this.forwardMsgHandler.doForward(webSocketLiveChatClient, msg);
    }

    @Override
    public void onEnterRoomMsg(IEnterRoomMsg msg) {
        this.forwardMsgHandler.doForward(webSocketLiveChatClient, msg);
    }

    @Override
    public void onLikeMsg(ILikeMsg msg) {
        this.forwardMsgHandler.doForward(webSocketLiveChatClient, msg);
    }

    @Override
    public void onLiveStatusMsg(ILiveStatusChangeMsg msg) {
        this.forwardMsgHandler.doForward(webSocketLiveChatClient, msg);
    }

    @Override
    public void onRoomStatsMsg(IRoomStatsMsg msg) {
        this.forwardMsgHandler.doForward(webSocketLiveChatClient, msg);
    }

    @Override
    public void onSocialMsg(ISocialMsg msg) {
        this.forwardMsgHandler.doForward(webSocketLiveChatClient, msg);
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
}
