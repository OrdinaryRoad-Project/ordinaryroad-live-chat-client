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
package tech.ordinaryroad.live.chat.client.codec.douyu.api.response;

import cn.hutool.db.meta.Column;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.dto.Room;
import tech.ordinaryroad.live.chat.client.codec.douyu.api.dto.Room_args;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetardResponse {

    private JsonNode room_gg;
    private int is_newbie;
    private String near_show_time;
    private JsonNode launch_remind;
    private long cache_time;
    private List<String> black;
    private String can_send_gift;
    private Room room;
    private String page_url;
    private int cate_id;
    private String share_swf_url;
    private boolean qqLotterySwitch;
    private List<String> home_ad_info;
    private String VoddUploadUrl;
    private String faceList;
    private String swf_url;
    private String ranking;
    private String defaultRankName;
    private JsonNode seo_info;
    private String isWeekListFristThree;
    private List<String> hotCate;
    private List<String> leftNav;
    private Column column;
    private JsonNode child_cate;
    private JsonNode qqgroup;
    private JsonNode post_list;
    private List<String> h5_guardJS;
    private int h5_default;
    private Room_args room_args;
    private String bind_vodCateUrl;
    private List<Integer> yzpk_cate2_id_list;
    private boolean var_is_yz;
    private String var_yz_pk_name;
    private int barrage_praise;
    private JsonNode serviceSwitch;
    private int player_barrage;
    private JsonNode staticWebconfHash;
    private int barrage_timeout_downgrade;

}