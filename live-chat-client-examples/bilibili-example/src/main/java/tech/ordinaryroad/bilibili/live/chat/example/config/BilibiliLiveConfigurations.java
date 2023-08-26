package tech.ordinaryroad.bilibili.live.chat.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import tech.ordinaryroad.live.chat.client.bilibili.config.BilibiliLiveChatClientConfig;

/**
 * @author mjz
 * @date 2023/8/21
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tech.ordinaryroad.bilibili.live.chat.client")
public class BilibiliLiveConfigurations {

    private BilibiliLiveChatClientConfig config = new BilibiliLiveChatClientConfig();

}
