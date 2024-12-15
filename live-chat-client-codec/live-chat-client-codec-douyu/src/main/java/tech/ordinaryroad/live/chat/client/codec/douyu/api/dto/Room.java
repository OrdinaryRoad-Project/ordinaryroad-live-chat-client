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
package tech.ordinaryroad.live.chat.client.codec.douyu.api.dto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    private String room_src;
    private boolean chat_age_limit;
    private String cate1_id;
    private boolean chat_group;
    private String nickname;
    private long show_time;
    private boolean cq;
    private boolean chat_cd_factor;
    private boolean pwd;
    private String show_details;
    private String cate2_id;
    private boolean chat_level;
    private String room_name;
    private String is_diy;
    private String cate3_id;
    private String wmt;
    private long owner_uid;
    private JsonNode avatar;
    private boolean live_url;
    private String end_time;
    private long room_id;
    private int show_status;
    private long show_id;
    private String rs1;
    private int rst;
    private String status;
    private String is_multibit;
    private int cate_id;
    private String child_id;
    private int is_vr;
    private String tags;
    private String bgimg_src;
    private String icon_id;
    private String icon_start_time;
    private String icon_end_time;
    private String ver;
    private String e_url;
    private String fans_bn;
    private String did;
    private String client_sys;
    private String close_notice;
    private String close_notice_ctime;
    private String close_notice_always;
    private String live_client_type;
    private String avatar_mid;
    private String avatar_small;
    private int iol;
    private String cityname;
    private int isvertival;
    private int videoLoop;
    private JsonNode share;
    private JsonNode speakSet;
    private JsonNode detailsData;
    private String room_pic;
    private String owner_name;
    private String room_url;
    private int isVertical;
    private int is_video_high_quality_time;
    private String video_high_quality_resolution;
    private String video_high_quality_num;
    private String can_send_gift;
    private String yuba_jump_url;
    private int room_label_right_flag;
    private int is_set_fans_badge;
    private List<String> eticket;
    private List<String> effectInfo;
    private JsonNode wab;
    private String category_id;
    private JsonNode giftActivity;
    private int isNzRoom;
    private List<String> cfmGiftList;
    private int isPubgmRoom;
    private List<String> rankActivity;
    private long nowtime;
    private JsonNode nobleConfig;
    private String second_lvl_name;
    private JsonNode stsign_room;
    private List<String> emperorPush;
    private JsonNode levelInfo;
    private String is_show_rank_list;
    private int giftTempId;
    private JsonNode music;
    private String up_id;
    private int ban_display;
    private int vipId;
    private JsonNode multirates;
    private int is_password;
    private JsonNode p2p_setting;
    private int is_high_game;
    private int open_full_screen;
    private JsonNode cate_limit;
    private String defaultSrc;
    private String coverSrc;
    private String owner_avatar;
    private int isDefaultAvatar;
    private JsonNode room_idle;
    private List<String> wsproxy;
    private List<String> h5wsproxy;
    private String videop;
    private String room_plugin;
    private JsonNode officialAnchor;
    private JsonNode authInfo;
    private String authVersion;
    private List<String> officialCerts;
    private int isVip;
    private JsonNode room_biz_all;
    private int st;
    private JsonNode simplifyBulletScreen;

}