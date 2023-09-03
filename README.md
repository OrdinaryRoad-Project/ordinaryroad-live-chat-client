# ordinaryroad-live-chat-client

![license](https://img.shields.io/github/license/OrdinaryRoad-Project/ordinaryroad-live-chat-client) ![release](https://img.shields.io/github/v/release/OrdinaryRoad-Project/ordinaryroad-live-chat-client) ![Maven Central](https://img.shields.io/maven-central/v/tech.ordinaryroad/live-chat-client)

> This project is in progress... 👨‍💻
>
> 有问题欢迎提交[issuse](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/issues)，
> 觉得有用的话可以点个小星星⭐️鼓励一下，感谢
>
> 如果对项目感兴趣也欢迎[交流讨论](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/discussions)，
> 提交PR
>
> ToDo List: https://github.com/orgs/OrdinaryRoad-Project/projects/1

- [x] Bilibili
    - [x] BilibiliLiveChatClient
    - [x] 支持 cookie
    - [x] 支持 短房间id
- [x] Douyu
    - [x] DouyuLiveChatClient
    - [ ] ~~支持 cookie~~（暂时不需要）
    - [x] 支持 短房间id

- [ ] Huya
- [ ] ...

---

Live room WebSocket chat client

- Feature 0: Netty
- Feature 1: 消息中的未知属性统一放到单独的MAP中
- Feature 2: 支持自动重连
- Feature 3: 支持同时监听多个直播间
- Feature 4: 支持直播间短id

[//]: # ([在线文档]&#40;https://ordinaryroad.tech/or_module/live-chat-client/&#41;)

## 1 安装

### B站

```xml

<dependency>
    <groupId>tech.ordinaryroad</groupId>
    <artifactId>live-chat-client-bilibili</artifactId>
    <!-- 参考github release版本，不需要前缀`v` -->
    <version>${ordinaryroad-live-chat-client.version}</version>
</dependency>
```  

### 斗鱼

```xml

<dependency>
    <groupId>tech.ordinaryroad</groupId>
    <artifactId>live-chat-client-douyu</artifactId>
    <!-- 参考github release版本，不需要前缀`v` -->
    <version>${ordinaryroad-live-chat-client.version}</version>
</dependency>
```

## 2 使用

### 2.1 Client模式

> Spring Boot 3 示例 [client-example](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/tree/main/live-chat-client-examples/client-example)

```java
public class ClientModeExample {
    public static void main(String[] args) {
        String cookie = System.getenv("cookie");
        BilibiliLiveChatClientConfig config = BilibiliLiveChatClientConfig.builder()
                // TODO 浏览器Cookie
                .cookie(cookie)
                // TODO 直播间id（支持短id）
                .roomId(7777)
                .build();

        BilibiliLiveChatClient client = new BilibiliLiveChatClient(config, new IBilibiliSendSmsReplyMsgListener() {
            @Override
            public void onDanmuMsg(BilibiliBinaryFrameHandler binaryFrameHandler, SendSmsReplyMsg msg) {
                IBilibiliSendSmsReplyMsgListener.super.onDanmuMsg(binaryFrameHandler, msg);

                JsonNode info = msg.getInfo();
                JsonNode jsonNode1 = info.get(1);
                String danmuText = jsonNode1.asText();
                JsonNode jsonNode2 = info.get(2);
                Long uid = jsonNode2.get(0).asLong();
                String uname = jsonNode2.get(1).asText();
                System.out.printf("%s 收到弹幕 %s(%d)：%s%n", binaryFrameHandler.getRoomId(), uname, uid, danmuText);
            }
        });
        client.connect();
    }
}
```

### 2.2 高级模式

> 参考 [BilibiliHandlerModeExample](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/tree/main/live-chat-client-examples/handler-example/src/main/java/tech/ordinaryroad/live/chat/client/example/handler/BilibiliHandlerModeExample.java)
