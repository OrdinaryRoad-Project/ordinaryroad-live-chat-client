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

package tech.ordinaryroad.live.chat.client.codec.douyin.msg;

import cn.hutool.core.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.codec.douyin.msg.base.IDouyinMsg;
import tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.RoomStatsMessage;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IRoomStatsMsg;

/**
 * @author mjz
 * @date 2024/3/28
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DouyinRoomStatsMsg implements IDouyinMsg, IRoomStatsMsg {

    /**
     * 保存{@link tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LikeMessage#getTotal()}
     */
    private String likedCount;

    private RoomStatsMessage msg;

    @Override
    public String getLikedCount() {
        return this.likedCount;
    }

    @Override
    public String getWatchingCount() {
        if (msg != null) {
            return NumberUtil.toStr(msg.getTotal());
        }
        return null;
    }
}
