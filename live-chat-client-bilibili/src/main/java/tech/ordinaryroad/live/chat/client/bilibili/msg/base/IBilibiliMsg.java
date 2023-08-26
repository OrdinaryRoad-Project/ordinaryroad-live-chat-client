package tech.ordinaryroad.live.chat.client.bilibili.msg.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tech.ordinaryroad.live.chat.client.bilibili.constant.OperationEnum;
import tech.ordinaryroad.live.chat.client.bilibili.constant.ProtoverEnum;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IMsg;

/**
 * @author mjz
 * @date 2023/8/26
 */
public interface IBilibiliMsg extends IMsg {

    @JsonIgnore
    ProtoverEnum getProtoverEnum();

    @JsonIgnore
    OperationEnum getOperationEnum();

}
