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

package tech.ordinaryroad.live.chat.client.huya.netty.handler;

import com.qq.tars.protocol.tars.TarsInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.huya.api.HuyaApis;
import tech.ordinaryroad.live.chat.client.huya.client.HuyaLiveChatClient;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaCmdEnum;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaOperationEnum;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaWupFunctionEnum;
import tech.ordinaryroad.live.chat.client.huya.listener.IHuyaMsgListener;
import tech.ordinaryroad.live.chat.client.huya.msg.MessageNoticeMsg;
import tech.ordinaryroad.live.chat.client.huya.msg.PushMessage;
import tech.ordinaryroad.live.chat.client.huya.msg.SendItemSubBroadcastPacketMsg;
import tech.ordinaryroad.live.chat.client.huya.msg.WupRsp;
import tech.ordinaryroad.live.chat.client.huya.msg.base.IHuyaMsg;
import tech.ordinaryroad.live.chat.client.huya.msg.dto.PropsItem;
import tech.ordinaryroad.live.chat.client.huya.msg.req.GetPropsListRsp;
import tech.ordinaryroad.live.chat.client.huya.netty.frame.factory.HuyaWebSocketFrameFactory;
import tech.ordinaryroad.live.chat.client.huya.util.HuyaCodecUtil;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientBinaryFrameHandler;

import java.util.List;


/**
 * 消息处理器
 *
 * @author mjz
 * @date 2023/9/5
 */
@Slf4j
@ChannelHandler.Sharable
public class HuyaBinaryFrameHandler extends BaseNettyClientBinaryFrameHandler<HuyaLiveChatClient, HuyaBinaryFrameHandler, HuyaCmdEnum, IHuyaMsg, IHuyaMsgListener> {

    private ChannelHandlerContext channelHandlerContext;

    public HuyaBinaryFrameHandler(List<IHuyaMsgListener> iHuyaMsgListeners, HuyaLiveChatClient client, long roomId) {
        super(iHuyaMsgListeners, client, roomId);
    }

    public HuyaBinaryFrameHandler(List<IHuyaMsgListener> iHuyaMsgListeners, HuyaLiveChatClient client) {
        super(iHuyaMsgListeners, client);
    }

    public HuyaBinaryFrameHandler(List<IHuyaMsgListener> iHuyaMsgListeners, long roomId) {
        super(iHuyaMsgListeners, roomId);
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
    public void onMsg(IMsg msg) {
        IHuyaMsg iHuyaMsg = (IHuyaMsg) msg;
        HuyaOperationEnum operationEnum = iHuyaMsg.getOperationEnum();
        if (operationEnum == HuyaOperationEnum.EWSCmd_RegisterRsp) {
            // 获取礼物列表
            if (channelHandlerContext == null) {
                log.error("channelHandlerContext is null, cannot get gift list");
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug("获取礼物列表");
            }
            channelHandlerContext.writeAndFlush(HuyaWebSocketFrameFactory.getInstance(getRoomId()).createGiftListReq());
        } else if (operationEnum == HuyaOperationEnum.EWSCmd_WupRsp) {
            WupRsp wupRsp = (WupRsp) msg;
            String functionName = wupRsp.getTarsServantRequest().getFunctionName();
            HuyaWupFunctionEnum wupFunctionEnum = HuyaWupFunctionEnum.getByName(functionName);
            if (wupFunctionEnum == null) {
                if (log.isWarnEnabled()) {
                    log.warn("未知 function {}", functionName);
                }
                return;
            }

            switch (wupFunctionEnum) {
                case getPropsList -> {
                    GetPropsListRsp getPropsListRsp = new GetPropsListRsp();
                    getPropsListRsp = wupRsp.getUniAttribute().getByClass("tRsp", getPropsListRsp);
                    for (PropsItem propsItem : getPropsListRsp.getVPropsItemList()) {
                        HuyaApis.GIFT_ITEMS.put(propsItem.getIPropsId(), propsItem);
                    }
                }
                default -> {
                    if (log.isDebugEnabled()) {
                        log.debug("暂不支持 function {}", wupFunctionEnum);
                    }
                }
            }
        }
    }

    @Override
    public void onCmdMsg(HuyaCmdEnum cmd, ICmdMsg<HuyaCmdEnum> cmdMsg) {
        if (super.msgListeners.isEmpty()) {
            return;
        }

        PushMessage pushMessage = (PushMessage) cmdMsg;
        TarsInputStream tarsInputStream = HuyaCodecUtil.newUtf8TarsInputStream(pushMessage.getDataBytes());
        switch (cmd) {
            case MessageNotice -> {
                MessageNoticeMsg messageNoticeMsg = new MessageNoticeMsg(tarsInputStream);
                iteratorMsgListeners(msgListener -> msgListener.onDanmuMsg(HuyaBinaryFrameHandler.this, messageNoticeMsg));
            }
            case SendItemSubBroadcastPacket -> {
                SendItemSubBroadcastPacketMsg sendItemSubBroadcastPacketMsg = new SendItemSubBroadcastPacketMsg(tarsInputStream);
                sendItemSubBroadcastPacketMsg.setPropsItem(HuyaApis.GIFT_ITEMS.getOrDefault(sendItemSubBroadcastPacketMsg.getIItemType(), PropsItem.DEFAULT));
                iteratorMsgListeners(msgListener -> msgListener.onGiftMsg(HuyaBinaryFrameHandler.this, sendItemSubBroadcastPacketMsg));
            }
            default ->
                    iteratorMsgListeners(msgListener -> msgListener.onOtherCmdMsg(HuyaBinaryFrameHandler.this, cmd, cmdMsg));
        }
    }

    @Override
    protected List<IHuyaMsg> decode(ByteBuf byteBuf) {
        return HuyaCodecUtil.decode(byteBuf);
    }
}
