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

package tech.ordinaryroad.live.chat.client.codec.douyu.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.codec.douyu.msg.base.BaseDouyuCmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.ISuperChatMsg;

/**
 * <pre>{@code
 * {
 * 	"type": "comm_chatmsg",
 * 	"vrid": "1856171486511906816",
 * 	"btype": "voiceDanmu",
 * 	"chatmsg": {
 * 		"nn": "King彡吖西",
 * 		"level": "14",
 * 		"type": "chatmsg",
 * 		"rid": "1126960",
 * 		"gag": "0",
 * 		"uid": "16751345",
 * 		"txt": "c桑又在赞elo了，上分给你玩明白了",
 * 		"hidenick": "0",
 * 		"nc": "0",
 * 		"ic": ["avatar", "016", "75", "13", "45_avatar"],
 * 		"nl": "0",
 * 		"tbid": "0",
 * 		"tbl": "0",
 * 		"tbvip": "0"
 *  },
 * 	"range": "2",
 * 	"cprice": "3000",
 * 	"cmgType": "1",
 * 	"rid": "1126960",
 * 	"gbtemp": "2",
 * 	"uid": "16751345",
 * 	"crealPrice": "3000",
 * 	"cet": "60",
 * 	"now": "1731380814042",
 * 	"csuperScreen": "0",
 * 	"danmucr": "1"
 * }
 * }</pre>
 *
 * @author mjz
 * @date 2025/1/15
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommChatmsgMsg extends BaseDouyuCmdMsg implements ISuperChatMsg {
    private String vrid;
    private String btype;
    private ChatmsgMsg chatmsg;
    private String range;
    private String cprice;
    private String cmgType;
    private String rid;
    private String gbtemp;
    private String uid;
    private String crealPrice;
    /**
     * 持续时长（秒）
     */
    private Integer cet;
    private String now;
    private String csuperScreen;
    private String danmucr;

    @Override
    public int getDuration() {
        return this.cet;
    }

    @Override
    public String getBadgeName() {
        return this.chatmsg.getBadgeName();
    }

    @Override
    public byte getBadgeLevel() {
        return this.chatmsg.getBadgeLevel();
    }

    @Override
    public String getUsername() {
        return this.chatmsg.getUsername();
    }

    @Override
    public String getUserAvatar() {
        return this.chatmsg.getUserAvatar();
    }

    @Override
    public String getContent() {
        return this.chatmsg.getContent();
    }
}
