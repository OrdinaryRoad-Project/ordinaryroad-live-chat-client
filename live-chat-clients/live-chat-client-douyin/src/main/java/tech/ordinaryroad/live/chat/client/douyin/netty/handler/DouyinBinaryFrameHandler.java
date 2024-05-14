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

package tech.ordinaryroad.live.chat.client.douyin.netty.handler;

import cn.hutool.core.util.NumberUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.douyin.api.DouyinApis;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinCmdEnum;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.*;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.base.IDouyinMsg;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.*;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.douyin.client.DouyinLiveChatClient;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientBinaryFrameHandler;

import java.io.IOException;
import java.util.List;

/**
 * @author mjz
 * @date 2024/1/2
 */
@Slf4j
@ChannelHandler.Sharable
public class DouyinBinaryFrameHandler extends BaseNettyClientBinaryFrameHandler<DouyinLiveChatClient, DouyinBinaryFrameHandler, DouyinCmdEnum, IDouyinMsg, IDouyinMsgListener> {

    private ChannelHandlerContext channelHandlerContext;

    public DouyinBinaryFrameHandler(List<IDouyinMsgListener> iDouyinMsgListeners, DouyinLiveChatClient client) {
        super(iDouyinMsgListeners, client);
    }

    public DouyinBinaryFrameHandler(List<IDouyinMsgListener> iDouyinMsgListeners, long roomId) {
        super(iDouyinMsgListeners, roomId);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        channelHandlerContext = ctx;
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        channelHandlerContext = null;
    }

    @Override
    public void onCmdMsg(DouyinCmdEnum cmd, ICmdMsg<DouyinCmdEnum> cmdMsg) {
        if (super.msgListeners.isEmpty()) {
            return;
        }

        ByteString payload = ((DouyinCmdMsg) cmdMsg).getMsg().getPayload();
        switch (cmd) {
            case WebcastChatMessage: {
                try {
                    DouyinWebcastChatMessageMsgOuterClass.DouyinWebcastChatMessageMsg douyinWebcastChatMessageMsg = DouyinWebcastChatMessageMsgOuterClass.DouyinWebcastChatMessageMsg.parseFrom(payload);
                    DouyinDanmuMsg msg = new DouyinDanmuMsg(douyinWebcastChatMessageMsg);
                    iteratorMsgListeners(msgListener -> msgListener.onDanmuMsg(DouyinBinaryFrameHandler.this, msg));
                } catch (IOException e) {
                    throw new BaseException(e);
                }
                break;
            }
            case WebcastGiftMessage: {
                try {
                    DouyinWebcastGiftMessageMsgOuterClass.DouyinWebcastGiftMessageMsg douyinWebcastGiftMessageMsg = DouyinWebcastGiftMessageMsgOuterClass.DouyinWebcastGiftMessageMsg.parseFrom(payload);
                    DouyinGiftMsg msg = new DouyinGiftMsg(douyinWebcastGiftMessageMsg);
                    // 计算礼物个数
                    DouyinApis.calculateGiftCount(msg);
                    iteratorMsgListeners(msgListener -> msgListener.onGiftMsg(DouyinBinaryFrameHandler.this, msg));
                } catch (InvalidProtocolBufferException e) {
                    throw new BaseException(e);
                }
                break;
            }
            case WebcastMemberMessage: {
                try {
                    DouyinWebcastMemberMessageMsgOuterClass.DouyinWebcastMemberMessageMsg douyinWebcastMemberMessageMsg = DouyinWebcastMemberMessageMsgOuterClass.DouyinWebcastMemberMessageMsg.parseFrom(payload);
                    DouyinEnterRoomMsg msg = new DouyinEnterRoomMsg(douyinWebcastMemberMessageMsg);
                    iteratorMsgListeners(msgListener -> msgListener.onEnterRoomMsg(DouyinBinaryFrameHandler.this, msg));
                } catch (InvalidProtocolBufferException e) {
                    throw new BaseException(e);
                }
                break;
            }
            case WebcastLikeMessage: {
                try {
                    DouyinWebcastLikeMessageMsgOuterClass.DouyinWebcastLikeMessageMsg douyinWebcastLikeMessageMsg = DouyinWebcastLikeMessageMsgOuterClass.DouyinWebcastLikeMessageMsg.parseFrom(payload);
                    DouyinLikeMsg msg = new DouyinLikeMsg(douyinWebcastLikeMessageMsg);

                    DouyinRoomStatsMsg douyinRoomStatsMsg = new DouyinRoomStatsMsg();
                    douyinRoomStatsMsg.setLikedCount(NumberUtil.toStr(douyinWebcastLikeMessageMsg.getTotal()));

                    iteratorMsgListeners(msgListener -> {
                        msgListener.onLikeMsg(DouyinBinaryFrameHandler.this, msg);
                        msgListener.onRoomStatsMsg(DouyinBinaryFrameHandler.this, douyinRoomStatsMsg);
                    });
                } catch (InvalidProtocolBufferException e) {
                    throw new BaseException(e);
                }
                break;
            }

            case WebcastControlMessage: {
                try {
                    DouyinWebcastControlMessageMsgOuterClass.DouyinWebcastControlMessageMsg douyinWebcastControlMessageMsg = DouyinWebcastControlMessageMsgOuterClass.DouyinWebcastControlMessageMsg.parseFrom(payload);
                    DouyinControlMsg msg = new DouyinControlMsg(douyinWebcastControlMessageMsg);
                    iteratorMsgListeners(msgListener -> msgListener.onLiveStatusMsg(DouyinBinaryFrameHandler.this, msg));
                } catch (InvalidProtocolBufferException e) {
                    throw new BaseException(e);
                }
                break;
            }

            case WebcastSocialMessage: {
                try {
                    DouyinWebcastSocialMessageMsgOuterClass.DouyinWebcastSocialMessageMsg douyinWebcastSocialMessageMsg = DouyinWebcastSocialMessageMsgOuterClass.DouyinWebcastSocialMessageMsg.parseFrom(payload);
                    DouyinSocialMsg msg = new DouyinSocialMsg(douyinWebcastSocialMessageMsg);
                    iteratorMsgListeners(msgListener -> msgListener.onSocialMsg(DouyinBinaryFrameHandler.this, msg));
                } catch (InvalidProtocolBufferException e) {
                    throw new BaseException(e);
                }
                break;
            }

            case WebcastRoomStatsMessage: {
                try {
                    DouyinWebcastRoomStatsMessageMsgOuterClass.DouyinWebcastRoomStatsMessageMsg douyinWebcastRoomStatsMessageMsg = DouyinWebcastRoomStatsMessageMsgOuterClass.DouyinWebcastRoomStatsMessageMsg.parseFrom(payload);
                    DouyinRoomStatsMsg douyinRoomStatsMsg = new DouyinRoomStatsMsg();
                    douyinRoomStatsMsg.setMsg(douyinWebcastRoomStatsMessageMsg);
                    iteratorMsgListeners(msgListener -> msgListener.onRoomStatsMsg(DouyinBinaryFrameHandler.this, douyinRoomStatsMsg));
                } catch (InvalidProtocolBufferException e) {
                    throw new BaseException(e);
                }
                break;
            }
            default: {
                iteratorMsgListeners(msgListener -> msgListener.onOtherCmdMsg(DouyinBinaryFrameHandler.this, cmd, cmdMsg));
            }
        }
    }
}
