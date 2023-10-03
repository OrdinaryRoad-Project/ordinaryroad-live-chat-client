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

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.huya.config.HuyaLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaCmdEnum;
import tech.ordinaryroad.live.chat.client.huya.listener.IHuyaConnectionListener;
import tech.ordinaryroad.live.chat.client.huya.listener.IHuyaMsgListener;
import tech.ordinaryroad.live.chat.client.huya.msg.base.IHuyaMsg;
import tech.ordinaryroad.live.chat.client.huya.netty.handler.HuyaBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.huya.netty.handler.HuyaConnectionHandler;
import tech.ordinaryroad.live.chat.client.servers.netty.client.base.BaseNettyClient;

/**
 * 虎牙直播间弹幕客户端
 *
 * @author mjz
 * @date 2023/8/20
 */
@Slf4j
public class HuyaChatClient extends BaseNettyClient<
        HuyaLiveChatClientConfig,
        HuyaCmdEnum,
        IHuyaMsg,
        IHuyaMsgListener,
        HuyaConnectionHandler,
        HuyaBinaryFrameHandler
        > implements IHuyaMsgListener {

    public HuyaChatClient(HuyaLiveChatClientConfig config, IHuyaMsgListener msgListener, IHuyaConnectionListener connectionListener, EventLoopGroup workerGroup) {
        super(config, workerGroup, connectionListener);
        addMsgListener(msgListener);

        // 初始化
        this.init();
    }

    public HuyaChatClient(HuyaLiveChatClientConfig config, IHuyaMsgListener msgListener, IHuyaConnectionListener connectionListener) {
        this(config, msgListener, connectionListener, new NioEventLoopGroup());
    }

    public HuyaChatClient(HuyaLiveChatClientConfig config, IHuyaMsgListener msgListener) {
        this(config, msgListener, null, new NioEventLoopGroup());
    }

    @Override
    public void init() {
        super.init();
        addMsgListener(HuyaChatClient.this);
    }

    @Override
    public HuyaConnectionHandler initConnectionHandler(IBaseConnectionListener<HuyaConnectionHandler> clientConnectionListener) {
        return new HuyaConnectionHandler(
                WebSocketClientHandshakerFactory.newHandshaker(getWebsocketUri(), WebSocketVersion.V13, null, true, new DefaultHttpHeaders(), getConfig().getMaxFramePayloadLength()),
                HuyaChatClient.this, clientConnectionListener
        );
    }

    @Override
    public HuyaBinaryFrameHandler initBinaryFrameHandler() {
        return new HuyaBinaryFrameHandler(super.msgListeners, HuyaChatClient.this);
    }
}
