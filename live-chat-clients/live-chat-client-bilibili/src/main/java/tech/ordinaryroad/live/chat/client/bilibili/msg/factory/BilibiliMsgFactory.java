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

package tech.ordinaryroad.live.chat.client.bilibili.msg.factory;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.NumberUtil;
import tech.ordinaryroad.live.chat.client.bilibili.api.BilibiliApis;
import tech.ordinaryroad.live.chat.client.bilibili.constant.ProtoverEnum;
import tech.ordinaryroad.live.chat.client.bilibili.msg.HeartbeatMsg;
import tech.ordinaryroad.live.chat.client.bilibili.msg.userAuthenticationMsg;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author mjz
 * @date 2023/1/5
 */
public class BilibiliMsgFactory {

    private static final TimedCache<Long, BilibiliMsgFactory> FACTORY_CACHE = new TimedCache<>(TimeUnit.DAYS.toMillis(1), new ConcurrentHashMap<>());
    private static final TimedCache<ProtoverEnum, HeartbeatMsg> HEARTBEAT_MSG_CACHE = new TimedCache<>(TimeUnit.DAYS.toMillis(1), new ConcurrentHashMap<>());

    /**
     * 浏览器地址中的房间id，支持短id
     */
    private final long roomId;

    public BilibiliMsgFactory(long roomId) {
        this.roomId = roomId;
    }

    public static BilibiliMsgFactory getInstance(long roomId) {
        if (!FACTORY_CACHE.containsKey(roomId)) {
            FACTORY_CACHE.put(roomId, new BilibiliMsgFactory(roomId));
        }
        return FACTORY_CACHE.get(roomId);
    }

    /**
     * 创建认证包
     *
     * @param protover {@link ProtoverEnum}
     * @return AuthWebSocketFrame
     */
    public userAuthenticationMsg createAuth(ProtoverEnum protover, BilibiliApis.RoomInitResult roomInitResult) {
        try {
            String buvid3 = roomInitResult.getBuvid3();
            long realRoomId = roomInitResult.getRoomPlayInfoResult().getRoom_id();
            userAuthenticationMsg userAuthenticationMsg = new userAuthenticationMsg(realRoomId, protover.getCode(), buvid3, roomInitResult.getDanmuinfoResult().getToken());
            userAuthenticationMsg.setUid(NumberUtil.parseLong(roomInitResult.getUid()));
            return userAuthenticationMsg;
        } catch (Exception e) {
            throw new BaseException(String.format("认证包创建失败，请检查房间号是否正确。roomId: %d, msg: %s", roomId, e.getMessage()));
        }
    }

    /**
     * 创建心跳包
     *
     * @param protover {@link ProtoverEnum}
     * @return AuthWebSocketFrame
     */
    public HeartbeatMsg createHeartbeat(ProtoverEnum protover) {
        if (!HEARTBEAT_MSG_CACHE.containsKey(protover)) {
            HEARTBEAT_MSG_CACHE.put(protover, new HeartbeatMsg(protover.getCode()));
        }
        return HEARTBEAT_MSG_CACHE.get(protover);
    }
}
