package tech.ordinaryroad.live.chat.client.servers.netty.client.config;

import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.*;
import lombok.experimental.SuperBuilder;
import tech.ordinaryroad.live.chat.client.commons.client.config.BaseLiveChatClientConfig;

/**
 * @author mjz
 * @date 2023/8/26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseNettyLiveChatClientConfig extends BaseLiveChatClientConfig {

    /**
     * 聚合器允许的最大消息体长度，默认 64*1024 byte
     *
     * @see HttpObjectAggregator#HttpObjectAggregator(int)
     */
    @Builder.Default
    private int aggregatorMaxContentLength = 64 * 1024;
}
