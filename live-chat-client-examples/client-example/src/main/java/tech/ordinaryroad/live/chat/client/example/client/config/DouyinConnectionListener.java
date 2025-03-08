package tech.ordinaryroad.live.chat.client.example.client.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.ordinaryroad.live.chat.client.douyin.listener.IDouyinConnectionListener;
import tech.ordinaryroad.live.chat.client.douyin.netty.handler.DouyinConnectionHandler;

/**
 * @date 2023/8/21
 */
@Slf4j
@Service
public class DouyinConnectionListener implements IDouyinConnectionListener {

    @Override
    public void onConnected(DouyinConnectionHandler connectionHandler) {
        log.info("Douyin {} onConnected", connectionHandler.getRoomId());
    }

    @Override
    public void onConnectFailed(DouyinConnectionHandler connectionHandler) {
        log.info("Douyin {} onConnectFailed", connectionHandler.getRoomId());
    }

    @Override
    public void onDisconnected(DouyinConnectionHandler connectionHandler) {
        log.info("Douyin {} onDisconnected", connectionHandler.getRoomId());
    }
}
