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

package tech.ordinaryroad.live.chat.client.huya.msg;

import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.TarsStructBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mjz
 * @date 2023/9/5
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WSConnectParaInfo extends TarsStructBase {

    private long lUid = 0;
    private String sGuid = "";
    private String sUA = "";
    private String sAppSrc = "";
    private String sMid = "";
    private String sExp = "";
    private int iTokenType = 0;
    private String sToken = "";
    private String sCookie = "";
    private String sTraceId = "";
    private Map<String, String> mCustomHeaders = new HashMap<>() {{
        put("", "");
    }};

    @Override
    public void writeTo(TarsOutputStream os) {
        os.write(this.lUid, 0);
        os.write(this.sGuid, 1);
        os.write(this.sUA, 2);
        os.write(this.sAppSrc, 3);
        os.write(this.sMid, 4);
        os.write(this.sExp, 5);
        os.write(this.iTokenType, 6);
        os.write(this.sToken, 7);
        os.write(this.sCookie, 8);
        os.write(this.sTraceId, 9);
        os.write(this.mCustomHeaders, 10);
    }

    @Override
    public void readFrom(TarsInputStream is) {
        this.lUid = is.read(this.lUid, 0, true);
        this.sGuid = is.read(this.sGuid, 1, true);
        this.sUA = is.read(this.sUA, 2, true);
        this.sAppSrc = is.read(this.sAppSrc, 3, true);
        this.sMid = is.read(this.sMid, 4, true);
        this.sExp = is.read(this.sExp, 5, true);
        this.iTokenType = is.read(this.iTokenType, 6, true);
        this.sToken = is.read(this.sToken, 7, true);
        this.sCookie = is.read(this.sCookie, 8, true);
        this.sTraceId = is.read(this.sTraceId, 9, true);
        this.mCustomHeaders = is.readMap(this.mCustomHeaders, 10, true);
    }

    public static WSConnectParaInfo newWSConnectParaInfo(String ver, String sExp, String appSrc) {
        WSConnectParaInfo wsConnectParaInfo = new WSConnectParaInfo();
//        wsConnectParaInfo.sGuid = UUID.fastUUID().toString(true);

        wsConnectParaInfo.sUA = "webh5&%s&websocket".formatted(ver);
        wsConnectParaInfo.sAppSrc = appSrc;
        wsConnectParaInfo.sExp = sExp;
        wsConnectParaInfo.mCustomHeaders = new HashMap<>() {{
            put("HUYA_NET", "0");
            put("HUYA_VSDKUA", wsConnectParaInfo.sUA);
        }};
        return wsConnectParaInfo;
    }
}