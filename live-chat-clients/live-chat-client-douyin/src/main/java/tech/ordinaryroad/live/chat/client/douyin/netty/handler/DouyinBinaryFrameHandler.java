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

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.douyin.api.DouyinApis;
import tech.ordinaryroad.live.chat.client.douyin.client.DouyinLiveChatClient;
import tech.ordinaryroad.live.chat.client.douyin.constant.DouyinCmdEnum;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener;
import tech.ordinaryroad.live.chat.client.douyin.msg.*;
import tech.ordinaryroad.live.chat.client.douyin.msg.base.IDouyinMsg;
import tech.ordinaryroad.live.chat.client.douyin.protobuf.*;
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

        ByteString payload = ((douyin_cmd_msg) cmdMsg).getPayload();
        switch (cmd) {
            case WebcastChatMessage: {
                try {
                    douyin_webcast_chat_message_msg douyinWebcastChatMessageMsg = douyin_webcast_chat_message_msg.parseFrom(payload);
                    iteratorMsgListeners(msgListener -> msgListener.onDanmuMsg(DouyinBinaryFrameHandler.this, new DouyinDanmuMsg(douyinWebcastChatMessageMsg)));
                } catch (IOException e) {
                    throw new BaseException(e);
                }
                break;
            }
            case WebcastGiftMessage: {
                try {
                    douyin_webcast_gift_message_msg douyinWebcastGiftMessageMsg = douyin_webcast_gift_message_msg.parseFrom(payload);
                    iteratorMsgListeners(msgListener -> {
                        DouyinGiftMsg msg = new DouyinGiftMsg(douyinWebcastGiftMessageMsg);
                        // 计算礼物个数
                        DouyinApis.calculateGiftCount(msg);
                        msgListener.onGiftMsg(DouyinBinaryFrameHandler.this, msg);
                    });
                } catch (InvalidProtocolBufferException e) {
                    throw new BaseException(e);
                }
                break;
            }
            case WebcastMemberMessage: {
                try {
                    douyin_webcast_member_message_msg douyinWebcastMemberMessageMsg = douyin_webcast_member_message_msg.parseFrom(payload);
                    iteratorMsgListeners(msgListener -> msgListener.onEnterRoomMsg(DouyinBinaryFrameHandler.this, new DouyinEnterRoomMsg(douyinWebcastMemberMessageMsg)));
                } catch (InvalidProtocolBufferException e) {
                    throw new BaseException(e);
                }
                break;
            }
            case WebcastLikeMessage: {
                try {
                    douyin_webcast_like_message_msg douyinWebcastLikeMessageMsg = douyin_webcast_like_message_msg.parseFrom(payload);
                    iteratorMsgListeners(msgListener -> msgListener.onLikeMsg(DouyinBinaryFrameHandler.this, new DouyinLikeMsg(douyinWebcastLikeMessageMsg)));
                } catch (InvalidProtocolBufferException e) {
                    throw new BaseException(e);
                }
                break;
            }

            case WebcastControlMessage: {
                try {
                    douyin_webcast_control_message_msg douyinWebcastControlMessageMsg = douyin_webcast_control_message_msg.parseFrom(payload);
                    iteratorMsgListeners(msgListener -> msgListener.onLiveStatusMsg(DouyinBinaryFrameHandler.this, new DouyinControlMsg(douyinWebcastControlMessageMsg)));
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
