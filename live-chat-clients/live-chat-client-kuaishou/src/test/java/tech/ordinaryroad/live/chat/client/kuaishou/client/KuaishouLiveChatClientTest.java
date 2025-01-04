package tech.ordinaryroad.live.chat.client.kuaishou.client;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.constant.RoomInfoGetTypeEnum;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouDanmuMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouGiftMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouLikeMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.msg.KuaishouRoomStatsMsg;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PayloadTypeOuterClass;
import tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.WebGiftFeedOuterClass;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ICmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;
import tech.ordinaryroad.live.chat.client.commons.client.enums.ClientStatusEnums;
import tech.ordinaryroad.live.chat.client.kuaishou.config.KuaishouLiveChatClientConfig;
import tech.ordinaryroad.live.chat.client.kuaishou.listener.IKuaishouMsgListener;
import tech.ordinaryroad.live.chat.client.kuaishou.netty.handler.KuaishouBinaryFrameHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mjz
 * @date 2024/1/5
 */
@Slf4j
class KuaishouLiveChatClientTest {

    Map<String, List<WebGiftFeedOuterClass.WebGiftFeed>> map = new HashMap<>();

    static Object lock = new Object();
    KuaishouLiveChatClient client;

    @Test
    void example() throws InterruptedException {
        String cookie = System.getenv("cookie");
        log.error("cookie: {}", cookie);
        KuaishouLiveChatClientConfig config = KuaishouLiveChatClientConfig.builder()

                // .forwardWebsocketUri("ws://127.0.0.1:8080/websocket")

                // TODO 浏览器Cookie
                .cookie(cookie)
                .roomId("3xpbyu443usqwau")
                .roomId("DX204317461")
                .roomId("xzx11234")
                .roomId("N-ik-T8-20")
                .roomId("3x9f7e3t3fsr74k")
                .roomId("3xiqpb2riusznvq")
                .roomId("QQ2027379716")
                .roomId("xiannvwan1008")
                // 祁天道
                .roomId("t8888888")
                .roomId("by529529")
                // 大师2
                .roomId("3x6pb6bcmjrarvs")
                .roomId("3xbyfeffjhky7b2")
                // 月神
                .roomId("YUE99999")
                .roomId("mengyu980726")
                // 大师1
                .roomId("3xkz5pb2kx3q4u6")

                .roomId("xx6892530")
                .roomId("yc566000")
                .roomId("tianci666")
                .roomId("kslibai66")


                .roomId("Ouluo52134")
                .roomId("cutgirl325")


                .roomId("KPL704668133")
                // .roomInfoGetType(RoomInfoGetTypeEnum.NOT_COOKIE)
                .build();

        client = new KuaishouLiveChatClient(config, new IKuaishouMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                // log.debug("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
                // log.debug("收到CMD消息{}", cmd);
            }

            @Override
            public void onOtherCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
                log.debug("收到其他CMD消息 {}", cmd);
            }

            @Override
            public void onUnknownCmd(String cmdString, IMsg msg) {
                log.debug("收到未知CMD消息 {}", cmdString);
            }

            @Override
            public void onDanmuMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouDanmuMsg msg) {
                if (msg.getBadgeLevel() > 0) {
                    String badgeName = msg.getBadgeName();
                    if (StrUtil.isBlank(badgeName)) {
                        msg.getBadgeName();
                    }
                }

                log.info("{} 收到弹幕 [{}] {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onGiftMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouGiftMsg msg) {
                if (msg.getBadgeLevel() > 0) {
                    String badgeName = msg.getBadgeName();
                    if (StrUtil.isBlank(badgeName)) {
                        msg.getBadgeName();
                    }
                }

                String mergeKey = msg.getMsg().getMergeKey();
                map.computeIfAbsent(mergeKey, s -> new ArrayList<>()).add(msg.getMsg());

                if (msg.getGiftCount() > 0) {
                    log.info("{} 收到礼物 [{}] {}({}) {} {}({})x{}({}) mergeKey:{},comboCount:{}, batchSize:{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), "赠送", msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice(), msg.getMsg().getMergeKey(), msg.getMsg().getComboCount(), msg.getMsg().getBatchSize());
                }
            }

            @Override
            public void onLikeMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouLikeMsg msg) {
                if (msg.getBadgeLevel() > 0) {
                    String badgeName = msg.getBadgeName();
                    if (StrUtil.isBlank(badgeName)) {
                        msg.getBadgeName();
                    }
                }
                log.info("{} 收到点赞 [{}] {}({})", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid());
            }

            @Override
            public void onRoomStatsMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouRoomStatsMsg msg) {
                IKuaishouMsgListener.super.onRoomStatsMsg(binaryFrameHandler, msg);
                log.info("{} 统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", binaryFrameHandler.getRoomId(), msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
            }
        });

        client.addStatusChangeListener((evt, oldStatus, newStatus) -> {
            if (newStatus == ClientStatusEnums.CONNECTED) {
                log.warn("{} 已连接", client.getConfig().getRoomId());
                log.warn("直播间标题 {}", client.getRoomInitResult().getRoomTitle());
                // 连接成功10秒后发送弹幕
                ThreadUtil.execAsync(() -> {
                    ThreadUtil.sleep(10000);
                    client.sendDanmu("666666", new Runnable() {
                        @Override
                        public void run() {
                            log.warn("弹幕发送成功");
                        }
                    });
                });
            }
        });

        client.connect();

        // 防止测试时直接退出
        while (true) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    @Test
    void clickLike() throws Exception {
        String cookie = System.getenv("cookie");
//        String cookie ="";
        log.error("cookie: {}", cookie);
        KuaishouLiveChatClientConfig config = KuaishouLiveChatClientConfig.builder()
                .cookie(cookie)
                .roomId("K6866676")
                .roomId("xinsang_")
                .roomId("lollaopu")
                .roomId("YTC2844073618")
                .build();

        client = new KuaishouLiveChatClient(config, new IKuaishouMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
//                log.debug("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
//                log.debug("收到CMD消息{} {}", cmd, cmdMsg);
            }

            @Override
            public void onOtherCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
//                log.debug("收到其他CMD消息 {}", cmd);
            }

            @Override
            public void onUnknownCmd(String cmdString, IMsg msg) {
//                log.debug("收到未知CMD消息 {}", cmdString);
            }

            @Override
            public void onDanmuMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouDanmuMsg msg) {
                log.info("{} 收到弹幕 [{}] {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onGiftMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouGiftMsg msg) {
                log.info("{} 收到礼物 [{}] {}({}) {} {}({})x{}({})", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), "赠送", msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice());
            }

            @Override
            public void onLikeMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouLikeMsg msg) {
                log.info("{} 收到点赞 [{}] {}({})", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid());
            }
        });

        client.addStatusChangeListener((evt, oldStatus, newStatus) -> {
            if (newStatus == ClientStatusEnums.CONNECTED) {
                // 连接成功10秒后发送弹幕
                ThreadUtil.execAsync(() -> {
                    ThreadUtil.sleep(10000);
                    client.sendDanmu("6666a", () -> {
                        log.warn("弹幕发送成功");
                        client.clickLike(13, () -> {
                            log.warn("为直播间点赞成功");
                        });
                    });
                });
            }
        });

        client.connect();

        // 防止测试时直接退出
        while (true) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

//    public static void chineseToCode() throws UnsupportedEncodingException {
//        //1.url编码出字符串
//        //str = %E5%A4%A7%E5%AE%B6%E5%A5%BD%E3%80%82
//        String str = URLEncoder.encode("欧皇粉", "UTF-8");
//        //2.获取十六进制表示的字符串
//        String[] s = str.substring(1).split("%");
//        StringBuffer s8 = new StringBuffer();
//        //3.把每个十六进制字符串转换成八进制字符串，形式为:\+数字
//        for (int i = 0; i < s.length; i++) {
//            s8.append("\\" + Integer.toOctalString(Integer.valueOf(s[i], 16)));
//        }
//        System.out.println(s8.toString());
//    }
//
//    public static void codeToChinese() throws UnsupportedEncodingException {
//        //\345\244\247\345\256\266\345\245\275\343\200\202
//        String code = "\\347\\210\\271\\345\\246\\210\\345\\233\\242";
//        //1.获取八进制数据
//        String[] split = code.substring(1).split("\\\\");
//
//        StringBuffer s16 = new StringBuffer();
//
//        //2.把八进制转换成格式为:%+十六进制
//        for (String s : split) {
//            s16.append("%" + Integer.toHexString(Integer.valueOf(s, 8)).toString().toUpperCase());
//        }
//
//        //3.解码
//        String decode = URLDecoder.decode(s16.toString(), "UTF-8");
//        System.out.println("decode = " + decode);
//
//        System.out.println(s16);
////
//    }
//
//
//    public static void main(String[] args) throws UnsupportedEncodingException {
////        chineseToCode();
//        codeToChinese();
//    }


    @Test
    void notCookie() throws InterruptedException {
        KuaishouLiveChatClientConfig config = KuaishouLiveChatClientConfig.builder()
                .roomInfoGetType(RoomInfoGetTypeEnum.NOT_COOKIE)
                .roomId("y4357890213")
                .build();

        client = new KuaishouLiveChatClient(config, new IKuaishouMsgListener() {
            @Override
            public void onMsg(IMsg msg) {
                // log.debug("收到{}消息 {}", msg.getClass(), msg);
            }

            @Override
            public void onCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
                // log.debug("收到CMD消息{}", cmd);
            }

            @Override
            public void onOtherCmdMsg(PayloadTypeOuterClass.PayloadType cmd, ICmdMsg<PayloadTypeOuterClass.PayloadType> cmdMsg) {
                log.debug("收到其他CMD消息 {}", cmd);
            }

            @Override
            public void onUnknownCmd(String cmdString, IMsg msg) {
                log.debug("收到未知CMD消息 {}", cmdString);
            }

            @Override
            public void onDanmuMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouDanmuMsg msg) {
                if (msg.getBadgeLevel() > 0) {
                    String badgeName = msg.getBadgeName();
                    if (StrUtil.isBlank(badgeName)) {
                        msg.getBadgeName();
                    }
                }

                log.info("{} 收到弹幕 [{}] {}({})：{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), msg.getContent());
            }

            @Override
            public void onGiftMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouGiftMsg msg) {
                if (msg.getBadgeLevel() > 0) {
                    String badgeName = msg.getBadgeName();
                    if (StrUtil.isBlank(badgeName)) {
                        msg.getBadgeName();
                    }
                }

                String mergeKey = msg.getMsg().getMergeKey();
                map.computeIfAbsent(mergeKey, s -> new ArrayList<>()).add(msg.getMsg());

                if (msg.getGiftCount() > 0) {
                    log.info("{} 收到礼物 [{}] {}({}) {} {}({})x{}({}) mergeKey:{},comboCount:{}, batchSize:{}", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid(), "赠送", msg.getGiftName(), msg.getGiftId(), msg.getGiftCount(), msg.getGiftPrice(), msg.getMsg().getMergeKey(), msg.getMsg().getComboCount(), msg.getMsg().getBatchSize());
                }
            }

            @Override
            public void onLikeMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouLikeMsg msg) {
                if (msg.getBadgeLevel() > 0) {
                    String badgeName = msg.getBadgeName();
                    if (StrUtil.isBlank(badgeName)) {
                        msg.getBadgeName();
                    }
                }
                log.info("{} 收到点赞 [{}] {}({})", binaryFrameHandler.getRoomId(), msg.getBadgeLevel() != 0 ? msg.getBadgeLevel() + msg.getBadgeName() : "", msg.getUsername(), msg.getUid());
            }

            @Override
            public void onRoomStatsMsg(KuaishouBinaryFrameHandler binaryFrameHandler, KuaishouRoomStatsMsg msg) {
                IKuaishouMsgListener.super.onRoomStatsMsg(binaryFrameHandler, msg);
                log.info("{} 统计信息 累计点赞数: {}, 当前观看人数: {}, 累计观看人数: {}", binaryFrameHandler.getRoomId(), msg.getLikedCount(), msg.getWatchingCount(), msg.getWatchedCount());
            }
        });

        client.addStatusChangeListener((evt, oldStatus, newStatus) -> {
            if (newStatus == ClientStatusEnums.CONNECTED) {
                // 连接成功10秒后发送弹幕
                ThreadUtil.execAsync(() -> {
                    ThreadUtil.sleep(10000);
                    client.sendDanmu("666666", new Runnable() {
                        @Override
                        public void run() {
                            log.warn("弹幕发送成功");
                        }
                    });
                });
            }
        });

        client.connect();

        // 防止测试时直接退出
        while (true) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

}