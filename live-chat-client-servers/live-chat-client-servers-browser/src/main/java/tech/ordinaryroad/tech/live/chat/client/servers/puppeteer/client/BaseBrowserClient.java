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

package tech.ordinaryroad.tech.live.chat.client.servers.puppeteer.client;

import com.ruiyun.jvppeteer.api.core.Browser;
import com.ruiyun.jvppeteer.api.core.Page;
import com.ruiyun.jvppeteer.cdp.core.Puppeteer;
import lombok.extern.slf4j.Slf4j;
import tech.ordinaryroad.live.chat.client.commons.base.exception.BaseException;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseMsgListener;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.client.BaseLiveChatClient;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.tech.live.chat.client.servers.puppeteer.config.BaseBrowserClientConfig;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author mjz
 * @date 2025/5/3
 */
@Slf4j
public abstract class BaseBrowserClient
        <Config extends BaseBrowserClientConfig,
                RoomInitResult extends IRoomInitResult,
                CmdEnum extends Enum<CmdEnum>,
                Msg extends IMsg,
                MsgListener extends IBaseMsgListener<?, CmdEnum>
                >
        extends BaseLiveChatClient<Config, RoomInitResult, MsgListener> {

    protected ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    protected Browser browser;
    protected Page page;
    protected Class<MsgListener> msgListenerClass;

    protected BaseBrowserClient(Config config) {
        super(config);
        this.msgListenerClass = null;
    }

    protected BaseBrowserClient(Config config, Class<MsgListener> msgListenerClass) {
        super(config);
        this.msgListenerClass = msgListenerClass;
    }

    @Override
    public void init() {
        if (checkStatus(ClientStatusEnums.INITIALIZED)) {
            return;
        }

        try {
            this.browser = Puppeteer.launch(getConfig().getLaunchOptions());
        } catch (Exception e) {
            throw new BaseException("初始化失败", e);
        }

        this.setStatus(ClientStatusEnums.INITIALIZED);
    }

    @Override
    public void connect(Runnable success, Consumer<Throwable> failed) {
        super.connect(success, failed);
        if (this.cancelReconnect) {
            this.cancelReconnect = false;
        }
        if (!checkStatus(ClientStatusEnums.INITIALIZED)) {
            return;
        }
        if (getStatus() == ClientStatusEnums.CONNECTED) {
            return;
        }
        if (getStatus() != ClientStatusEnums.RECONNECTING) {
            this.setStatus(ClientStatusEnums.CONNECTING);
        }

        if (this.page == null) {
            if (browser.pages().isEmpty()) {
                this.page = browser.newPage();
            } else {
                this.page = browser.pages().get(0);
            }
        }
    }

    @Override
    public void disconnect() {
        if (this.page != null) {
            this.page.close();
            this.page = null;
        }
        this.setStatus(ClientStatusEnums.DISCONNECTED);
        tryReconnect();
    }

    @Override
    protected void tryReconnect() {
        if (this.cancelReconnect) {
            this.cancelReconnect = false;
            return;
        }
        if (!getConfig().isAutoReconnect()) {
            return;
        }
        if (log.isWarnEnabled()) {
            Object roomId = getConfig().getRoomId();
            log.warn("{}s后将重新连接 {}", getConfig().getReconnectDelay(), roomId == null ? getConfig().getWebsocketUri() : roomId);
        }
        executor.schedule(() -> {
            this.setStatus(ClientStatusEnums.RECONNECTING);
            this.connect();
        }, getConfig().getReconnectDelay(), TimeUnit.SECONDS);
    }

    @Override
    public void send(Object msg, Runnable success, Consumer<Throwable> failed) {
        throw new BaseException("暂未支持该功能");
    }

    @Override
    public void sendDanmu(Object danmu, Runnable success, Consumer<Throwable> failed) {
        throw new BaseException("暂未支持该功能");
    }

    @Override
    public void clickLike(int count, Runnable success, Consumer<Throwable> failed) {
        throw new BaseException("暂未支持该功能");
    }

    @Override
    public void destroy() {
        // 销毁时不需要重连
        this.cancelReconnect = true;
        try {
            browser.close();
        } catch (Exception e) {
            log.error("浏览器关闭失败", e);
        } finally {
            this.setStatus(ClientStatusEnums.DESTROYED);
        }
    }
}
