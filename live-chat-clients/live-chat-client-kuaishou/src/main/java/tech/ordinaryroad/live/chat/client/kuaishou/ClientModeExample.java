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

package tech.ordinaryroad.live.chat.client.kuaishou;

import cn.hutool.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.kuaishou.client.KuaishouLiveChatClient;
import tech.ordinaryroad.live.chat.client.kuaishou.config.KuaishouLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.msg.KuaishouDanmuMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.msg.KuaishouGiftMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouBinaryFrameHandler;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.PayloadTypeOuterClass;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ClientModeExample {
    static Logger log = LoggerFactory.getLogger(ClientModeExample.class);

    public static void main(String[] args) {
        // 1. 创建配置
        KuaishouLiveChatClientConfig config = KuaishouLiveChatClientConfig.builder()
                // TODO 浏览器Cookie
                .cookie("did=web_c6e84e916daa091705ff43ee07011d71; clientid=3; did=web_c6e84e916daa091705ff43ee07011d71; client_key=65890b29; kpn=GAME_ZONE; didv=1704615659000; userId=19773365; userId=19773365; kuaishou.live.bfb1s=3e261140b0cf7444a0ba411c6f227d88; kuaishou.live.web_st=ChRrdWFpc2hvdS5saXZlLndlYi5zdBKgAefFHSqt4eG_74xB48ZnQ2HJeBfxZcMQYEGD0iBr4HFhcjW0tPsVdngdXBw2tYrlSDl7_8-KO5eAA-Ed--Im37GxrtS99FrS0MZM0Kjy37FmMEZlNeQSuylvMRvYefd8s-G2Cina1cp78LKmdUfl32rlLyuICyJSGUJKB73GipA1KC0I42eFXRg95cYF7j8BPtu7mF5oX0Jw3Tgc47f1r1waEnkpvegnDUJtgj96dZ26lsX47iIgGcp7PCNbRzeUkTp1KFMDR-ZZ8LIXP-rny_vq555GMxYoBTAB; kuaishou.live.web_ph=634c45653c0a1cb49ed6ac9542109f524308")
                // TODO 直播间id（支持短id）
                .roomId("hrj20011221")
                .build();

        // 2. 创建Client并传入配置、添加消息回调
        KuaishouLiveChatClient client = new KuaishouLiveChatClient(config, new IKuaishouMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                log.debug("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
                log.debug("收到CMD消息{} {}", cmd, cmdMsg);
            }

            @Override
            public void onOtherCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
                log.debug("收到其他CMD消息 {}", cmd);
            }

            @Override
            public void onUnknownCmd(String cmdString, IMsg msg) {
                log.debug("收到未知CMD消息 {}", cmdString);
            }

            @Override
            public void onDanmuMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouDanmuMsg msg) {
                log.info("{} 收到弹幕 [{}] {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onGiftMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouGiftMsg msg) {
                log.info("{} 收到礼物 [{}] {}({}) {} {}({})x{}({}) mergeKey:{},comboCount:{}, batchSize:{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), "赠送", msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice(), msg.getMsg().getMergeKey(), msg.getMsg().getComboCount(), msg.getMsg().getBatchSize());
            }
        });
        // 3. 开始监听直播间
        client.connect();

        // 客户端连接状态回调
        client.addStatusChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue().equals(ClientStatusEnums.CONNECTED)) {
                    // TODO 要发送的弹幕内容，请注意控制发送频率；框架内置支持设置发送弹幕的最少时间间隔，小于时将忽略该次发送
                    client.sendDanmu("666666" + RandomUtil.randomNumbers(1));
                }
            }
        });
    }
}