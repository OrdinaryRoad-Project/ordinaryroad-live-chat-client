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

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseMsgListener;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseCmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;

import java.util.List;


/**
 * 消息处理器
 *
 * @author mjz
 * @date 2023/1/4
 */
@Slf4j
public abstract class BaseBinaryFrameHandler<CmdEnum extends Enum<CmdEnum>, Msg extends IMsg, MsgListener extends IBaseMsgListener<CmdEnum>>
        extends SimpleChannelInboundHandler<BinaryWebSocketFrame>
        implements IBaseMsgListener<CmdEnum> {

    protected final MsgListener listener;

    public BaseBinaryFrameHandler(MsgListener listener) {
        this.listener = listener;
        if (listener == null) {
            log.warn("listener not set");
        }
    }

    /**
     * 解码收到的二进制流
     *
     * @param byteBuf ByteBuf
     * @return List<Msg>
     */
    protected abstract List<Msg> decode(ByteBuf byteBuf);

    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame message) {
        ByteBuf byteBuf = message.content();
        List<Msg> msgList = this.decode(byteBuf);
        for (Msg msg : msgList) {
            this.onMsg(msg);
            if (msg instanceof BaseCmdMsg<?> cmdMsg) {
                Enum<?> cmdEnum = cmdMsg.getCmdEnum();
                if (cmdEnum == null) {
                    this.onUnknownCmd(cmdMsg.getCmd(), cmdMsg);
                } else {
                    this.onCmdMsg((CmdEnum) cmdEnum, (BaseCmdMsg<CmdEnum>) cmdMsg);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause.getCause() instanceof UnrecognizedPropertyException) {
            log.error("缺少字段：{}", cause.getMessage());
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }

    @Override
    public void onMsg(IMsg msg) {
        if (this.listener != null) {
            this.listener.onMsg(msg);
        }
    }

    /**
     * 重写该方法，判断CMD，或者调用{@link IBaseMsgListener#onOtherCmdMsg(Enum, BaseCmdMsg)}
     *
     * @param cmd    CmdEnum
     * @param cmdMsg BaseMsg
     */
    @Override
    public void onCmdMsg(CmdEnum cmd, BaseCmdMsg<CmdEnum> cmdMsg) {
        if (this.listener != null) {
            this.listener.onCmdMsg(cmd, cmdMsg);
        }
    }

    @Override
    public void onUnknownCmd(String cmdString, BaseMsg msg) {
        if (this.listener != null) {
            this.listener.onUnknownCmd(cmdString, msg);
        }
    }
}
