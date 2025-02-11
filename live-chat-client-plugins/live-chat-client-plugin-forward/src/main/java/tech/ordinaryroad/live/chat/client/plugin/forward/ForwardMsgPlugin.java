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

package tech.ordinaryroad.live.chat.client.plugin.forward;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseMsgListener;
import tech.ordinaryroad.live.chat.client.commons.client.IBaseLiveChatClient;
import tech.ordinaryroad.live.chat.client.commons.client.plugin.IPlugin;
import tech.ordinaryroad.live.chat.client.plugin.forward.base.AbstractForwardMsgListener;
import tech.ordinaryroad.live.chat.client.plugin.forward.base.IForwardMsgHandler;
import tech.ordinaryroad.live.chat.client.websocket.client.WebSocketLiveChatClient;

/**
 * @author mjz
 * @date 2025/2/11
 */
@Slf4j
public class ForwardMsgPlugin implements IPlugin {
    private final String forwardWebSocketUri;
    private final IForwardMsgHandler forwardMsgHandler;
    private AbstractForwardMsgListener forwardMsgListenerProxy;

    public ForwardMsgPlugin(String forwardWebSocketUri) {
        this(forwardWebSocketUri, new DefaultForwardMsgHandler());
    }

    public ForwardMsgPlugin(String forwardWebSocketUri, IForwardMsgHandler forwardMsgHandler) {
        this.forwardWebSocketUri = forwardWebSocketUri;
        this.forwardMsgHandler = forwardMsgHandler;
    }

    @Override
    public <LiveCharClient extends IBaseLiveChatClient<?, MsgListener>, MsgListener extends IBaseMsgListener<?, ?>> void register(LiveCharClient liveChatClient, Class<MsgListener> msgListenerClass) {
        log.debug("插件注册中：消息转发");

        if (StrUtil.isBlank(this.forwardWebSocketUri)) {
            log.warn("消息转发地址为空，取消注册「消息转发」插件");
            return;
        }

        // 使用 ByteBuddy 动态生成实现了指定MsgListener接口的插件实现类
        Class<?> dynamicType = new ByteBuddy()
                .subclass(AbstractForwardMsgListener.class) // 继承插件抽象类
                .implement(msgListenerClass)   // 实现MsgListener接口
                .make()
                .load(msgListenerClass.getClassLoader())
                .getLoaded();
        try {
            // 创建转发消息监听器，继承自抽象类，实现了对应平台的接口
            forwardMsgListenerProxy = (AbstractForwardMsgListener) dynamicType.getDeclaredConstructor(String.class, IForwardMsgHandler.class).newInstance(this.forwardWebSocketUri, this.forwardMsgHandler);

            // 添加消息监听器
            liveChatClient.addMsgListener((MsgListener) forwardMsgListenerProxy);

            if (log.isDebugEnabled()) {
                log.debug("插件注册成功：消息转发，转发地址: {}", this.forwardMsgListenerProxy);
            }
        } catch (Exception e) {
            log.error("插件注册失败：消息转发", e);
            throw new BaseException(e);
        }
    }

    @Override
    public <LiveCharClient extends IBaseLiveChatClient<?, MsgListener>, MsgListener extends IBaseMsgListener<?, ?>> void unregister(LiveCharClient liveChatClient, Class<MsgListener> msgListenerClass) {
        log.debug("插件销毁中：消息转发");

        // 销毁用于转发消息的WebSocket LiveChatClient
        forwardMsgListenerProxy.destroyForwardClient();
        // 移除消息监听器
        liveChatClient.removeMsgListener((MsgListener) this.forwardMsgListenerProxy);

        log.debug("插件销毁完成：消息转发");
    }

    /**
     * 获取用于转发的WebSocket Client
     */
    public WebSocketLiveChatClient getWebSocketLiveChatClient() {
        return this.forwardMsgListenerProxy.getWebSocketLiveChatClient();
    }
}
