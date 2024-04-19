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

package tech.ordinaryroad.live.chat.client.kuaishou.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IGiftMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.api.KuaishouApis;
import tech.ordinaryroad.live.chat.client.kuaishou.msg.base.IKuaishouMsg;
import tech.ordinaryroad.live.chat.client.kuaishou.protobuf.WebGiftFeedOuterClass;

/**
 * @author mjz
 * @date 2024/1/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KuaishouGiftMsg implements IKuaishouMsg, IGiftMsg {

    private WebGiftFeedOuterClass.WebGiftFeed msg;
    /**
     * 计算后得到的礼物个数
     */
    private int calculatedGiftCount;

    public KuaishouGiftMsg(WebGiftFeedOuterClass.WebGiftFeed msg) {
        this.msg = msg;
    }

    @Override
    public String getBadgeName() {
        return KuaishouApis.getBadgeName(msg.getLiveAudienceState());
    }

    @Override
    public byte getBadgeLevel() {
        return KuaishouApis.getBadgeLevel(msg.getLiveAudienceState());
    }

    @Override
    public String getUid() {
        return msg.getUser().getPrincipalId();
    }

    @Override
    public String getUsername() {
        return msg.getUser().getUserName();
    }

    @Override
    public String getUserAvatar() {
        return msg.getUser().getHeadUrl();
    }

    @Override
    public String getGiftName() {
        return KuaishouApis.getGiftInfoById(this.getGiftId()).getGiftName();
    }

    @Override
    public String getGiftImg() {
        return KuaishouApis.getGiftInfoById(this.getGiftId()).getGiftUrl();
    }

    @Override
    public String getGiftId() {
        return Integer.toString(msg.getIntGiftId());
    }

    @Override
    public int getGiftCount() {
        return this.calculatedGiftCount;
    }

    @Override
    public int getGiftPrice() {
        return 0;
    }

    @Override
    public String getReceiveUid() {
        return null;
    }

    @Override
    public String getReceiveUsername() {
        return null;
    }

    @Override
    public String toString() {
        return msg.toString();
    }
}
