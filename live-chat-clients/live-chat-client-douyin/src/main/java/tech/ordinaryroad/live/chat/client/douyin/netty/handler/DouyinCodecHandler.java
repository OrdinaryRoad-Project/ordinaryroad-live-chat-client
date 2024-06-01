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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.douyin.constant.DouyinPayloadTypeEnum;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.DouyinCmdMsg;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.DouyinMsg;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.base.IDouyinMsg;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.HeadersList;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.PushFrame;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Response;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BinaryWebSocketFrameToMessageCodec;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mjz
 * @date 2024/3/22
 */
@Slf4j
public class DouyinCodecHandler extends BinaryWebSocketFrameToMessageCodec<IDouyinMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IDouyinMsg msg, List<Object> out) throws Exception {
        if (msg instanceof DouyinMsg) {
            out.add(new BinaryWebSocketFrame(ctx.alloc().buffer().writeBytes(((DouyinMsg) msg).getMsg().toByteArray())));
        } else {
            throw new BaseException("暂不支持" + msg.getClass());
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame msg, List<Object> out) throws Exception {
        PushFrame pushFrame = PushFrame.parseFrom(msg.content().nioBuffer());

        byte[] bytes;
        String compressType = null;
        if (CollUtil.isNotEmpty(pushFrame.getHeadersListList())) {
            for (HeadersList headersList : pushFrame.getHeadersListList()) {
                if ("compress_type".equals(headersList.getKey())) {
                    compressType = headersList.getValue();
                }
            }
        }
        // 无压缩
        if (StrUtil.isBlank(compressType) || "none".equals(compressType)) {
            bytes = pushFrame.getPayload().toByteArray();
        }
        // gzip
        else if ("gzip".equalsIgnoreCase(compressType)) {
            ByteString payload = pushFrame.getPayload();
            bytes = ZipUtil.unGzip(payload.newInput());
        }
        // 暂不支持
        else {
            if (log.isWarnEnabled()) {
                log.warn("暂不支持的压缩方式: {}", compressType);
            }
            return;
        }

        String payloadType = pushFrame.getPayloadType();
        DouyinPayloadTypeEnum payloadTypeEnum = DouyinPayloadTypeEnum.getByCode(payloadType);
        if (payloadTypeEnum == null) {
            if (log.isDebugEnabled()) {
                log.debug("暂不支持的payloadType: {}", payloadType);
            }
            return;
        }

        switch (payloadTypeEnum) {
            case MSG: {
                Response response = Response.parseFrom(bytes);
                // ACK
                if (response.getNeedAck()) {
                    PushFrame ack = PushFrame.newBuilder()
                            .setLogId(pushFrame.getLogId())
                            .setPayloadType(DouyinPayloadTypeEnum.ACK.getCode())
                            .setPayload(response.getInternalExtBytes())
                            .build();
                    ctx.writeAndFlush(ack);
                }
                out.addAll(response.getMessagesListList().stream().map(DouyinCmdMsg::new).collect(Collectors.toList()));
                return;
            }
            default: {
                // ignore
            }
        }
    }
}
