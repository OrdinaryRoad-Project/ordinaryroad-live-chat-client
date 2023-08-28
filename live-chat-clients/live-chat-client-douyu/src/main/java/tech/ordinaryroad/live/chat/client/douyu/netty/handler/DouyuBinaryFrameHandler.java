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

package tech.ordinaryroad.live.chat.client.douyu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.msg.BaseCmdMsg;
import tech.ordinaryroad.live.chat.client.douyu.constant.DouyuCmdEnum;
import tech.ordinaryroad.live.chat.client.douyu.listener.IDouyuDouyuCmdMsgListener;
import tech.ordinaryroad.live.chat.client.douyu.msg.DouyuCmdMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.LoginresMsg;
import tech.ordinaryroad.live.chat.client.douyu.msg.base.IDouyuMsg;
import tech.ordinaryroad.live.chat.client.douyu.util.DouyuCodecUtil;
import tech.ordinaryroad.live.chat.client.servers.netty.handler.base.BaseBinaryFrameHandler;

import java.util.List;


/**
 * 消息处理器
 *
 * @author mjz
 * @date 2023/1/4
 */
@Slf4j
@ChannelHandler.Sharable
public class DouyuBinaryFrameHandler extends BaseBinaryFrameHandler<DouyuCmdEnum, IDouyuMsg, IDouyuDouyuCmdMsgListener> {

    public DouyuBinaryFrameHandler(IDouyuDouyuCmdMsgListener listener) {
        super(listener);
    }

    @Override
    public void onCmdMsg(DouyuCmdEnum cmd, BaseCmdMsg<DouyuCmdEnum> cmdMsg) {
        super.onCmdMsg(cmd, cmdMsg);

        if (super.listener == null) {
            return;
        }

        if (!(cmdMsg instanceof DouyuCmdMsg)) {
            return;
        }

        DouyuCmdMsg douyuCmdMsg = (DouyuCmdMsg) cmdMsg;
        switch (cmd) {
            case chatmsg -> listener.onDanmuMsg(douyuCmdMsg);
            default -> super.listener.onOtherCmdMsg(cmd, cmdMsg);
        }
    }

    @Override
    protected List<IDouyuMsg> decode(ByteBuf byteBuf) {
        return DouyuCodecUtil.decode(byteBuf);
    }
}
