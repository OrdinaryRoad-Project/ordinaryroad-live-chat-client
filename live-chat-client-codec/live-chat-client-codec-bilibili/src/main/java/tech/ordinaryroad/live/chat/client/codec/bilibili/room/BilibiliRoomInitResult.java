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

package tech.ordinaryroad.live.chat.client.codec.bilibili.room;

import lombok.*;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.BilibiliApis;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.response.RoomInfoRes;
import tech.ordinaryroad.live.chat.client.codec.bilibili.api.response.RoomPlayInfoResult;
import tech.ordinaryroad.live.chat.client.commons.base.constant.RoomLiveStatusEnum;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomInitResult;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BilibiliRoomInitResult implements IRoomInitResult {
    private String buvid3;
    private String uid;

    private BilibiliApis.RoomByIds roomByIds;
    private BilibiliApis.DanmuinfoResult danmuinfoResult = new BilibiliApis.DanmuinfoResult();
    private RoomPlayInfoResult roomPlayInfoResult = new RoomPlayInfoResult();

    private RoomInfoRes roomInfoRes = new RoomInfoRes();

    /**
     * 直播间标题
     */
    @Override
    public String getRoomTitle() {
        return roomByIds.getTitle();
    }

    @Override
    public RoomLiveStatusEnum getRoomLiveStatus() {
        RoomLiveStatusEnum roomLiveStatus = null;
        switch (roomPlayInfoResult.getLive_status()) {
            case LIVING:
                roomLiveStatus = RoomLiveStatusEnum.LIVING;
                break;
            case STOPPED:
                roomLiveStatus = RoomLiveStatusEnum.STOPPED;
                break;
            default:
        }
        return roomLiveStatus;
    }
}