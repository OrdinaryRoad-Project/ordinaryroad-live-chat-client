package tech.ordinaryroad.bilibili.live.chat.example;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;
import tech.ordinaryroad.live.chat.client.bilibili.client.BilibiliLiveChatClient;

@Slf4j
@SpringBootApplication
public class BilibiliLiveChatExampleApplication {

    @Resource
    private BilibiliLiveChatClient bilibiliLiveChatClient;

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("run");
        SpringApplication.run(BilibiliLiveChatExampleApplication.class, args);
        stopWatch.stop();
        log.info("run end！ {}", stopWatch.prettyPrint());
    }

    @PreDestroy
    public void preDestroy() {
        bilibiliLiveChatClient.destroy();
    }

}
