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

package tech.ordinaryroad.live.chat.client.kuaishou.netty.handler;

import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.api.KuaishouApis;
import tech.ordinaryroad.live.chat.client.kuaishou.client.KuaishouLiveChatClient;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.msg.KuaishouDanmuMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.msg.KuaishouGiftMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.msg.KuaishouLikeMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.msg.base.IKuaishouMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.*;
import tech.ordinaryroad.live.chat.client.servers.netty.client.handler.BaseNettyClientBinaryFrameHandler;

import java.util.List;

/**
 * @author mjz
 * @date 2024/1/5
 */
@Slf4j
@ChannelHandler.Sharable
public class KuaishouBinaryFrameHandler extends BaseNettyClientBinaryFrameHandler<KuaishouLiveChatClient, KuaishouBinaryFrameHandler, PayloadTypeOuterClass.PayloadType, IKuaishouMsg, IKuaishouMsgListener> {

    public KuaishouBinaryFrameHandler(List<IKuaishouMsgListener> iKuaishouMsgListeners, KuaishouLiveChatClient client) {
        super(iKuaishouMsgListeners, client);
    }

    public KuaishouBinaryFrameHandler(List<IKuaishouMsgListener> iKuaishouMsgListeners, long roomId) {
        super(iKuaishouMsgListeners, roomId);
    }

    @SneakyThrows
    @Override
    public void onCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
        if (super.msgListeners.isEmpty()) {
            return;
        }

        SocketMessageOuterClass.SocketMessage socketMessage = (SocketMessageOuterClass.SocketMessage) cmdMsg;
        ByteString payloadByteString = socketMessage.getPayload();
        switch (socketMessage.getPayloadType()) {
            // 用户贡献名单
            case SC_LIVE_WATCHING_LIST: {
                SCWebLiveWatchingUsersOuterClass.SCWebLiveWatchingUsers scWebLiveWatchingUsers = SCWebLiveWatchingUsersOuterClass.SCWebLiveWatchingUsers.parseFrom(payloadByteString);
                List<WebWatchingUserInfoOuterClass.WebWatchingUserInfo> watchingUserList = scWebLiveWatchingUsers.getWatchingUserList();
                String displayWatchingCount = scWebLiveWatchingUsers.getDisplayWatchingCount();
                long pendingDuration = scWebLiveWatchingUsers.getPendingDuration();
                break;
            }
            case SC_FEED_PUSH: {
                SCWebFeedPushOuterClass.SCWebFeedPush scWebFeedPush = SCWebFeedPushOuterClass.SCWebFeedPush.parseFrom(payloadByteString);
                if (scWebFeedPush.getCommentFeedsCount() > 0) {
                    for (WebCommentFeedOuterClass.WebCommentFeed webCommentFeed : scWebFeedPush.getCommentFeedsList()) {
                        KuaishouDanmuMsg msg = new KuaishouDanmuMsg(webCommentFeed);
                        iteratorMsgListeners(msgListener -> msgListener.onDanmuMsg(KuaishouBinaryFrameHandler.this, msg));
                    }
                }
                if (scWebFeedPush.getGiftFeedsCount() > 0) {
                    for (WebGiftFeedOuterClass.WebGiftFeed webGiftFeed : scWebFeedPush.getGiftFeedsList()) {
                        KuaishouGiftMsg msg = new KuaishouGiftMsg(webGiftFeed);
                        // 计算礼物个数
                        KuaishouApis.calculateGiftCount(msg);
                        iteratorMsgListeners(msgListener -> msgListener.onGiftMsg(KuaishouBinaryFrameHandler.this, msg));
                    }
                }
                if (scWebFeedPush.getLikeFeedsCount() > 0) {
                    for (WebLikeFeedOuterClass.WebLikeFeed webLikeFeed : scWebFeedPush.getLikeFeedsList()) {
                        KuaishouLikeMsg msg = new KuaishouLikeMsg(webLikeFeed);
                        iteratorMsgListeners(msgListener -> msgListener.onLikeMsg(KuaishouBinaryFrameHandler.this, msg));
                    }
                }
                break;
            }
            default: {
                iteratorMsgListeners(msgListener -> msgListener.onOtherCmdMsg(KuaishouBinaryFrameHandler.this, cmd, socketMessage));
            }
        }
    }
}
