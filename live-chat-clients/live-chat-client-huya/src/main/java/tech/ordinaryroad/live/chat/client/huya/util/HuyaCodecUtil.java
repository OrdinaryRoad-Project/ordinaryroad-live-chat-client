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

package tech.ordinaryroad.live.chat.client.huya.util;

import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsStructBase;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaOperationEnum;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaWupFunctionEnum;
import tech.ordinaryroad.live.chat.client.huya.msg.PushMessage;
import tech.ordinaryroad.live.chat.client.huya.msg.WebSocketCommand;
import tech.ordinaryroad.live.chat.client.huya.msg.WupRsp;
import tech.ordinaryroad.live.chat.client.huya.msg.base.IHuyaMsg;
import tech.ordinaryroad.live.chat.client.huya.msg.req.WupReq;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author mjz
 * @date 2023/9/5
 */
@Slf4j
public class HuyaCodecUtil {

    public static List<IHuyaMsg> decode(ByteBuf in) {
        List<IHuyaMsg> msgList = new ArrayList<>();
        Queue<ByteBuf> pendingByteBuf = new LinkedList<>();

        do {
            Optional<IHuyaMsg> msg = doDecode(in, pendingByteBuf);
            msg.ifPresent(msgList::add);
            in = pendingByteBuf.poll();
        } while (in != null);

        return msgList;
    }

    /**
     * 执行解码操作，有压缩则先解压，解压后可能得到多条消息
     *
     * @param in             handler收到的一条消息
     * @param pendingByteBuf 用于存放未读取完的ByteBuf
     */
    private static Optional<IHuyaMsg> doDecode(ByteBuf in, Queue<ByteBuf> pendingByteBuf) {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);

        WebSocketCommand webSocketCommand = new WebSocketCommand(newUtf8TarsInputStream(bytes));
        HuyaOperationEnum operationEnum = webSocketCommand.getOperationEnum();
        if (operationEnum == null) {
            throw new BaseException("未知operation: %d".formatted(webSocketCommand.getOperation()));
        }

        switch (operationEnum) {
            case EWSCmd_WupRsp -> {
                return Optional.of(new WupRsp(webSocketCommand.getVData()));
            }
            case EWSCmdS2C_MsgPushReq -> {
                return Optional.of(new PushMessage(newUtf8TarsInputStream(webSocketCommand.getVData())));
            }
            default -> {
                return Optional.of(webSocketCommand);
            }
        }
    }

    public static byte[] encode(String servantName, HuyaWupFunctionEnum function, TarsStructBase req) {
        WupReq wupReq = new WupReq();
        wupReq.getTarsServantRequest().setServantName(servantName);
        wupReq.getTarsServantRequest().setFunctionName(function.name());
        wupReq.getUniAttribute().put("tReq", req);
        return wupReq.encode();
    }

    public static TarsInputStream newUtf8TarsInputStream(byte[] bytes) {
        TarsInputStream tarsInputStream = new TarsInputStream(bytes);
        tarsInputStream.setServerEncoding(StandardCharsets.UTF_8.name());
        return tarsInputStream;
    }
}
