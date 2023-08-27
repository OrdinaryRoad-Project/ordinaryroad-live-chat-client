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

package tech.ordinaryroad.live.chat.client.commons.client;

import lombok.Getter;
import tech.ordinaryroad.live.chat.client.commons.client.config.BaseLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author mjz
 * @date 2023/8/26
 */
public abstract class BaseLiveChatClient<Config extends BaseLiveChatClientConfig> {

    private final Config config;
    @Getter
    private volatile ClientStatusEnums status = ClientStatusEnums.NEW;
    protected volatile boolean cancelReconnect = false;

    protected BaseLiveChatClient(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public abstract void init();

    public abstract void connect();

    /**
     * 手动断开连接
     *
     * @param cancelReconnect 取消本次的自动重连（如果启用自动重连）
     */
    public void disconnect(boolean cancelReconnect) {
        this.cancelReconnect = cancelReconnect;
        this.disconnect();
    }

    public abstract void disconnect();

    public abstract void destroy();

    public abstract void send(Object msg);

    public abstract void send(Object msg, Runnable success, Consumer<Throwable> failed);

    public void send(Object msg, Runnable success) {
        this.send(msg, success, null);
    }

    public void send(Object msg, Consumer<Throwable> failed) {
        this.send(msg, null, failed);
    }

    protected abstract void tryReconnect();

    protected abstract String getWebSocketUriString();

    /**
     * 判断是否处于某个状态，或者处于后续状态
     *
     * @param status {@link ClientStatusEnums}
     * @return false: 还没有到达该状态
     */
    protected boolean checkStatus(ClientStatusEnums status) {
        return this.status.getCode() >= Objects.requireNonNull(status).getCode();
    }

    protected void setStatus(ClientStatusEnums status) {
        if (this.status != status) {
            this.status = status;
        }
    }

}
