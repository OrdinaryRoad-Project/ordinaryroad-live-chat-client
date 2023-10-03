package tech.ordinaryroad.live.chat.client.huya.util;

import org.junit.jupiter.api.Test;
import tech.ordinaryroad.live.chat.client.huya.msg.WSConnectParaInfo;

/**
 * @author mjz
 * @date 2023/10/3
 */
class HuyaCodecUtilTest {

    private String ver = "2309271152";
    private String exp = "15547.23738,16582.25335,32083.50834";
    private String appSrc = "HUYA&ZH&2052";

    @Test
    void ab2str() {
        WSConnectParaInfo wsConnectParaInfo = WSConnectParaInfo.newWSConnectParaInfo(ver, exp, appSrc);
        byte[] byteArray = wsConnectParaInfo.toByteArray();
        String s = HuyaCodecUtil.ab2str(byteArray);
        System.out.println(s);
    }

    @Test
    void btoa() {
        WSConnectParaInfo wsConnectParaInfo = WSConnectParaInfo.newWSConnectParaInfo(ver, exp, appSrc);
        byte[] byteArray = wsConnectParaInfo.toByteArray();
        String s = HuyaCodecUtil.ab2str(byteArray);

        String btoa = HuyaCodecUtil.btoa(s);
        System.out.println(btoa);
    }
}