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

package tech.ordinaryroad.live.chat.client.commons.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.URLUtil;

import java.util.function.Function;

/**
 * @author mjz
 * @date 2024/3/25
 */
public class OrLiveChatUrlUtil extends URLUtil {

    /**
     * 匹配地址Scheme，${scheme}://xxxxxxxxx
     */
    private static final String PATTERN_URI_SCHEME = "([a-zA-Z]+):\\/\\/";
    /**
     * 匹配地址Port，scheme://xxxxxxxxx:${port}/123
     */
    private static final String PATTERN_URI_PORT = "[a-zA-Z]+:\\/\\/.+:([0-9]+)";

    public static String getScheme(String location) {
        return ReUtil.getGroup1(PATTERN_URI_SCHEME, location);
    }

    public static int getPort(String location, Function<String, Integer> defaultPortSupplier) {
        int port = -1;
        int i = NumberUtil.parseInt(ReUtil.getGroup1(PATTERN_URI_PORT, location));
        if (i > 0) {
            port = i;
        } else {
            if (defaultPortSupplier != null) {
                port = defaultPortSupplier.apply(location);
            }
        }
        return port;
    }

    public static int getPort(String location) {
        return getPort(location, null);
    }

    public static int getWebSocketUriPort(String location) {
        return getPort(location, (string) -> {
            String scheme = getScheme(location);
            if ("wss".equalsIgnoreCase(scheme)) {
                return 443;
            } else if ("ws".equalsIgnoreCase(scheme)) {
                return 80;
            } else {
                return -1;
            }
        });
    }
}
