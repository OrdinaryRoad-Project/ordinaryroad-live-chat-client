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

package tech.ordinaryroad.live.chat.client.huya.netty.frame.factory;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.huya.api.HuyaApis;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaClientTemplateTypeEnum;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaOperationEnum;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaStreamLineTypeEnum;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaWupFunctionEnum;
import tech.ordinaryroad.live.chat.client.huya.msg.GetPropsListReq;
import tech.ordinaryroad.live.chat.client.huya.msg.UserHeartBeatReq;
import tech.ordinaryroad.live.chat.client.huya.msg.WSUserInfo;
import tech.ordinaryroad.live.chat.client.huya.msg.WebSocketCommand;
import tech.ordinaryroad.live.chat.client.huya.netty.frame.AuthWebSocketFrame;
import tech.ordinaryroad.live.chat.client.huya.netty.frame.HeartbeatWebSocketFrame;
import tech.ordinaryroad.live.chat.client.huya.util.HuyaCodecUtil;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mjz
 * @date 2023/1/5
 */
public class HuyaWebSocketFrameFactory {

    private static final ConcurrentHashMap<Long, HuyaWebSocketFrameFactory> CACHE = new ConcurrentHashMap<>();

    /**
     * 浏览器地址中的房间id，支持短id
     */
    private final long roomId;
    private final JsonNode roomInfo;
    private volatile static byte[] heartbeatMsg;
    private volatile static byte[] giftListReqMsg;

    public HuyaWebSocketFrameFactory(long roomId) {
        this.roomId = roomId;
        this.roomInfo = HuyaApis.roomInit(roomId);
    }

    public synchronized static HuyaWebSocketFrameFactory getInstance(long roomId) {
        return CACHE.computeIfAbsent(roomId, aLong -> new HuyaWebSocketFrameFactory(roomId));
    }

    /**
     * 创建认证包
     *
     * @return AuthWebSocketFrame
     */
    public AuthWebSocketFrame createAuth() {
        try {
            WSUserInfo wsUserInfo = new WSUserInfo();
            wsUserInfo.setLUid(roomInfo.get("lYyid").asLong());
            wsUserInfo.setBAnonymous(roomInfo.get("lYyid").asLong() == 0);
            wsUserInfo.setLTid(roomInfo.get("lChannelId").asLong());
            wsUserInfo.setLSid(roomInfo.get("lSubChannelId").asLong());
            wsUserInfo.setLGroupId(roomInfo.get("lYyid").asLong());
            wsUserInfo.setLGroupType(3);

            WebSocketCommand webSocketCommand = new WebSocketCommand();
            webSocketCommand.setOperation(HuyaOperationEnum.EWSCmd_RegisterReq.getCode());
            webSocketCommand.setVData(wsUserInfo.toByteArray());
            return new AuthWebSocketFrame(Unpooled.wrappedBuffer(webSocketCommand.toByteArray()));
        } catch (Exception e) {
            throw new BaseException("认证包创建失败，请检查房间号是否正确。roomId: %d, msg: %s".formatted(roomId, e.getMessage()));
        }
    }

    /**
     * 创建获取礼物列表请求包
     *
     * @return BinaryWebSocketFrame
     */
    public BinaryWebSocketFrame createGiftListReq() {
        WebSocketCommand webSocketCommand = new WebSocketCommand();
        webSocketCommand.setOperation(HuyaOperationEnum.EWSCmd_WupReq.getCode());
        webSocketCommand.setVData(this.getGiftListReqMsg());
        return new BinaryWebSocketFrame(Unpooled.wrappedBuffer(webSocketCommand.toByteArray()));
    }

    public HeartbeatWebSocketFrame createHeartbeat() {
        WebSocketCommand webSocketCommand = new WebSocketCommand();
        webSocketCommand.setOperation(HuyaOperationEnum.EWSCmd_WupReq.getCode());
        webSocketCommand.setVData(this.getHeartbeatMsg());
        return new HeartbeatWebSocketFrame(Unpooled.wrappedBuffer(webSocketCommand.toByteArray()));
    }

    /**
     * 心跳包单例模式
     */
    public byte[] getHeartbeatMsg() {
        if (heartbeatMsg == null) {
            synchronized (HuyaWebSocketFrameFactory.this) {
                if (heartbeatMsg == null) {
                    UserHeartBeatReq userHeartBeatReq = new UserHeartBeatReq();
                    userHeartBeatReq.getTId().setSHuYaUA("webh5&1.0.0&websocket");
                    userHeartBeatReq.setLSid(roomInfo.get("lSubChannelId").asLong());
                    userHeartBeatReq.setLPid(roomInfo.get("lYyid").asLong());
                    userHeartBeatReq.setELineType(HuyaStreamLineTypeEnum.STREAM_LINE_WS.getCode());

                    heartbeatMsg = HuyaCodecUtil.encode("onlineui", HuyaWupFunctionEnum.OnUserHeartBeat, userHeartBeatReq);
                }
            }
        }
        return heartbeatMsg;
    }

    /**
     * 礼物列表请求包单例模式
     */
    public byte[] getGiftListReqMsg() {
        if (giftListReqMsg == null) {
            synchronized (HuyaWebSocketFrameFactory.this) {
                if (giftListReqMsg == null) {
                    GetPropsListReq getPropsListReq = new GetPropsListReq();
                    getPropsListReq.getTUserId().setLUid(roomInfo.get("lYyid").asLong());
                    getPropsListReq.getTUserId().setSHuYaUA("webh5&1.0.0&websocket");
                    getPropsListReq.setITemplateType(HuyaClientTemplateTypeEnum.TPL_MIRROR.getCode());

                    giftListReqMsg = HuyaCodecUtil.encode("PropsUIServer", HuyaWupFunctionEnum.getPropsList, getPropsListReq);
                }
            }
        }
        return giftListReqMsg;
    }

}
