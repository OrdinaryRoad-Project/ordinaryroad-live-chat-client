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

package tech.ordinaryroad.live.chat.client.servers.netty.handler.base;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


/**
 * 连接处理器
 *
 * @author mjz
 * @date 2023/8/21
 */
@Slf4j
public abstract class BaseConnectionHandler<ConnectionHandler extends BaseConnectionHandler<?>>
        extends ChannelInboundHandlerAdapter
        implements IBaseConnectionHandler {

    @Getter
    private final Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler;
    @Getter
    private ChannelPromise handshakeFuture;
    private final IBaseConnectionListener<ConnectionHandler> listener;
    /**
     * 客户端发送心跳包
     */
    private ScheduledFuture<?> scheduledFuture = null;

    public BaseConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler, IBaseConnectionListener<ConnectionHandler> listener) {
        this.webSocketProtocolHandler = webSocketProtocolHandler;
        this.listener = listener;
    }

    public BaseConnectionHandler(Supplier<WebSocketClientProtocolHandler> webSocketProtocolHandler) {
        this(webSocketProtocolHandler, null);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (log.isDebugEnabled()) {
            log.debug("channelActive");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (log.isDebugEnabled()) {
            log.debug("channelInactive");
        }
        heartbeatCancel();
        if (this.listener != null) {
            listener.onDisconnected((ConnectionHandler) BaseConnectionHandler.this);
        }
    }

    /**
     * 开始发送心跳包
     */
    private void heartbeatStart(Channel channel) {
        if (getHeartbeatPeriod() > 0) {
            scheduledFuture = channel.eventLoop().scheduleAtFixedRate(() -> {
                sendHeartbeat(channel);
            }, getHeartbeatInitialDelay(), getHeartbeatPeriod(), TimeUnit.SECONDS);
        } else {
            scheduledFuture = channel.eventLoop().schedule(() -> {
                sendHeartbeat(channel);
            }, getHeartbeatInitialDelay(), TimeUnit.SECONDS);
        }
    }

    /**
     * 取消发送心跳包
     */
    private void heartbeatCancel() {
        if (null != scheduledFuture && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
    }

    public abstract long getHeartbeatPeriod();

    public abstract long getHeartbeatInitialDelay();

    private void handshakeSuccessfully(Channel channel) {
        if (log.isDebugEnabled()) {
            log.debug("握手完成!");
        }
        this.handshakeFuture.setSuccess();

        heartbeatCancel();
        heartbeatStart(channel);
        if (this.listener != null) {
            listener.onConnected((ConnectionHandler) BaseConnectionHandler.this);
        }
    }

    private void handshakeFailed(ChannelHandlerContext ctx, WebSocketClientProtocolHandler.ClientHandshakeStateEvent evt) {
        log.error("握手失败！ {}", evt);
        this.handshakeFuture.setFailure(new BaseException(evt.name()));

        if (listener != null) {
            this.listener.onConnectFailed((ConnectionHandler) this);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("userEventTriggered {}", evt);
        }
        if (evt == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            handshakeSuccessfully(ctx.channel());
        } else if (evt == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_TIMEOUT) {
            handshakeFailed(ctx, (WebSocketClientProtocolHandler.ClientHandshakeStateEvent) evt);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("exceptionCaught", cause);
        if (!this.handshakeFuture.isDone()) {
            this.handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
