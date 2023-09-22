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

package tech.ordinaryroad.live.chat.client.bilibili.msg;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ordinaryroad.live.chat.client.bilibili.constant.OperationEnum;
import tech.ordinaryroad.live.chat.client.bilibili.msg.base.BaseBilibiliCmdMsg;
import tech.ordinaryroad.live.chat.client.commons.base.msg.IGiftMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mjz
 * @date 2023/9/8
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendGiftMsg extends BaseBilibiliCmdMsg implements IGiftMsg {

    private Data data;

    @Override
    public OperationEnum getOperationEnum() {
        return OperationEnum.SEND_SMS_REPLY;
    }

    @Override
    public long getUid() {
        return data.getUid();
    }

    @Override
    public String getUsername() {
        return data.getUname();
    }

    @Override
    public String getGiftName() {
        return data.getGiftName();
    }

    @Override
    public long getGiftId() {
        return data.getGiftId();
    }

    @Override
    public int getGiftCount() {
        return data.getNum();
    }

    @Override
    public int getGiftPrice() {
        return data.getPrice();
    }

    @Override
    public long getReceiveUid() {
        return data.getReceive_user_info().getUid();
    }

    @Override
    public String getReceiveUsername() {
        return data.getReceive_user_info().getUname();
    }

    @lombok.Data
    public static class Data {

        private int draw;
        private int gold;
        private int silver;
        private int num;
        private int total_coin;
        private int effect;
        private int broadcast_id;
        private int crit_prob;
        private int guard_level;
        private long rcost;
        private long uid;
        private long timestamp;
        private int giftId;
        private int giftType;
        @JsonProperty("super")
        private int _super;
        private int super_gift_num;
        private int super_batch_gift_num;
        private int remain;
        private int discount_price;
        private int price;
        private String beatId;
        private String biz_source;
        private String action;
        private String coin_type;
        private String uname;
        private String face;
        private String batch_combo_id;
        private String rnd;
        private String giftName;
        private String original_gift_name;
        private Combo_send combo_send;
        private Batch_combo_send batch_combo_send;
        private String tag_image;
        private String top_list;
        private String send_master;
        private boolean is_first;
        private int demarcation;
        private int combo_stay_time;
        private int combo_total_coin;
        private String tid;
        private int effect_block;
        private int is_special_batch;
        private int combo_resources_id;
        private int magnification;
        private String name_color;
        private Medal_info medal_info;
        private int svga_block;
        private String blind_gift;
        private int float_sc_resource_id;
        @JsonProperty("switch")
        private boolean _switch;
        private int face_effect_type;
        private int face_effect_id;
        private boolean is_naming;
        private Receive_user_info receive_user_info;
        private boolean is_join_receiver;
        private Bag_gift bag_gift;
        private int wealth_level;

        /**
         * 未知属性都放在这
         */
        private final Map<String, JsonNode> unknownProperties = new HashMap<>();

        @JsonAnyGetter
        public Map<String, JsonNode> getUnknownProperties() {
            return unknownProperties;
        }

        @JsonAnySetter
        public void setOther(String key, JsonNode value) {
            this.unknownProperties.put(key, value);
        }
    }

    @lombok.Data
    public static class Medal_info {

        private long target_id;
        private String special;
        private int icon_id;
        private String anchor_uname;
        private int anchor_roomid;
        private int medal_level;
        private String medal_name;
        private long medal_color;
        private long medal_color_start;
        private long medal_color_end;
        private long medal_color_border;
        private int is_lighted;
        private int guard_level;

        /**
         * 未知属性都放在这
         */
        private final Map<String, JsonNode> unknownProperties = new HashMap<>();

        @JsonAnyGetter
        public Map<String, JsonNode> getUnknownProperties() {
            return unknownProperties;
        }

        @JsonAnySetter
        public void setOther(String key, JsonNode value) {
            this.unknownProperties.put(key, value);
        }
    }

    @lombok.Data
    public static class Combo_send {

        private long uid;
        private int gift_num;
        private int combo_num;
        private int gift_id;
        private String combo_id;
        private String gift_name;
        private String action;
        private String uname;
        private String send_master;

        /**
         * 未知属性都放在这
         */
        private final Map<String, JsonNode> unknownProperties = new HashMap<>();

        @JsonAnyGetter
        public Map<String, JsonNode> getUnknownProperties() {
            return unknownProperties;
        }

        @JsonAnySetter
        public void setOther(String key, JsonNode value) {
            this.unknownProperties.put(key, value);
        }
    }

    @lombok.Data
    public static class Receive_user_info {

        private String uname;
        private long uid;

        /**
         * 未知属性都放在这
         */
        private final Map<String, JsonNode> unknownProperties = new HashMap<>();

        @JsonAnyGetter
        public Map<String, JsonNode> getUnknownProperties() {
            return unknownProperties;
        }

        @JsonAnySetter
        public void setOther(String key, JsonNode value) {
            this.unknownProperties.put(key, value);
        }
    }

    @lombok.Data
    public static class Batch_combo_send {

        private long uid;
        private int gift_num;
        private int batch_combo_num;
        private int gift_id;
        private String batch_combo_id;
        private String gift_name;
        private String action;
        private String uname;
        private String send_master;
        private JsonNode blind_gift;

        /**
         * 未知属性都放在这
         */
        private final Map<String, JsonNode> unknownProperties = new HashMap<>();

        @JsonAnyGetter
        public Map<String, JsonNode> getUnknownProperties() {
            return unknownProperties;
        }

        @JsonAnySetter
        public void setOther(String key, JsonNode value) {
            this.unknownProperties.put(key, value);
        }
    }

    @lombok.Data
    public static class Bag_gift {

        private int show_price;
        private int price_for_show;

        /**
         * 未知属性都放在这
         */
        private final Map<String, JsonNode> unknownProperties = new HashMap<>();

        @JsonAnyGetter
        public Map<String, JsonNode> getUnknownProperties() {
            return unknownProperties;
        }

        @JsonAnySetter
        public void setOther(String key, JsonNode value) {
            this.unknownProperties.put(key, value);
        }
    }
}
