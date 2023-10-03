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
public class WebSocketParam {

    private long lUid = 0;
    private String sGuid = "";
    private String sUA = "";
    private String sAppSrc = "";
    private String sExp = "";
    private int iTokenType = 0;
    private String sToken = "";
    private String sCookie = "";
    private String sTraceId = "";
    private Map<String, String> mCustomHeaders;

    public WebSocketParam(String ver, String sExp, String appSrc) {
        this.sUA = "webh5&%s&websocket".formatted(ver);
        this.sAppSrc = appSrc;
        this.sExp = sExp;
        this.mCustomHeaders = new HashMap<>() {{
            put("HUYA_NET", "0");
            put("HUYA_VSDKUA", "webh5&%s&websocket".formatted(ver));
        }};
    }
}