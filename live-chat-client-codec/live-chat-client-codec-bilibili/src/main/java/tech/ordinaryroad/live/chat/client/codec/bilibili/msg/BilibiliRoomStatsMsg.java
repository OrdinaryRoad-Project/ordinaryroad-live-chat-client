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

package tech.ordinaryroad.live.chat.client.codec.bilibili.msg;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.BilibiliCmdEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.OperationEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.base.BaseBilibiliCmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IRoomStatsMsg;

/**
 * BilibiliRoomStatsMsg
 *
 * @author mjz
 * @date 2024/4/24
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BilibiliRoomStatsMsg extends BaseBilibiliCmdMsg implements IRoomStatsMsg {

    private JsonNode data;

    @Override
    public String getWatchingCount() {
        if (getCmdEnum() == BilibiliCmdEnum.ONLINE_RANK_COUNT) {
            if (data != null && data.has("online_count")) {
                return data.get("online_count").asText();
            }
        }
        return null;
    }

    @Override
    public String getWatchedCount() {
        if (getCmdEnum() == BilibiliCmdEnum.WATCHED_CHANGE) {
            if (data != null && data.has("num")) {
                return data.get("num").asText();
            }
        }
        return null;
    }

    @Override
    public String getLikedCount() {
        if (getCmdEnum() == BilibiliCmdEnum.LIKE_INFO_V3_UPDATE) {
            if (data != null && data.has("click_count")) {
                return data.get("click_count").asText();
            }
        }
        return null;
    }

    @Override
    public OperationEnum getOperationEnum() {
        return OperationEnum.MESSAGE;
    }
}
