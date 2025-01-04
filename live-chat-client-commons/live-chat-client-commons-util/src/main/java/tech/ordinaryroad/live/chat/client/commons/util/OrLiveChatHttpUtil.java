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

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

/**
 * @author mjz
 * @date 2024/3/20
 */
public class OrLiveChatHttpUtil extends HttpUtil {

    public static final String USER_AGENT;
    private static final ProxyProperties PROXY_PROPERTIES = new ProxyProperties();

    private static final List<String> USER_AGENT_OS_LIST = Arrays.asList("(Windows NT 10.0; Win64; x64)", "(Windows NT 10.0; WOW64)",
            "(Windows NT 6.3; WOW64)", "(Windows NT 6.3; Win64; x64)", "(Windows NT 6.1; Win64; x64)", "(Windows NT 6.1; WOW64)", "(X11; Linux x86_64)",
            "(Macintosh; Intel Mac OS X 10_12_6)", "(Macintosh; Intel Mac OS X 10_15_7)");
    private static final List<String> USER_AGENT_VERSION_LIST = Arrays.asList(
            "131.0.0.0",
            "110.0.5481.77", "110.0.5481.30", "109.0.5414.74", "108.0.5359.71", "108.0.5359.22",
            "98.0.4758.48", "97.0.4692.71");

    static {
        // Random Chrome Version
        String randomVersion = RandomUtil.randomEle(USER_AGENT_VERSION_LIST);
        USER_AGENT = "Mozilla/5.0 "
                // os
                + RandomUtil.randomEle(USER_AGENT_OS_LIST)
                + " AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/" + randomVersion
                + " Safari/537.36"
                + " Edg/" + randomVersion;
        GlobalHeaders.INSTANCE.header(Header.USER_AGENT, USER_AGENT);
    }

    public static void updateProxyHost(String host) {
        PROXY_PROPERTIES.setHost(host);
    }

    public static void updateProxyPort(int port) {
        PROXY_PROPERTIES.setPort(port);
    }

    public static void updateProxyUsername(String username) {
        PROXY_PROPERTIES.setUsername(username);
    }

    public static void updateProxyPassword(String password) {
        PROXY_PROPERTIES.setPassword(password);
    }

    public static HttpRequest createRequest(Method method, String url) {
        return setRequestSocks5Proxy(HttpUtil.createRequest(method, url));
    }

    public static HttpRequest createGet(String url, boolean isFollowRedirects) {
        return setRequestSocks5Proxy(HttpUtil.createGet(url, isFollowRedirects));
    }

    public static HttpRequest createGet(String url) {
        return setRequestSocks5Proxy(HttpUtil.createGet(url));
    }

    public static HttpRequest createPost(String url) {
        return setRequestSocks5Proxy(HttpUtil.createPost(url));
    }

    public static HttpRequest setRequestSocks5Proxy(HttpRequest request) {
        String host = PROXY_PROPERTIES.getHost();
        if (StrUtil.isNotBlank(host)) {
            request.setProxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, PROXY_PROPERTIES.getPort())));
            String username = PROXY_PROPERTIES.getUsername();
            if (StrUtil.isNotBlank(username)) {
                request.basicProxyAuth(username, PROXY_PROPERTIES.getPassword());
            }
        }
        return request;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProxyProperties {
        private String host;
        private int port;
        private String username;
        private String password;
    }
}
