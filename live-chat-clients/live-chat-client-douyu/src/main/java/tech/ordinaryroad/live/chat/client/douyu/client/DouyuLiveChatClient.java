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

package tech.ordinaryroad.live.chat.client.douyu.client;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseCmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.douyu.api.DouyuApis;
import tech.ordinaryroad.live.chat.client.douyu.config.DouyuLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyu.constant.DouyuCmdEnum;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuDouyuCmdMsgListener;
import tech.ordinaryroad.live.chat.client.douyu.msg.DouyuCmdMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.HeartbeatMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.LoginresMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.base.IDouyuMsg;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.douyu.netty.frame.HeartbeatWebSocketFrame;
import tech.ordinaryroad.live.chat.client.douyu.netty.frame.factory.DouyuWebSocketFrameFactory;
import tech.ordinaryroad.live.chat.client.douyu.netty.handler.DouyuBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.douyu.netty.handler.DouyuConnectionHandler;
import tech.ordinaryroad.live.chat.client.servers.netty.client.base.BaseNettyLiveChatClient;

/**
 * 直播间弹幕客户端
 *
 * @author mjz
 * @date 2023/8/20
 */
@Slf4j
public class DouyuLiveChatClient extends BaseNettyLiveChatClient<
        DouyuLiveChatClientConfig,
        DouyuCmdEnum,
        IDouyuMsg,
        IDouyuDouyuCmdMsgListener,
        DouyuConnectionHandler,
        DouyuBinaryFrameHandler
        > implements IDouyuDouyuCmdMsgListener {

    private final IDouyuDouyuCmdMsgListener msgListener;

    public DouyuLiveChatClient(DouyuLiveChatClientConfig config, IDouyuDouyuCmdMsgListener msgListener, IBaseConnectionListener<DouyuConnectionHandler> connectionListener, EventLoopGroup workerGroup) {
        super(config, workerGroup, connectionListener);
        this.msgListener = msgListener;

        // 初始化
        this.init();
    }

    public DouyuLiveChatClient(DouyuLiveChatClientConfig config, IDouyuDouyuCmdMsgListener msgListener, IBaseConnectionListener<DouyuConnectionHandler> connectionListener) {
        this(config, msgListener, connectionListener, new NioEventLoopGroup());
    }

    public DouyuLiveChatClient(DouyuLiveChatClientConfig config, IDouyuDouyuCmdMsgListener msgListener) {
        this(config, msgListener, null, new NioEventLoopGroup());
    }

    @Override
    public void init() {
        super.init();
        DouyuApis.init(super.getConfig());
    }

    @Override
    public DouyuConnectionHandler initConnectionHandler(IBaseConnectionListener<DouyuConnectionHandler> clientConnectionListener) {
        return new DouyuConnectionHandler(
                WebSocketClientHandshakerFactory.newHandshaker(getWebsocketUri(), WebSocketVersion.V13, null, true, new DefaultHttpHeaders()),
                clientConnectionListener, DouyuLiveChatClient.this
        );
    }

    @Override
    public DouyuBinaryFrameHandler initBinaryFrameHandler() {
        return new DouyuBinaryFrameHandler(DouyuLiveChatClient.this);
    }

    @Override
    public void onMsg(IMsg msg) {
        if (msg instanceof LoginresMsg) {
            // 1 type@=joingroup/rid@=4615502/gid@=1/
            send(DouyuWebSocketFrameFactory.getInstance().createJoingroup(getConfig().getRoomId()), () -> {
                // 2 type@=mrkl/
                send(DouyuWebSocketFrameFactory.getInstance().createHeartbeat(), () -> {
                    // 3 type@=sub/mt@=dayrk/
                    send(DouyuWebSocketFrameFactory.getInstance().createSub());
                });
            });
        }
        if (this.msgListener != null) {
            this.msgListener.onMsg(msg);
        }
    }

    @Override
    public void onDanmuMsg(DouyuCmdMsg cmdMsg) {
        if (this.msgListener != null) {
            this.msgListener.onDanmuMsg(cmdMsg);
        }
    }

    @Override
    public void onCmdMsg(DouyuCmdEnum cmd, BaseCmdMsg<DouyuCmdEnum> cmdMsg) {
        if (!(cmdMsg instanceof DouyuCmdMsg)) {
            log.debug("非DouyuCmdMsg {}", cmdMsg.getClass());
            return;
        }
        if (this.msgListener != null) {
            this.msgListener.onCmdMsg(cmd, cmdMsg);
        }
    }

    @Override
    public void onOtherCmdMsg(DouyuCmdEnum cmd, BaseCmdMsg<DouyuCmdEnum> cmdMsg) {
        if (this.msgListener != null) {
            this.msgListener.onOtherCmdMsg(cmd, cmdMsg);
        }
    }

    @Override
    public void onUnknownCmd(String cmdString, BaseMsg msg) {
        if (this.msgListener != null) {
            this.msgListener.onUnknownCmd(cmdString, msg);
        }
    }

}
