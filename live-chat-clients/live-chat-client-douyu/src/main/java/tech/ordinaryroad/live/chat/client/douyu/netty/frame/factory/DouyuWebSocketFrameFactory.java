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

package tech.ordinaryroad.live.chat.client.douyu.netty.frame.factory;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import tech.ordinaryroad.live.chat.client.douyu.api.DouyuApis;
import tech.ordinaryroad.live.chat.client.douyu.constant.DouyuCmdEnum;
import tech.ordinaryroad.live.chat.client.douyu.msg.HeartbeatMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.JoingroupMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.LoginreqMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.SubMsg;
import tech.ordinaryroad.live.chat.client.douyu.netty.frame.AuthWebSocketFrame;
import tech.ordinaryroad.live.chat.client.douyu.netty.frame.HeartbeatWebSocketFrame;
import tech.ordinaryroad.live.chat.client.douyu.util.DouyuCodecUtil;

/**
 * @author mjz
 * @date 2023/1/5
 */
public class DouyuWebSocketFrameFactory {

    private static final DouyuWebSocketFrameFactory INSTANCE = new DouyuWebSocketFrameFactory();
    private volatile static HeartbeatMsg heartbeatMsg;

    public DouyuWebSocketFrameFactory() {
    }

    public static DouyuWebSocketFrameFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 创建认证包
     *
     * @param roomId 浏览器地址中的房间id，支持短id
     * @return AuthWebSocketFrame
     */
    public AuthWebSocketFrame createAuth(long roomId) {
        try {
            // type@=loginreq/roomid@=7750753/dfl@=/username@=visitor10424697/uid@=1168052601/ver@=20220825/aver@=218101901/ct@=0/
            LoginreqMsg loginreqMsg = new LoginreqMsg();
            loginreqMsg.setRoomid(roomId);
            // TODO 支持cookie
            loginreqMsg.setVer(DouyuApis.config.getVer());
            loginreqMsg.setAver(DouyuApis.config.getAver());
            loginreqMsg.setUsername("visitor" + RandomUtil.randomLong(10000000, 19999999));
            loginreqMsg.setUid(RandomUtil.randomLong(1000000000, 1999999999));
            loginreqMsg.setCt(0);
            return new AuthWebSocketFrame(DouyuCodecUtil.encode(loginreqMsg));
        } catch (Exception e) {
            throw new RuntimeException("认证包创建失败，请检查房间号是否正确。roomId: %d, msg: %s".formatted(roomId, e.getMessage()));
        }
    }

    public HeartbeatWebSocketFrame createHeartbeat() {
        return new HeartbeatWebSocketFrame(DouyuCodecUtil.encode(this.getHeartbeatMsg()));
    }

    /**
     * 心跳包单例模式
     *
     * @return HeartbeatWebSocketFrame
     */
    public HeartbeatMsg getHeartbeatMsg() {
        if (heartbeatMsg == null) {
            synchronized (DouyuWebSocketFrameFactory.this) {
                if (heartbeatMsg == null) {
                    heartbeatMsg = new HeartbeatMsg();
                }
            }
        }
        return heartbeatMsg;
    }

    public WebSocketFrame createJoingroup(long roomId) {
        JoingroupMsg joingroupMsg = new JoingroupMsg();
        joingroupMsg.setRid(roomId);
        return new BinaryWebSocketFrame(DouyuCodecUtil.encode(joingroupMsg));
    }

    public WebSocketFrame createSub() {
        return new BinaryWebSocketFrame(DouyuCodecUtil.encode(new SubMsg()));
    }
}
