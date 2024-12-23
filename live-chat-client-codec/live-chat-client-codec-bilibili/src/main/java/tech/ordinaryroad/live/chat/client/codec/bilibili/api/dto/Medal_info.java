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

/**
 * Auto-generated: 2024-12-04 23:10:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Medal_info {

    private int guard_level;
    private long medal_color_start;
    private long medal_color_end;
    private long medal_color_border;
    private String medal_name;
    private int level;
    private long target_id;
    private int is_light;
    public void setGuard_level(int guard_level) {
         this.guard_level = guard_level;
     }
     public int getGuard_level() {
         return guard_level;
     }

    public void setMedal_color_start(long medal_color_start) {
         this.medal_color_start = medal_color_start;
     }
     public long getMedal_color_start() {
         return medal_color_start;
     }

    public void setMedal_color_end(long medal_color_end) {
         this.medal_color_end = medal_color_end;
     }
     public long getMedal_color_end() {
         return medal_color_end;
     }

    public void setMedal_color_border(long medal_color_border) {
         this.medal_color_border = medal_color_border;
     }
     public long getMedal_color_border() {
         return medal_color_border;
     }

    public void setMedal_name(String medal_name) {
         this.medal_name = medal_name;
     }
     public String getMedal_name() {
         return medal_name;
     }

    public void setLevel(int level) {
         this.level = level;
     }
     public int getLevel() {
         return level;
     }

    public void setTarget_id(long target_id) {
         this.target_id = target_id;
     }
     public long getTarget_id() {
         return target_id;
     }

    public void setIs_light(int is_light) {
         this.is_light = is_light;
     }
     public int getIs_light() {
         return is_light;
     }

}