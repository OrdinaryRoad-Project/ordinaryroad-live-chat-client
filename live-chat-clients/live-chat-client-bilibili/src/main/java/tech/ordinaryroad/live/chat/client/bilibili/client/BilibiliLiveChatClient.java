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

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.bilibili.constant.BilibiliCmdEnum;
import tech.ordinaryroad.live.chat.client.bilibili.listener.IBilibiliSendSmsReplyMsgListener;
import tech.ordinaryroad.live.chat.client.bilibili.msg.base.IBilibiliMsg;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliConnectionHandler;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.servers.netty.client.base.BaseNettyClient;

/**
 * B站直播间弹幕客户端
 *
 * @author mjz
 * @date 2023/8/20
 */
@Slf4j
public class BilibiliLiveChatClient extends BaseNettyClient<
        BilibiliLiveChatClientConfig,
        BilibiliCmdEnum,
        IBilibiliMsg,
        IBilibiliSendSmsReplyMsgListener,
        BilibiliConnectionHandler,
        BilibiliBinaryFrameHandler
        > {

    private final IBilibiliSendSmsReplyMsgListener msgListener;

    public BilibiliLiveChatClient(BilibiliLiveChatClientConfig config, IBilibiliSendSmsReplyMsgListener msgListener, IBaseConnectionListener<BilibiliConnectionHandler> connectionListener, EventLoopGroup workerGroup) {
        super(config, workerGroup, connectionListener);
        this.msgListener = msgListener;

        // 初始化
        this.init();
    }

    public BilibiliLiveChatClient(BilibiliLiveChatClientConfig config, IBilibiliSendSmsReplyMsgListener msgListener, IBaseConnectionListener<BilibiliConnectionHandler> connectionListener) {
        this(config, msgListener, connectionListener, new NioEventLoopGroup());
    }

    public BilibiliLiveChatClient(BilibiliLiveChatClientConfig config, IBilibiliSendSmsReplyMsgListener msgListener) {
        this(config, msgListener, null, new NioEventLoopGroup());
    }

    @Override
    public BilibiliConnectionHandler initConnectionHandler(IBaseConnectionListener<BilibiliConnectionHandler> clientConnectionListener) {
        return new BilibiliConnectionHandler(
                WebSocketClientHandshakerFactory.newHandshaker(getWebsocketUri(), WebSocketVersion.V13, null, true, new DefaultHttpHeaders()),
                clientConnectionListener, BilibiliLiveChatClient.this
        );
    }

    @Override
    public BilibiliBinaryFrameHandler initBinaryFrameHandler() {
        return new BilibiliBinaryFrameHandler(BilibiliLiveChatClient.this.msgListener, BilibiliLiveChatClient.this);
    }
}
