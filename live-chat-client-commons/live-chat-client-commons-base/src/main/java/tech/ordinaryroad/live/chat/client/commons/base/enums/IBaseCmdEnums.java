package tech.ordinaryroad.live.chat.client.commons.base.enums;

/**
 * @author mjz
 * @date 2023/8/26
 */
public interface IBaseCmdEnums<ENUM extends Enum<ENUM>> {

    String getCode();

    String getDescription();

    Enum<ENUM> getEnum();


}
