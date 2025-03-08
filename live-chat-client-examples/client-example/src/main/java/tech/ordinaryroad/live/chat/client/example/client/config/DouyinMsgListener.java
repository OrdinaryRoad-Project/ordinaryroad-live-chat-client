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
 import tech.ordinaryroad.live.chat.client.codec.douyin.msg.DouyinDanmuMsg;
 import tech.ordinaryroad.live.chat.client.codec.douyin.msg.DouyinGiftMsg;
 import tech.ordinaryroad.live.chat.client.codec.douyin.msg.DouyinRoomStatsMsg;
 import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
 import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
 import tech.ordinaryroad.live.chat.client.douyin.client.DouyinLiveChatClient;
 import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinMsgListener;
 import tech.ordinaryroad.live.chat.client.douyin.netty.handler.DouyinBinaryFrameHandler;

 @Slf4j
 @Service
 public class DouyinMsgListener implements IDouyinMsgListener {

     private DouyinLiveChatClient getDouyinLiveChatClient() {
         return SpringUtil.getBean(DouyinLiveChatClient.class);
     }

     @Override
     public void onDanmuMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinDanmuMsg msg) {
         IDouyinMsgListener.super.onDanmuMsg(binaryFrameHandler, msg);
         String content = msg.getContent();
         String uid = msg.getUid();
         log.info("{} 收到弹幕 {} {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), uid, content);

         // TODO 可以用大模型进行FAQ回复
//         String answer = content + "  的回复";
//         DouyinLiveChatClient.sendDanmu(answer);
         RoomLiveStatusEnum roomLiveStatus = getDouyinLiveChatClient().getRoomInitResult().getRoomLiveStatus();
         log.debug("直播间状态 {}", roomLiveStatus);
     }

     @Override
     public void onGiftMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinGiftMsg msg) {
         IDouyinMsgListener.super.onGiftMsg(binaryFrameHandler, msg);
     }


     @Override
     public void onRoomStatsMsg(DouyinBinaryFrameHandler binaryFrameHandler, DouyinRoomStatsMsg msg) {
         IDouyinMsgListener.super.onRoomStatsMsg(binaryFrameHandler, msg);
         log.info("{} 统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", binaryFrameHandler.getRoomId(), msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
     }


     @Override
     public void onMsg(IMsg msg) {
     }

     @Override
     public void onUnknownCmd(String cmdString, IMsg msg) {
         log.info("收到未知CMD消息 {}", cmdString);
     }
 }
