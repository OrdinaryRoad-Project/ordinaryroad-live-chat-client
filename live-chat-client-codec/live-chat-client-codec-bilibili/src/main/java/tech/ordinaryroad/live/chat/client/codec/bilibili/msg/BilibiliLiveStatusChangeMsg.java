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

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.codec.bilibili.constant.OperationEnum;
import tech.ordinaryroad.live.chat.client.codec.bilibili.msg.base.BaseBilibiliCmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.constant.LiveStatusAction;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ILiveStatusChangeMsg;

/**
 * @author mjz
 * @date 2024/3/11
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BilibiliLiveStatusChangeMsg extends BaseBilibiliCmdMsg implements ILiveStatusChangeMsg {

    /**
     * 保存房间号和短房间号
     */
    private Pair<Object, Object> roomIdPair;

    private JsonNode data;

    @Override
    public OperationEnum getOperationEnum() {
        return OperationEnum.MESSAGE;
    }

    @Override
    public LiveStatusAction getLiveStatusAction() {
        switch (getCmdEnum()) {
            case LIVE: {
                return LiveStatusAction.BEGIN;
            }
            case STOP_LIVE_ROOM_LIST: {
                Object roomId = roomIdPair.getKey();
                Object shortRoomId = roomIdPair.getValue();
                if (data == null || !data.has("room_id_list")) {
                    return null;
                }
                ArrayNode roomIdList = data.withArray("room_id_list");
                for (JsonNode jsonNode : roomIdList) {
                    long aLong = jsonNode.asLong();
                    if (aLong == NumberUtil.parseLong(StrUtil.toStringOrNull(roomId)) || aLong == NumberUtil.parseLong(StrUtil.toStringOrNull(shortRoomId))) {
                        return LiveStatusAction.END;
                    }
                }
            }
        }
        return null;
    }
}
