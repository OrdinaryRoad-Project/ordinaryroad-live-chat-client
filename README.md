# ordinaryroad-live-chat-client

![license](https://img.shields.io/github/license/OrdinaryRoad-Project/ordinaryroad-live-chat-client) ![release](https://img.shields.io/github/v/release/OrdinaryRoad-Project/ordinaryroad-live-chat-client) ![Maven Central](https://img.shields.io/maven-central/v/tech.ordinaryroad/live-chat-client)

> This project is in progress... 👨‍💻
> 
> 有问题欢迎提交[issuse](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/issues)，觉得有用的话可以点个小星星⭐️鼓励一下，感谢
>
> 如果对项目感兴趣也欢迎[交流讨论](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/discussions)、提交PR
>
> ToDo List: https://github.com/orgs/OrdinaryRoad-Project/projects/1


- [x] Bilibili
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

[//]: # ([在线文档]&#40;https://ordinaryroad.tech/or_module/live_chat_client&#41;)

### Install

#### B站

```xml

<dependency>
    <groupId>tech.ordinaryroad</groupId>
    <artifactId>live-chat-client-bilibili</artifactId>
    <!-- 参考github release版本，不需要前缀`v` -->
    <version>${ordinaryroad-live-chat-client.version}</version>
</dependency>
```  

#### 斗鱼

```xml

<dependency>
    <groupId>tech.ordinaryroad</groupId>
    <artifactId>live-chat-client-douyu</artifactId>
    <!-- 参考github release版本，不需要前缀`v` -->
    <version>${ordinaryroad-live-chat-client.version}</version>
</dependency>
```

### Examples

see [live-chat-client-examples](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client/tree/main/live-chat-client-examples)

> 参考 `bilibili-example` 即可，API基本一致
