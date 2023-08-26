package tech.ordinaryroad.bilibili.live.chat.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.ordinaryroad.live.chat.client.bilibili.netty.handler.BilibiliConnectionHandler;
import tech.ordinaryroad.live.chat.client.commons.base.listener.IBaseConnectionListener;

/**
 * @author mjz
 * @date 2023/8/21
 */
@Slf4j
@Service
public class BilibiliConnectionListener implements IBaseConnectionListener<BilibiliConnectionHandler> {

    @Override
    public void onConnected() {
        log.info("onConnected");
    }

    @Override
    public void onConnectFailed(BilibiliConnectionHandler connectionHandler) {
        log.info("onConnectFailed");
    }

    @Override
    public void onDisconnected(BilibiliConnectionHandler connectionHandler) {
        log.info("onDisconnected");
    }
}
