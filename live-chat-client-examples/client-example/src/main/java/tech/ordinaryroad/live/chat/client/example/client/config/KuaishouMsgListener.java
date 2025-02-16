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

 package tech.ordinaryroad.live.chat.client.example.client.config;

 import cn.hutool.extra.spring.SpringUtil;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.stereotype.Service;
 import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouDanmuMsg;
 import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouGiftMsg;
 import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouRoomStatsMsg;
 import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
 import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
 import tech.ordinaryroad.live.chat.client.kuaishou.client.KuaishouLiveChatClient;
 import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
 import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouBinaryFrameHandler;

 @Slf4j
 @Service
 public class KuaishouMsgListener implements IKuaishouMsgListener {

     private KuaishouLiveChatClient getKuaishouLiveChatClient() {
         return SpringUtil.getBean(KuaishouLiveChatClient.class);
     }

     @Override
     public void onDanmuMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouDanmuMsg msg) {
         IKuaishouMsgListener.super.onDanmuMsg(binaryFrameHandler, msg);
         String content = msg.getContent();
         String uid = msg.getUid();
         log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), uid, content);

         // TODO 可以用大模型进行FAQ回复
         // String answer = content + "  的回复";
         // kuaishouLiveChatClient.sendDanmu(answer);
         RoomLiveStatusEnum roomLiveStatus = getKuaishouLiveChatClient().getRoomInitResult().getRoomLiveStatus();
         log.debug("直播间状态 {}", roomLiveStatus);
     }

     @Override
     public void onGiftMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouGiftMsg msg) {
         IKuaishouMsgListener.super.onGiftMsg(binaryFrameHandler, msg);
         //log.info("{} 收到礼物 {} {}({}) {} {}({})x{}({})", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getUid(), msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice());
     }


     @Override
     public void onRoomStatsMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouRoomStatsMsg msg) {
         IKuaishouMsgListener.super.onRoomStatsMsg(binaryFrameHandler, msg);
         log.info("{} 统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", binaryFrameHandler.getRoomId(), msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
     }


     @Override
     public void onMsg(IMsg msg) {
//        KuaishouCmdMsg  cmdMsg = (KuaishouCmdMsg) msg;
//log info("收到{}消息 {}", msg.getClass(), msg);
//        log.info("收到{}消息 {}", msg.getClass(), msg);
     }

     @Override
     public void onUnknownCmd(String cmdString, IMsg msg) {
         log.info("收到未知CMD消息 {}", cmdString);
     }
 }
