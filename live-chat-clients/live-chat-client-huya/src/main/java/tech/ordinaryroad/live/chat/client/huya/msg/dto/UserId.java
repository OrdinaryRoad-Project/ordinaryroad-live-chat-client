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

package tech.ordinaryroad.live.chat.client.huya.msg.dto;

import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.TarsStructBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mjz
 * @date 2023/10/2
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserId extends TarsStructBase {

    private long lUid;
    private String sGuid = "";
    private String sToken = "";
    private String sHuYaUA = "";
    private String sCookie = "";
    private int iTokenType;
    private String sDeviceInfo = "";
    private String sQIMEI = "";


    @Override
    public void writeTo(TarsOutputStream os) {
        os.write(this.lUid, 0);
        os.write(this.sGuid, 1);
        os.write(this.sToken, 2);
        os.write(this.sHuYaUA, 3);
        os.write(this.sCookie, 4);
        os.write(this.iTokenType, 5);
        os.write(this.sDeviceInfo, 6);
        os.write(this.sQIMEI, 7);
    }

    @Override
    public void readFrom(TarsInputStream is) {
        this.lUid = is.read(this.lUid, 0, false);
        this.sGuid = is.read(this.sGuid, 1, false);
        this.sToken = is.read(this.sToken, 2, false);
        this.sHuYaUA = is.read(this.sHuYaUA, 3, false);
        this.sCookie = is.read(this.sCookie, 4, false);
        this.iTokenType = is.read(this.iTokenType, 5, false);
        this.sDeviceInfo = is.read(this.sDeviceInfo, 6, false);
        this.sQIMEI = is.read(this.sQIMEI, 7, false);
    }

    @Override
    public TarsStructBase newInit() {
        return this;
    }
}
