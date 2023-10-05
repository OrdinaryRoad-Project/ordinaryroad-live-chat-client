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

package tech.ordinaryroad.live.chat.client.huya.msg;

import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.TarsOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IDanmuMsg;
import tech.ordinaryroad.live.chat.client.huya.constant.HuyaOperationEnum;
import tech.ordinaryroad.live.chat.client.huya.msg.base.BaseHuyaMsg;
import tech.ordinaryroad.live.chat.client.huya.msg.dto.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mjz
 * @date 2023/10/2
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageNoticeMsg extends BaseHuyaMsg implements IDanmuMsg {

    private SenderInfo tUserInfo = new SenderInfo();
    private long lTid;
    private long lSid;
    private String sContent = "";
    private int iShowMode;
    private ContentFormat tFormat = new ContentFormat();
    private BulletFormat tBulletFormat = new BulletFormat();
    private int iTermType;
    private List<DecorationInfo> vDecorationPrefix = new ArrayList<>() {{
        add(new DecorationInfo());
    }};
    private List<DecorationInfo> vDecorationSuffix = new ArrayList<>() {{
        add(new DecorationInfo());
    }};
    private List<UidNickName> vAtSomeone = new ArrayList<>() {{
        add(new UidNickName());
    }};
    private long lPid;

    public MessageNoticeMsg(TarsInputStream is) {
        this.readFrom(is);
    }

    @Override
    public void writeTo(TarsOutputStream os) {
        os.write(this.tUserInfo, 0);
        os.write(this.lTid, 1);
        os.write(this.lSid, 2);
        os.write(this.sContent, 3);
        os.write(this.iShowMode, 4);
        os.write(this.tFormat, 5);
        os.write(this.tBulletFormat, 6);
        os.write(this.iTermType, 7);
        os.write(this.vDecorationPrefix, 8);
        os.write(this.vDecorationSuffix, 9);
        os.write(this.vAtSomeone, 10);
        os.write(this.lPid, 11);
    }

    @Override
    public void readFrom(TarsInputStream is) {
        this.tUserInfo = (SenderInfo) is.directRead(this.tUserInfo, 0, true);
        this.lTid = is.read(this.lTid, 1, true);
        this.lSid = is.read(this.lSid, 2, true);
        this.sContent = is.readString(3, true);
        this.iShowMode = is.read(this.iShowMode, 4, true);
        this.tFormat = (ContentFormat) is.directRead(this.tFormat, 5, true);
        this.tBulletFormat = (BulletFormat) is.directRead(this.tBulletFormat, 6, true);
        this.iTermType = is.read(this.iTermType, 7, true);
        this.vDecorationPrefix = is.readArray(this.vDecorationPrefix, 8, true);
        this.vDecorationSuffix = is.readArray(this.vDecorationSuffix, 9, true);
        this.vAtSomeone = is.readArray(this.vAtSomeone, 10, true);
        this.lPid = is.read(this.lPid, 11, true);
    }

    @Override
    public HuyaOperationEnum getOperationEnum() {
        return HuyaOperationEnum.EWSCmdS2C_MsgPushReq;
    }

    @Override
    public long getUid() {
        return this.tUserInfo.getLUid();
    }

    @Override
    public String getUsername() {
        return this.tUserInfo.getSNickName();
    }

    @Override
    public String getUserAvatar() {
        return this.tUserInfo.getSAvatarUrl();
    }

    @Override
    public String getContent() {
        return this.sContent;
    }
}
