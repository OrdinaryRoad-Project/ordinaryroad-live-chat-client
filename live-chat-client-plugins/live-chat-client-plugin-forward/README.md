### LiveChatClient 「消息转发」插件

#### 创建插件

1. 基础用法

```java
// 目的地址
String forwardWebsocketUri = "ws://localhost:8080/websocket";
IPlugin plugin = new ForwardMsgPlugin(forwardWebsocketUri);
```

2. 自定义转发操作

```java
// 目的地址
String forwardWebsocketUri = "ws://localhost:8080/websocket";
// 处理转发消息
IForwardMsgHandler forwardMsgHandler = (webSocketLiveChatClient, msg) -> {
    ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
    byteBuf.writeCharSequence("自定义消息转发：" + msg.toString(), StandardCharsets.UTF_8);
    webSocketLiveChatClient.send(new BinaryWebSocketFrame(byteBuf));
};
IPlugin plugin = new ForwardMsgPlugin(forwardWebsocketUri, forwardMsgHandler);
```

#### 添加插件

```java
client.addPlugin(plugin);
```