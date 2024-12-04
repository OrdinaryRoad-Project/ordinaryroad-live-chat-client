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
package tech.ordinaryroad.live.chat.client.codec.bilibili.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room_info {

    private long uid;
    private long room_id;
    private int short_id;
    private String title;
    private String cover;
    private String tags;
    private String background;
    private String description;
    private int live_status;
    private long live_start_time;
    private int live_screen_type;
    private int lock_status;
    private int lock_time;
    private int hidden_status;
    private int hidden_time;
    private int area_id;
    private String area_name;
    private int parent_area_id;
    private String parent_area_name;
    private String keyframe;
    private int special_type;
    private String up_session;
    private int pk_status;
    private boolean is_studio;
    private Pendants pendants;
    private int on_voice_join;
    private long online;
    private Map<String, Integer> room_type;
    private String sub_session_key;
    private long live_id;
    private String live_id_str;
    private int official_room_id;
    private String official_room_info;
    private String voice_background;

}