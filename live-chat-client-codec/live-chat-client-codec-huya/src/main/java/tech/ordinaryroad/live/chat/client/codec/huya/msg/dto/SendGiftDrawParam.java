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

package tech.ordinaryroad.live.chat.client.codec.huya.msg.dto;

import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.TarsStructBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mjz
 * @date 2026/1/27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendGiftDrawParam extends TarsStructBase {

    private ItemSize tItemSize = new ItemSize();
    private List<ItemCount> vItemInfo = new ArrayList<ItemCount>() {{
        add(new ItemCount());
    }};
    private List<ItemRoute> vItemRoute = new ArrayList<ItemRoute>() {{
        add(new ItemRoute());
    }};

    @Override
    public void writeTo(TarsOutputStream os) {
        os.write(tItemSize, 0);
        os.write(vItemInfo, 1);
        os.write(vItemRoute, 2);
    }

    @Override
    public void readFrom(TarsInputStream is) {
        tItemSize = (ItemSize) is.directRead(tItemSize, 0, false);
        vItemInfo = is.readArray(vItemInfo, 1, false);
        vItemRoute = is.readArray(vItemRoute, 2, false);
    }

    @Override
    public TarsStructBase newInit() {
        return this;
    }
}