/*
 * MIT License
 *
 * Copyright (c) 2023 OrdinaryRoad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tech.ordinaryroad.live.chat.client.codec.huya.msg;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import com.qq.tars.protocol.tars.TarsStructBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.codec.huya.constant.HuyaOperationEnum;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.base.BaseHuyaMsg;
import tech.ordinaryroad.live.chat.client.codec.huya.msg.dto.*;
import tech.ordinaryroad.live.chat.client.codec.huya.util.HuyaCodecUtil;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IGiftMsg;

import java.util.List;
import java.util.Optional;

/**
 * @author mjz
 * @date 2023/10/2
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendItemSubBroadcastPacketMsg extends BaseHuyaMsg implements IGiftMsg {

    private int iItemType;
    private String strPayId = "";
    private int iItemCount;
    private long lPresenterUid;
    private long lSenderUid;
    private String sPresenterNick = "";
    private String sSenderNick = "";
    private String sSendContent = "";
    private int iItemCountByGroup;
    private int iItemGroup;
    private int iSuperPupleLevel;
    private int iComboScore;
    private int iDisplayInfo;
    private int iEffectType;
    private String iSenderIcon = "";
    private String iPresenterIcon = "";
    private int iTemplateType;
    private String sExpand = "";
    private boolean bBusi;
    private int iColorEffectType;
    private String sPropsName = "";
    private short iAccpet;
    private short iEventType;
    private UserIdentityInfo userInfo = new UserIdentityInfo();
    private long lRoomId;
    private long lHomeOwnerUid;
    private StreamerNode streamerInfo = new StreamerNode();
    private int iPayType = -1;
    private int iNobleLevel;
    private NobleLevelInfo tNobleLevel = new NobleLevelInfo();
    private ItemEffectInfo tEffectInfo = new ItemEffectInfo();
    private List<Long> vExUid = CollUtil.newArrayList(0L);
    private int iComboStatus;
    private int iPidColorType;
    private int iMultiSend;
    private int iVFanLevel;
    private int iUpgradeLevel;
    private String sCustomText = "";
    private DIYBigGiftEffect tDIYEffect = new DIYBigGiftEffect();
    private long lComboSeqId;
    private long lPayTotal;
    private List<ItemEffectBizData> vBizData = CollUtil.newArrayList(new ItemEffectBizData());

    // region 额外属性
    private BadgeInfo badgeInfo;
    private PropsItem propsItem = PropsItem.DEFAULT;
    // endregion

    public SendItemSubBroadcastPacketMsg(TarsInputStream is) {
        this.readFrom(is);
    }

    @Override
    public void writeTo(TarsOutputStream os) {
        os.write(this.iItemType, 0);
        os.write(this.strPayId, 1);
        os.write(this.iItemCount, 2);
        os.write(this.lPresenterUid, 3);
        os.write(this.lSenderUid, 4);
        os.write(this.sPresenterNick, 5);
        os.write(this.sSenderNick, 6);
        os.write(this.sSendContent, 7);
        os.write(this.iItemCountByGroup, 8);
        os.write(this.iItemGroup, 9);
        os.write(this.iSuperPupleLevel, 10);
        os.write(this.iComboScore, 11);
        os.write(this.iDisplayInfo, 12);
        os.write(this.iEffectType, 13);
        os.write(this.iSenderIcon, 14);
        os.write(this.iPresenterIcon, 15);
        os.write(this.iTemplateType, 16);
        os.write(this.sExpand, 17);
        os.write(this.bBusi, 18);
        os.write(this.iColorEffectType, 19);
        os.write(this.sPropsName, 20);
        os.write(this.iAccpet, 21);
        os.write(this.iEventType, 22);
        os.write(this.userInfo, 23);
        os.write(this.lRoomId, 24);
        os.write(this.lHomeOwnerUid, 25);
        os.write(this.streamerInfo, 26);
        os.write(this.iPayType, 27);
        os.write(this.iNobleLevel, 28);
        os.write(this.tNobleLevel, 29);
        os.write(this.tEffectInfo, 30);
        os.write(this.vExUid, 31);
        os.write(this.iComboStatus, 32);
        os.write(this.iPidColorType, 33);
        os.write(this.iMultiSend, 34);
        os.write(this.iVFanLevel, 35);
        os.write(this.iUpgradeLevel, 36);
        os.write(this.sCustomText, 37);
        os.write(this.tDIYEffect, 38);
        os.write(this.lComboSeqId, 39);
        os.write(this.lPayTotal, 41);
        os.write(this.vBizData, 42);
    }

    @Override
    public void readFrom(TarsInputStream is) {
        this.iItemType = is.read(this.iItemType, 0, false);
        this.strPayId = is.read(this.strPayId, 1, false);
        this.iItemCount = is.read(this.iItemCount, 2, false);
        this.lPresenterUid = is.read(this.lPresenterUid, 3, false);
        this.lSenderUid = is.read(this.lSenderUid, 4, false);
        this.sPresenterNick = is.read(this.sPresenterNick, 5, false);
        this.sSenderNick = is.read(this.sSenderNick, 6, false);
        this.sSendContent = is.read(this.sSendContent, 7, false);
        this.iItemCountByGroup = is.read(this.iItemCountByGroup, 8, false);
        this.iItemGroup = is.read(this.iItemGroup, 9, false);
        this.iSuperPupleLevel = is.read(this.iSuperPupleLevel, 10, false);
        this.iComboScore = is.read(this.iComboScore, 11, false);
        this.iDisplayInfo = is.read(this.iDisplayInfo, 12, false);
        this.iEffectType = is.read(this.iEffectType, 13, false);
        this.iSenderIcon = is.read(this.iSenderIcon, 14, false);
        this.iPresenterIcon = is.read(this.iPresenterIcon, 15, false);
        this.iTemplateType = is.read(this.iTemplateType, 16, false);
        this.sExpand = is.read(this.sExpand, 17, false);
        this.bBusi = is.read(this.bBusi, 18, false);
        this.iColorEffectType = is.read(this.iColorEffectType, 19, false);
        this.sPropsName = is.read(this.sPropsName, 20, false);
        this.iAccpet = is.read(this.iAccpet, 21, false);
        this.iEventType = is.read(this.iEventType, 22, false);
        this.userInfo = (UserIdentityInfo) is.directRead(this.userInfo, 23, false);
        this.lRoomId = is.read(this.lRoomId, 24, false);
        this.lHomeOwnerUid = is.read(this.lHomeOwnerUid, 25, false);
        this.streamerInfo = (StreamerNode) is.directRead(this.streamerInfo, 26, false);
        this.iPayType = is.read(this.iPayType, 27, false);
        this.iNobleLevel = is.read(this.iNobleLevel, 28, false);
        this.tNobleLevel = (NobleLevelInfo) is.directRead(this.tNobleLevel, 29, false);
        this.tEffectInfo = (ItemEffectInfo) is.directRead(this.tEffectInfo, 30, false);
        this.vExUid = is.readArray(this.vExUid, 31, false);
        this.iComboStatus = is.read(this.iComboStatus, 32, false);
        this.iPidColorType = is.read(this.iPidColorType, 33, false);
        this.iMultiSend = is.read(this.iMultiSend, 34, false);
        this.iVFanLevel = is.read(this.iVFanLevel, 35, false);
        this.iUpgradeLevel = is.read(this.iUpgradeLevel, 36, false);
        this.sCustomText = is.read(this.sCustomText, 37, false);
        this.tDIYEffect = (DIYBigGiftEffect) is.directRead(this.tDIYEffect, 38, false);
        this.lComboSeqId = is.read(this.lComboSeqId, 39, false);
        this.lPayTotal = is.read(this.lPayTotal, 41, false);
        this.vBizData = is.readArray(this.vBizData, 42, false);

        // 解析额外属性
        for (DecorationInfo decorationPrefix : userInfo.getVDecorationPrefix()) {
            Optional<? extends TarsStructBase> optional = HuyaCodecUtil.decodeDecorationInfo(decorationPrefix);
            if (optional.isPresent()) {
                TarsStructBase tarsStructBase = optional.get();
                if (tarsStructBase instanceof BadgeInfo) {
                    this.badgeInfo = (BadgeInfo) tarsStructBase;
                    break;
                }
            }
        }
    }

    @Override
    public String getUid() {
        return Long.toString(this.lSenderUid);
    }

    @Override
    public String getUsername() {
        return this.sSenderNick;
    }

    @Override
    public String getUserAvatar() {
        return this.iSenderIcon;
    }

    @Override
    public String getGiftName() {
        return this.sPropsName;
    }

    @Override
    public String getGiftImg() {
        if (this.propsItem == null) {
            return "";
        }

        List<PropsIdentity> vPropsIdentity = this.propsItem.getVPropsIdentity();
        if (vPropsIdentity.isEmpty()) {
            return "";
        }

        PropsIdentity propsIdentity = vPropsIdentity.get(0);
        String sPropsWeb = propsIdentity.getSPropsWeb();
        if (StrUtil.isBlank(sPropsWeb)) {
            return "";
        }

        return sPropsWeb.substring(0, sPropsWeb.indexOf("&"));
    }

    @Override
    public String getGiftId() {
        return Long.toString(this.iItemType);
    }

    @Override
    public int getGiftCount() {
        return this.iItemCount;
    }

    /**
     * 100 对应 1虎牙币
     */
    @Override
    public int getGiftPrice() {
        return (int) (this.lPayTotal / this.iItemCount);
    }

    @Override
    public String getReceiveUid() {
        return Long.toString(this.lPresenterUid);
    }

    @Override
    public String getReceiveUsername() {
        return this.sPresenterNick;
    }

    @Override
    public HuyaOperationEnum getOperationEnum() {
        return HuyaOperationEnum.EWSCmdS2C_MsgPushReq;
    }
}
