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

import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseMsgListener;
import tech.ordinaryroad.live.chat.client.commons.base.room.IRoomInitResult;
import tech.ordinaryroad.live.chat.client.commons.client.config.BaseLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.commons.client.listener.IClientStatusChangeListener;
import tech.ordinaryroad.live.chat.client.commons.client.plugin.IPlugin;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author mjz
 * @date 2023/8/26
 */
public abstract class BaseLiveChatClient<
        Config extends BaseLiveChatClientConfig,
        RoomInitResult extends IRoomInitResult,
        MsgListener extends IBaseMsgListener<?, ?>
        > implements IBaseLiveChatClient<RoomInitResult, MsgListener> {

    @Getter
    private final Config config;
    @Getter
    private final List<MsgListener> msgListeners = Collections.synchronizedList(new ArrayList<>());
    @Getter
    private final List<IPlugin> plugins = Collections.synchronizedList(new ArrayList<>());
    protected Class<MsgListener> msgListenerClass;
    @Getter
    protected RoomInitResult roomInitResult;
    protected volatile boolean cancelReconnect = false;
    private volatile ClientStatusEnums status = ClientStatusEnums.NEW;
    protected PropertyChangeSupport statusChangeSupport = new PropertyChangeSupport(status);

    protected BaseLiveChatClient(Config config) {
        this.config = config;
        this.msgListenerClass = null;

        // 触发setter
        this.config.setSocks5ProxyHost(this.config.getSocks5ProxyHost());
        this.config.setSocks5ProxyPort(this.config.getSocks5ProxyPort());
        this.config.setSocks5ProxyUsername(this.config.getSocks5ProxyUsername());
        this.config.setSocks5ProxyPassword(this.config.getSocks5ProxyPassword());
    }

    protected BaseLiveChatClient(Config config, Class<MsgListener> msgListenerClass) {
        this.config = config;
        this.msgListenerClass = msgListenerClass;

        // 触发setter
        this.config.setSocks5ProxyHost(this.config.getSocks5ProxyHost());
        this.config.setSocks5ProxyPort(this.config.getSocks5ProxyPort());
        this.config.setSocks5ProxyUsername(this.config.getSocks5ProxyUsername());
        this.config.setSocks5ProxyPassword(this.config.getSocks5ProxyPassword());
    }

    @Override
    public void connect(Runnable success, Consumer<Throwable> failed) {
        // 开始连接前先初始化房间信息
        roomInitResult = this.initRoom();
    }

    @Override
    public void connect(Runnable success) {
        this.connect(success, null);
    }

    @Override
    public void connect() {
        this.connect(null, null);
    }

    @Override
    public void disconnect(boolean cancelReconnect) {
        this.cancelReconnect = cancelReconnect;
        this.disconnect();
    }

    @Override
    public void send(Object msg) {
        this.send(msg, null, null);
    }

    @Override
    public void send(Object msg, Runnable success) {
        this.send(msg, success, null);
    }

    @Override
    public void send(Object msg, Consumer<Throwable> failed) {
        this.send(msg, null, failed);
    }

    @Override
    public void sendDanmu(Object danmu) {
        this.sendDanmu(danmu, null, null);
    }

    @Override
    public void sendDanmu(Object danmu, Runnable success) {
        this.sendDanmu(danmu, success, null);
    }

    @Override
    public void sendDanmu(Object danmu, Consumer<Throwable> failed) {
        this.sendDanmu(danmu, null, failed);
    }

    @Override
    public void clickLike(int count) {
        this.clickLike(count, null, null);
    }

    @Override
    public void clickLike(int count, Runnable success) {
        this.clickLike(count, success, null);
    }

    @Override
    public void clickLike(int count, Consumer<Throwable> failed) {
        this.clickLike(count, null, failed);
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

    @Override
    public ClientStatusEnums getStatus() {
        return this.status;
    }

    protected void setStatus(ClientStatusEnums status) {
        ClientStatusEnums oldStatus = this.status;
        if (oldStatus != status) {
            this.status = status;
            this.statusChangeSupport.firePropertyChange("status", oldStatus, status);
        }
    }

    @Override
    public void addStatusChangeListener(IClientStatusChangeListener listener) {
        this.statusChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removeStatusChangeListener(IClientStatusChangeListener listener) {
        this.statusChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void destroy() {
        // 移除状态监听器
        for (PropertyChangeListener propertyChangeListener : this.statusChangeSupport.getPropertyChangeListeners()) {
            this.statusChangeSupport.removePropertyChangeListener(propertyChangeListener);
        }

        // 移除消息监听器
        this.msgListeners.clear();

        // 移除插件
        for (IPlugin plugin : this.plugins) {
            plugin.unregister(this, msgListenerClass);
        }
        this.plugins.clear();
    }

    @Override
    public boolean addMsgListener(MsgListener msgListener) {
        if (msgListener == null) {
            return false;
        }
        return this.msgListeners.add(msgListener);
    }

    @Override
    public boolean addMsgListeners(List<MsgListener> msgListeners) {
        if (msgListeners == null || msgListeners.isEmpty()) {
            return false;
        }
        return this.msgListeners.addAll(msgListeners);
    }

    @Override
    public boolean removeMsgListener(MsgListener msgListener) {
        if (msgListener == null) {
            return false;
        }
        return this.msgListeners.remove(msgListener);
    }

    @Override
    public boolean removeMsgListeners(List<MsgListener> msgListeners) {
        if (msgListeners == null || msgListeners.isEmpty()) {
            return false;
        }
        return this.msgListeners.removeAll(msgListeners);
    }

    @Override
    public void removeAllMsgListeners() {
        this.msgListeners.clear();
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void iteratorMsgListeners(Consumer<MsgListener> consumer) {
        if (msgListeners.isEmpty()) {
            return;
        }
        for (int i = 0; i < msgListeners.size(); i++) {
            consumer.accept(msgListeners.get(i));
        }
    }

    @Override
    public void addPlugin(IPlugin... plugins) {
        if (ArrayUtil.isEmpty(plugins)) {
            return;
        }
        for (IPlugin plugin : plugins) {
            plugin.register(this, msgListenerClass);
            this.plugins.add(plugin);
        }
    }

    @Override
    public void removePlugin(IPlugin... plugins) {
        if (ArrayUtil.isEmpty(plugins)) {
            return;
        }
        for (IPlugin plugin : plugins) {
            plugin.unregister(this, msgListenerClass);
            this.plugins.remove(plugin);
        }
    }
}
