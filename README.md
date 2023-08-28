# ordinaryroad-live-chat-client

![license](https://img.shields.io/github/license/OrdinaryRoad-Project/ordinaryroad-live-chat-client) ![release](https://img.shields.io/github/v/release/OrdinaryRoad-Project/ordinaryroad-live-chat-client) ![Maven Central](https://img.shields.io/maven-central/v/tech.ordinaryroad/live-chat-client)

> This project is in progress... 👨‍💻
>
> ToDo List: https://github.com/orgs/OrdinaryRoad-Project/projects/1

- [x] Bilibili
- [ ] Douyu
    - [x] DouyuLiveChatClient
    - [ ] 支持 cookie
    - [ ] 支持 短房间id

- [ ] Huya
- [ ] ...

---

Live room WebSocket chat client

- Feature 0: Netty
- Feature 1: 消息中的未知属性统一放到单独的MAP中
- Feature 2: 支持自动重连

### Install

```xml

<dependency>
    <groupId>tech.ordinaryroad</groupId>
    <artifactId>live-chat-client-bilibili</artifactId>
    <!-- 参考github release版本，不需要前缀`v` -->
    <version>${ordinaryroad-live-chat-client.version}</version>
</dependency>
```  

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
