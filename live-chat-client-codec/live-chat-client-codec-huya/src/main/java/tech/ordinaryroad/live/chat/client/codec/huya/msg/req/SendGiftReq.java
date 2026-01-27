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

package tech.ordinaryroad.live.chat.client.codec.huya.msg.req;

import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.TarsStructBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.dto.SendGiftExtParam;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.dto.SendGiftPayPloy;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.dto.UserId;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mjz
 * @date 2026/1/27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendGiftReq extends TarsStructBase {

    private UserId tId = new UserId();
    private long lGiftUid;
    private long lRoomUid;
    private int iItemType;
    private int iItemCount;
    private String sPayId = "";
    private String sSrcType = "";
    private SendGiftPayPloy tPayPloy = new SendGiftPayPloy();
    private SendGiftExtParam tExtParam = new SendGiftExtParam();
    private Map<String, String> mParam = new HashMap<>();

    @Override
    public void writeTo(TarsOutputStream os) {
        os.write(tId, 0);
        os.write(lGiftUid, 1);
        os.write(lRoomUid, 2);
        os.write(iItemType, 3);
        os.write(iItemCount, 4);
        os.write(sPayId, 5);
        os.write(sSrcType, 6);
        os.write(tPayPloy, 7);
        os.write(tExtParam, 8);
        os.write(mParam, 9);
    }

    @Override
    public void readFrom(TarsInputStream is) {
        tId = (UserId) is.read(tId, 0, false);
        lGiftUid = is.read(lGiftUid, 1, false);
        lRoomUid = is.read(lRoomUid, 2, false);
        iItemType = is.read(iItemType, 3, false);
        iItemCount = is.read(iItemCount, 4, false);
        sPayId = is.read(sPayId, 5, false);
        sSrcType = is.read(sSrcType, 6, false);
        tPayPloy = (SendGiftPayPloy) is.read(tPayPloy, 7, false);
        tExtParam = (SendGiftExtParam) is.read(tExtParam, 8, false);
        mParam = is.readStringMap(9, false);
    }

    @Override
    public TarsStructBase newInit() {
        return this;
    }
}
