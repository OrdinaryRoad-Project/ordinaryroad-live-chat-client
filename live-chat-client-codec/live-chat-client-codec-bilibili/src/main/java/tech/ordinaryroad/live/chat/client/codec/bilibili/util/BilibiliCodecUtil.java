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

package tech.ordinaryroad.live.chat.client.codec.bilibili.util;

import cn.hutool.core.util.StrUtil;
import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.BrotliInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.OperationEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.ProtoverEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.*;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.base.BaseBilibiliMsg;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.base.IBilibiliMsg;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * @author mjz
 * @date 2023/1/6
 */
@Slf4j
public class BilibiliCodecUtil {

    public static final short FRAME_HEADER_LENGTH = 16;
    public static int sequence = 0;

    public static ByteBuf encode(IBilibiliMsg msg, ByteBuf out) {
        String bodyJsonString = StrUtil.EMPTY;
        // HeartbeatMsg不需要正文，如果序列化后得到`{}`，则替换为空字符串
        if (!(msg instanceof HeartbeatMsg)) {
            bodyJsonString = msg.toString();
            if (StrUtil.EMPTY_JSON.equals(bodyJsonString)) {
                bodyJsonString = StrUtil.EMPTY;
            }
        }
        byte[] bodyBytes = bodyJsonString.getBytes(StandardCharsets.UTF_8);
        int length = bodyBytes.length + FRAME_HEADER_LENGTH;
        out.writeInt(length);
        out.writeShort(FRAME_HEADER_LENGTH);
        out.writeShort(msg.getProtoverEnum().getCode());
        out.writeInt(msg.getOperationEnum().getCode());
        out.writeInt(++sequence);
        out.writeBytes(bodyBytes);
        return out;
    }

    public static ByteBuf encode(IBilibiliMsg msg) {
        return encode(msg, ByteBufAllocator.DEFAULT.buffer(FRAME_HEADER_LENGTH));
    }

    public static List<IBilibiliMsg> decode(ByteBuf in, List<IBilibiliMsg> msgList) {
        Queue<ByteBuf> pendingByteBuf = new LinkedList<>();

        do {
            Optional<IBilibiliMsg> msg = doDecode(in, pendingByteBuf);
            msg.ifPresent(msgList::add);
            in = pendingByteBuf.poll();
        } while (in != null);

        return msgList;
    }

    public static List<IBilibiliMsg> decode(ByteBuf in) {
        return decode(in, new ArrayList<>());
    }

    /**
     * 执行解码操作，有压缩则先解压，解压后可能得到多条消息
     *
     * @param in             handler收到的一条消息
     * @param pendingByteBuf 用于存放未读取完的ByteBuf
     * @return Optional<IBilibiliMsg> 何时为空值：不支持的{@link OperationEnum}，不支持的{@link ProtoverEnum}，{@link #parse(OperationEnum, String)}反序列化失败
     * @see OperationEnum
     * @see ProtoverEnum
     */
    private static Optional<IBilibiliMsg> doDecode(ByteBuf in, Queue<ByteBuf> pendingByteBuf) {
        int length = in.readInt();
        short frameHeaderLength = in.readShort();
        short protoverCode = in.readShort();
        int operationCode = in.readInt();
        int sequence = in.readInt();
        int contentLength = length - frameHeaderLength;
        byte[] inputBytes = new byte[contentLength];
        in.readBytes(inputBytes);
        if (in.readableBytes() != 0) {
            // log.error("in.readableBytes() {}", in.readableBytes());
            pendingByteBuf.offer(in);
        }

        OperationEnum operationEnum = OperationEnum.getByCode(operationCode);
        if (operationEnum == null) {
            throw new BaseException(String.format("未知operation: %d", operationCode));
        }
        if (protoverCode == ProtoverEnum.NORMAL_ZLIB.getCode()) {
            switch (operationEnum) {
                case MESSAGE: {
                    // Decompress the bytes
                    Inflater inflater = new Inflater();
                    inflater.reset();
                    inflater.setInput(inputBytes);
                    // TODO 改用池化，并处理内存泄漏问题
                    ByteBuf buffer = Unpooled.buffer();
                    try {
                        byte[] bytes = new byte[1024];
                        while (!inflater.finished()) {
                            int count = inflater.inflate(bytes);
                            buffer.writeBytes(bytes, 0, count);
                        }
                    } catch (DataFormatException e) {
                        throw new BaseException(e);
                    } finally {
                        inflater.end();
                    }
                    return doDecode(buffer, pendingByteBuf);
                }
                case HEARTBEAT_REPLY: {
                    BigInteger bigInteger = new BigInteger(inputBytes);
                    return parse(operationEnum, String.format("{\"popularity\":%d}", bigInteger));
                }
                default: {
                    String s = new String(inputBytes, StandardCharsets.UTF_8);
                    return parse(operationEnum, s);
                }
            }
        } else if (protoverCode == ProtoverEnum.NORMAL_NO_COMPRESSION.getCode()) {
            switch (operationEnum) {
                case HEARTBEAT_REPLY: {
                    BigInteger bigInteger = new BigInteger(inputBytes);
                    return parse(operationEnum, String.format("{\"popularity\":%d}", bigInteger));
                }
                default: {
                    String s = new String(inputBytes, StandardCharsets.UTF_8);
                    return parse(operationEnum, s);
                }
            }
        } else if (protoverCode == ProtoverEnum.HEARTBEAT_AUTH_NO_COMPRESSION.getCode()) {
            switch (operationEnum) {
                case HEARTBEAT_REPLY: {
                    BigInteger bigInteger = new BigInteger(inputBytes);
                    return parse(operationEnum, String.format("{\"popularity\":%d}", bigInteger));
                }
                default: {
                    String s = new String(inputBytes, StandardCharsets.UTF_8);
                    return parse(operationEnum, s);
                }
            }
        } else if (protoverCode == ProtoverEnum.NORMAL_BROTLI.getCode()) {
            switch (operationEnum) {
                case MESSAGE: {
                    // Load the native library
                    Brotli4jLoader.ensureAvailability();

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
                    // TODO 改用池化，并处理内存泄漏问题
                    ByteBuf buffer = Unpooled.buffer();
                    byte[] bytes = new byte[1024];
                    BrotliInputStream brotliInputStream = null;
                    try {
                        brotliInputStream = new BrotliInputStream(byteArrayInputStream);
                        int count;
                        while ((count = brotliInputStream.read(bytes)) > -1) {
                            buffer.writeBytes(bytes, 0, count);
                        }
                    } catch (IOException e) {
                        throw new BaseException(e);
                    } finally {
                        try {
                            // Close the BrotliInputStream. This also closes the InputStream.
                            if (brotliInputStream != null) {
                                brotliInputStream.close();
                            }
                        } catch (IOException e) {
                            log.error("解压失败", e);
                        }
                    }
                    return doDecode(buffer, pendingByteBuf);
                }
                case HEARTBEAT_REPLY: {
                    BigInteger bigInteger = new BigInteger(inputBytes);
                    return parse(operationEnum, String.format("{\"popularity\":%d}", bigInteger));
                }
                default: {
                    String s = new String(inputBytes, StandardCharsets.UTF_8);
                    return parse(operationEnum, s);
                }
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("暂不支持的版本：{}", protoverCode);
            }
            return Optional.empty();
        }
    }

    public static Optional<IBilibiliMsg> parse(OperationEnum operation, String jsonString) {
        switch (operation) {
            case MESSAGE: {
                try {
                    return Optional.ofNullable(BaseBilibiliMsg.OBJECT_MAPPER.readValue(jsonString, MessageMsg.class));
                } catch (JsonProcessingException e) {
                    throw new BaseException(e);
                }
            }
            case CONNECT_SUCCESS: {
                try {
                    return Optional.ofNullable(BaseBilibiliMsg.OBJECT_MAPPER.readValue(jsonString, ConnectSuccessMsg.class));
                } catch (JsonProcessingException e) {
                    throw new BaseException(e);
                }
            }
            case HEARTBEAT_REPLY: {
                try {
                    return Optional.ofNullable(BaseBilibiliMsg.OBJECT_MAPPER.readValue(jsonString, HeartbeatReplyMsg.class));
                } catch (JsonProcessingException e) {
                    throw new BaseException(e);
                }
            }
            case USER_AUTHENTICATION: {
                try {
                    return Optional.ofNullable(BaseBilibiliMsg.OBJECT_MAPPER.readValue(jsonString, UserAuthenticationMsg.class));
                } catch (JsonProcessingException e) {
                    throw new BaseException(e);
                }
            }
            case HEARTBEAT: {
                try {
                    return Optional.ofNullable(BaseBilibiliMsg.OBJECT_MAPPER.readValue(jsonString, HeartbeatMsg.class));
                } catch (JsonProcessingException e) {
                    HeartbeatMsg heartbeatMsg = new HeartbeatMsg();
                    return Optional.of(heartbeatMsg);
                    // throw new BaseException(e);
                }
            }
            default: {
                if (log.isWarnEnabled()) {
                    log.warn("暂不支持 {}", operation);
                }
                return Optional.empty();
            }
        }
    }

}
