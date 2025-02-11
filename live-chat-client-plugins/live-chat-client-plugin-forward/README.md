### LiveChatClient 「消息转发」插件

> 更多代码可参考B站LiveChatClient测试样例中的`BilibiliLiveChatClientTest#forwardMsgTest`

#### 引入插件

```xml

<dependency>
    <groupId>tech.ordinaryroad</groupId>
    <artifactId>live-chat-client-plugin-forward</artifactId>
    <!-- 参考github release版本，不需要前缀`v` -->
    <version>${ordinaryroad-live-chat-client.version}</version>
</dependency>
```

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

3. 自定义插件实现「消息转发」插件功能

```java
// 自定义插件，实现「消息转发插件」的功能
IPlugin plugin = new IPlugin() {
            WebSocketLiveChatClient webSocketLiveChatClient;
            final IBilibiliMsgListener msgListener = new IBilibiliMsgListener() {
                @Override
                public void onDanmuMsg(DanmuMsgMsg msg) {
                    webSocketLiveChatClient.send(new BinaryWebSocketFrame(ByteBufAllocator.DEFAULT.buffer().writeBytes(("弹幕：" + msg.getContent()).getBytes(StandardCharsets.UTF_8))));
                }
            };

            @Override
            public <LiveChatClient extends BaseLiveChatClient<?, ?, MsgListener>, MsgListener extends IBaseMsgListener<?, ?>>
            void register(LiveChatClient liveChatClient, Class<MsgListener> msgListenerClass) {
                webSocketLiveChatClient = new WebSocketLiveChatClient(WebSocketLiveChatClientConfig
                        .builder()
                        .websocketUri("ws://localhost:8080/websocket")
                        .build());
                webSocketLiveChatClient.addStatusChangeListener((evt, oldStatus, newStatus) -> {
                    if (newStatus == ClientStatusEnums.CONNECTED) {
                        // TODO 自定义操作，例如发送认证包
                        webSocketLiveChatClient.send(new BinaryWebSocketFrame(ByteBufAllocator.DEFAULT.buffer().writeBytes("认证包".getBytes(StandardCharsets.UTF_8))));
                    }
                });
                webSocketLiveChatClient.connect();
                liveChatClient.addMsgListener((MsgListener) msgListener);
            }

            @Override
            public <LiveChatClient extends BaseLiveChatClient<?, ?, MsgListener>, MsgListener extends IBaseMsgListener<?, ?>>
            void unregister(LiveChatClient liveChatClient, Class<MsgListener> msgListenerClass) {
                webSocketLiveChatClient.destroy();
                liveChatClient.removeMsgListener((MsgListener) msgListener);
            }
        };
```

#### 添加插件

```java
LiveChatClient.addPlugin(plugin);
```

#### 移除插件

```java
LiveChatClient.removePlugin(plugin);
```