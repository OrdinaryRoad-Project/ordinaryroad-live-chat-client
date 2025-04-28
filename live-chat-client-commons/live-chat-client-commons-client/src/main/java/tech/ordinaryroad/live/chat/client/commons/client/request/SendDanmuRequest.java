package tech.ordinaryroad.live.chat.client.commons.client.request;

import lombok.Data;

@Data
public class SendDanmuRequest {
    private String msg;
    private String replyUid;
    private String replyUsername;
}