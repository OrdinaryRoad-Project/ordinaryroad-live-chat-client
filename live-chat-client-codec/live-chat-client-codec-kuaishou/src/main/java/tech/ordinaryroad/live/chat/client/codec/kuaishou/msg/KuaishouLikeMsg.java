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

package tech.ordinaryroad.live.chat.client.codec.kuaishou.msg;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.api.KuaishouApis;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.base.BaseKuaishouMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.WebLikeFeedOuterClass;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ILikeMsg;
import tech.ordinaryroad.live.chat.client.commons.util.jackson.serializer.ProtobufToBase64Serializer;

/**
 * @author mjz
 * @date 2024/1/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KuaishouLikeMsg extends BaseKuaishouMsg implements ILikeMsg {

    @JsonSerialize(using = ProtobufToBase64Serializer.class)
    private WebLikeFeedOuterClass.WebLikeFeed msg;

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
}
