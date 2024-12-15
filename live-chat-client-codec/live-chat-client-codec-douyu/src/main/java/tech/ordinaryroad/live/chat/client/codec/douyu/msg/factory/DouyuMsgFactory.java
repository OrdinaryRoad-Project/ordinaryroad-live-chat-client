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

package tech.ordinaryroad.live.chat.client.codec.douyu.msg.factory;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.buffer.ByteBuf;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.DouyuApis;
import tech.ordinaryroad.live.chat.client.codec.douyu.constant.DouyuClientModeEnum;
import tech.ordinaryroad.live.chat.client.codec.douyu.msg.*;
import tech.ordinaryroad.live.chat.client.codec.douyu.msg.base.BaseDouyuCmdMsg;
import tech.ordinaryroad.live.chat.client.codec.douyu.util.DouyuCodecUtil;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.util.OrLiveChatCookieUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author mjz
 * @date 2023/1/5
 */
public class DouyuMsgFactory {

    private static final TimedCache<Long, DouyuMsgFactory> FACTORY_CACHE = new TimedCache<>(TimeUnit.DAYS.toMillis(1), new ConcurrentHashMap<>());
    /**
     * 浏览器地址中的房间id，暂不支持短id
     */
    private final long roomId;
    private volatile static HeartbeatMsg heartbeatMsg;

    public DouyuMsgFactory(long roomId) {
        this.roomId = roomId;
    }

    public static DouyuMsgFactory getInstance(long roomId) {
        if (!FACTORY_CACHE.containsKey(roomId)) {
            FACTORY_CACHE.put(roomId, new DouyuMsgFactory(roomId));
        }
        return FACTORY_CACHE.get(roomId);
    }

    /**
     * 创建认证包
     *
     * @param mode   {@link DouyuClientModeEnum#DANMU}, {@link DouyuClientModeEnum#WS}
     * @param ver    VER
     * @param aver   AVER
     * @param cookie 浏览器Cookie，发送弹幕时必传
     * @return AuthWebSocketFrame
     */
    public ByteBuf createAuth(DouyuApis.RoomInitResult roomInitResult, DouyuClientModeEnum mode, String ver, String aver, String cookie) {
        try {
            // type@=loginreq/roomid@=7750753/dfl@=/username@=visitor10424697/uid@=1168052601/ver@=20220825/aver@=218101901/ct@=0/
            LoginreqMsg loginreqMsg;
            long realRoomId = roomInitResult.getRealRoomId();
            long uid;
            String username;
            Map<String, String> cookieMap = OrLiveChatCookieUtil.parseCookieString(cookie);

            if (cookieMap.isEmpty()) {
                // 视为未登录
                if (mode == DouyuClientModeEnum.DANMU) {
                    uid = RandomUtil.randomInt(10000, 19999);
                    username = "visitor" + RandomUtil.randomInt(1000000000, 1999999999);
                    loginreqMsg = new LoginreqMsg(realRoomId, "", username, uid, ver, aver);
                    return DouyuCodecUtil.encode(loginreqMsg, LoginreqMsg.SHOULD_IGNORE_PROPERTIES_WHEN_NOT_LOGGED_IN);
                } else {
                    loginreqMsg = new LoginreqMsg(realRoomId, "", "", ver, aver, "", "", "", UUID.fastUUID().toString(true));
                    return DouyuCodecUtil.encode(loginreqMsg, LoginreqMsg.SHOULD_IGNORE_PROPERTIES_WHEN_LOGGED_IN);
                }
            }
            // 视为登录
            else {
                String acfUid = OrLiveChatCookieUtil.getCookieByName(cookieMap, DouyuApis.KEY_COOKIE_ACF_UID, () -> {
                    throw new BaseException("Cookie中缺少字段" + DouyuApis.KEY_COOKIE_ACF_UID);
                });
                uid = NumberUtil.parseLong(acfUid);
                username = acfUid;
                String dfl = "sn@A=105@Sss@A=1";
                if (mode == DouyuClientModeEnum.DANMU) {
                    loginreqMsg = new LoginreqMsg(realRoomId, dfl, username, uid, ver, aver);
                    return DouyuCodecUtil.encode(loginreqMsg, LoginreqMsg.SHOULD_IGNORE_PROPERTIES_WHEN_NOT_LOGGED_IN);
                } else {
                    String acfLtkid = OrLiveChatCookieUtil.getCookieByName(cookieMap, DouyuApis.KEY_COOKIE_ACF_LTKID, () -> {
                        throw new BaseException("Cookie中缺少字段" + DouyuApis.KEY_COOKIE_ACF_LTKID);
                    });
                    String acfStk = OrLiveChatCookieUtil.getCookieByName(cookieMap, DouyuApis.KEY_COOKIE_ACF_STK, () -> {
                        throw new BaseException("Cookie中缺少字段" + DouyuApis.KEY_COOKIE_ACF_STK);
                    });
                    String dyDid = OrLiveChatCookieUtil.getCookieByName(cookieMap, DouyuApis.KEY_COOKIE_DY_DID, () -> {
                        throw new BaseException("Cookie中缺少字段" + DouyuApis.KEY_COOKIE_DY_DID);
                    });
                    loginreqMsg = new LoginreqMsg(realRoomId, dfl, username, ver, aver, acfLtkid, "1", acfStk, dyDid);
                    return DouyuCodecUtil.encode(loginreqMsg, LoginreqMsg.SHOULD_IGNORE_PROPERTIES_WHEN_LOGGED_IN);
                }
            }
        } catch (Exception e) {
            throw new BaseException(String.format("认证包创建失败，请检查房间号是否正确。roomId: %d, msg: %s", roomId, e.getMessage()));
        }
    }

    public ByteBuf createAuth(DouyuApis.RoomInitResult roomInitResult, DouyuClientModeEnum mode, String ver, String aver) {
        return this.createAuth(roomInitResult, mode, ver, aver, null);
    }

    public ByteBuf createHeartbeat() {
        return DouyuCodecUtil.encode(this.getHeartbeatMsg());
    }

    public ByteBuf createKeeplive(String cookie) {
        return DouyuCodecUtil.encode(this.getKeepliveMsg(StrUtil.isNotBlank(cookie) ? "hs-h5" : ""));
    }

    /**
     * 心跳包单例模式
     *
     * @return HeartbeatWebSocketFrame
     */
    public HeartbeatMsg getHeartbeatMsg() {
        if (heartbeatMsg == null) {
            synchronized (DouyuMsgFactory.this) {
                if (heartbeatMsg == null) {
                    heartbeatMsg = new HeartbeatMsg();
                }
            }
        }
        return heartbeatMsg;
    }

    private BaseDouyuCmdMsg getKeepliveMsg(String cnd) {
        return new KeepliveMsg(cnd);
    }

    public ByteBuf createJoingroup() {
        JoingroupMsg joingroupMsg = new JoingroupMsg();
        joingroupMsg.setRid(roomId);
        return DouyuCodecUtil.encode(joingroupMsg);
    }

    public ByteBuf createSub() {
        return DouyuCodecUtil.encode(new SubMsg());
    }

    public ByteBuf createDanmu(String msg, String cookie) {
        String dyDid = OrLiveChatCookieUtil.getCookieByName(cookie, DouyuApis.KEY_COOKIE_DY_DID, () -> {
            throw new BaseException("cookie中缺少参数" + DouyuApis.KEY_COOKIE_DY_DID);
        });
        String acfUid = OrLiveChatCookieUtil.getCookieByName(cookie, DouyuApis.KEY_COOKIE_ACF_UID, () -> {
            throw new BaseException("cookie中缺少参数" + DouyuApis.KEY_COOKIE_ACF_UID);
        });
        ChatmessageMsg chatmessageMsg = new ChatmessageMsg(msg, dyDid, acfUid);
        return DouyuCodecUtil.encode(chatmessageMsg);
    }
}
