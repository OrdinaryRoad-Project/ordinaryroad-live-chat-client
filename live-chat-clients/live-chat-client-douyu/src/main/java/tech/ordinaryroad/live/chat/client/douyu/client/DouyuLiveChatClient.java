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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseCmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.douyu.config.DouyuLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.douyu.constant.DouyuCmdEnum;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuConnectionListener;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuDouyuCmdMsgListener;
import tech.ordinaryroad.live.chat.client.douyu.msg.ChatmsgMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.LoginresMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.MsgrepeaterproxylistMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.base.IDouyuMsg;
import tech.ordinaryroad.live.chat.client.douyu.netty.frame.factory.DouyuWebSocketFrameFactory;
import tech.ordinaryroad.live.chat.client.douyu.netty.handler.DouyuBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.douyu.netty.handler.DouyuConnectionHandler;
import tech.ordinaryroad.live.chat.client.servers.netty.client.base.BaseNettyClient;

import java.util.function.Consumer;

/**
 * 直播间弹幕客户端
 *
 * @author mjz
 * @date 2023/8/20
 */
@Slf4j
public class DouyuLiveChatClient extends BaseNettyClient<
        DouyuLiveChatClientConfig,
        DouyuCmdEnum,
        IDouyuMsg,
        ChatmsgMsg,
        IDouyuDouyuCmdMsgListener,
        DouyuConnectionHandler,
        DouyuBinaryFrameHandler
        > implements IDouyuDouyuCmdMsgListener {

    public static final int MODE_WS = 1;
    public static final int MODE_DANMU = 2;

    private DouyuLiveChatClient danmuClient = null;
    private final int mode;
    private final IDouyuConnectionListener connectionListener;
    private final IDouyuDouyuCmdMsgListener msgListener;

    public DouyuLiveChatClient(int mode, DouyuLiveChatClientConfig config, IDouyuDouyuCmdMsgListener msgListener, IDouyuConnectionListener connectionListener, EventLoopGroup workerGroup) {
        super(config, workerGroup, null);
        this.mode = mode;
        this.connectionListener = connectionListener;
        this.msgListener = msgListener;

        // 初始化
        this.init();
    }

    public DouyuLiveChatClient(DouyuLiveChatClientConfig config, IDouyuDouyuCmdMsgListener msgListener, IDouyuConnectionListener connectionListener, EventLoopGroup workerGroup) {
        this(MODE_WS, config, msgListener, connectionListener, workerGroup);
    }

    public DouyuLiveChatClient(DouyuLiveChatClientConfig config, IDouyuDouyuCmdMsgListener msgListener, IDouyuConnectionListener connectionListener) {
        this(config, msgListener, connectionListener, new NioEventLoopGroup());
    }

    public DouyuLiveChatClient(DouyuLiveChatClientConfig config, IDouyuDouyuCmdMsgListener msgListener) {
        this(config, msgListener, null, new NioEventLoopGroup());
    }

    @Override
    public DouyuConnectionHandler initConnectionHandler(IBaseConnectionListener<DouyuConnectionHandler> clientConnectionListener) {
        return new DouyuConnectionHandler(
                mode, WebSocketClientHandshakerFactory.newHandshaker(getWebsocketUri(), WebSocketVersion.V13, null, true, new DefaultHttpHeaders()),
                DouyuLiveChatClient.this, new IDouyuConnectionListener() {
            @Override
            public void onConnected(DouyuConnectionHandler connectionHandler) {
                clientConnectionListener.onConnected(connectionHandler);
                if (mode == MODE_DANMU) {
                    if (DouyuLiveChatClient.this.connectionListener != null) {
                        DouyuLiveChatClient.this.connectionListener.onConnected(connectionHandler);
                    }
                }
            }

            @Override
            public void onConnectFailed(DouyuConnectionHandler connectionHandler) {
                clientConnectionListener.onConnectFailed(connectionHandler);
                if (mode == MODE_DANMU) {
                    if (DouyuLiveChatClient.this.connectionListener != null) {
                        DouyuLiveChatClient.this.connectionListener.onConnectFailed(connectionHandler);
                    }
                }
            }

            @Override
            public void onDisconnected(DouyuConnectionHandler connectionHandler) {
                clientConnectionListener.onDisconnected(connectionHandler);
                if (mode == MODE_DANMU) {
                    if (DouyuLiveChatClient.this.connectionListener != null) {
                        DouyuLiveChatClient.this.connectionListener.onDisconnected(connectionHandler);
                    }
                }
            }
        });
    }

    @Override
    public DouyuBinaryFrameHandler initBinaryFrameHandler() {
        return new DouyuBinaryFrameHandler(DouyuLiveChatClient.this, DouyuLiveChatClient.this);
    }

    @Override
    public void onMsg(DouyuBinaryFrameHandler binaryFrameHandler, IMsg msg) {
        if (mode == MODE_DANMU) {
            if (msg instanceof LoginresMsg) {
                // 1 type@=joingroup/rid@=4615502/gid@=1/
                send(getWebSocketFrameFactory(getConfig().getRoomId()).createJoingroup(), () -> {
                    // 2 type@=mrkl/
                    send(getWebSocketFrameFactory(getConfig().getRoomId()).createHeartbeat(), () -> {
                        // 3 type@=sub/mt@=dayrk/
                        send(getWebSocketFrameFactory(getConfig().getRoomId()).createSub());
                    });
                });
            }
        } else {
            if (msg instanceof LoginresMsg) {
                send(getWebSocketFrameFactory(getConfig().getRoomId()).createKeeplive(getConfig().getCookie()));
                // 1 type@=h5ckreq/rid@=3168536/ti@=250120230908/
                // send(getWebSocketFrameFactory(getConfig().getRoomId()).createH5ckreq());
            } else if (msg instanceof MsgrepeaterproxylistMsg msgrepeaterproxylistMsg) {
                // 初始化danmuClient
                if (mode == MODE_WS) {
                    DouyuLiveChatClientConfig danmuClientConfig = BeanUtil.toBean(getConfig(), DouyuLiveChatClientConfig.class, CopyOptions.create().ignoreNullValue());
                    danmuClientConfig.setWebsocketUri("wss://danmuproxy.douyu.com:8503/");
                    this.danmuClient = new DouyuLiveChatClient(MODE_DANMU, danmuClientConfig, msgListener, connectionListener, new NioEventLoopGroup());
                    this.danmuClient.connect();
                }

            }
        }
        if (this.msgListener != null) {
            this.msgListener.onMsg(binaryFrameHandler, msg);
        }
    }

    private static DouyuWebSocketFrameFactory getWebSocketFrameFactory(long roomId) {
        return DouyuWebSocketFrameFactory.getInstance(roomId);
    }

    @Override
    public void onDanmuMsg(DouyuBinaryFrameHandler binaryFrameHandler, ChatmsgMsg msg) {
        if (this.msgListener != null) {
            this.msgListener.onDanmuMsg(binaryFrameHandler, msg);
        }
    }

    @Override
    public void onCmdMsg(DouyuBinaryFrameHandler binaryFrameHandler, DouyuCmdEnum cmd, BaseCmdMsg<DouyuCmdEnum> cmdMsg) {
        if (this.msgListener != null) {
            this.msgListener.onCmdMsg(binaryFrameHandler, cmd, cmdMsg);
        }
    }

    @Override
    public void onOtherCmdMsg(DouyuBinaryFrameHandler binaryFrameHandler, DouyuCmdEnum cmd, BaseCmdMsg<DouyuCmdEnum> cmdMsg) {
        if (this.msgListener != null) {
            this.msgListener.onOtherCmdMsg(binaryFrameHandler, cmd, cmdMsg);
        }
    }

    @Override
    public void onUnknownCmd(DouyuBinaryFrameHandler binaryFrameHandler, String cmdString, BaseMsg msg) {
        if (this.msgListener != null) {
            this.msgListener.onUnknownCmd(binaryFrameHandler, cmdString, msg);
        }
    }

    @Override
    public void sendDanmu(Object danmu, Runnable success, Consumer<Throwable> failed) {
        if (!checkCanSendDanmn()) {
            return;
        }
        if (mode == MODE_WS && danmu instanceof String msg) {
            send(getWebSocketFrameFactory(getConfig().getRoomId()).createDanmu(msg, getConfig().getCookie()), success, failed);
            finishSendDanmu();
        } else {
            super.sendDanmu(danmu);
        }
    }
}
