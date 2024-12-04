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
import java.util.List;

/**
 * Auto-generated: 2024-12-04 23:10:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Like_info_v3 {

    private int total_likes;
    private boolean click_block;
    private boolean count_block;
    private String guild_emo_text;
    private String guild_dm_text;
    private String like_dm_text;
    private List<String> hand_icons;
    private List<String> dm_icons;
    private String eggshells_icon;
    private int count_show_time;
    private String process_icon;
    private String process_color;
    private int report_click_limit;
    private int report_time_min;
    private int report_time_max;
    private String icon;
    private double cooldown;
    private boolean hand_use_face;
    private List<String> guide_icon_urls;
    private double guide_icon_ratio;
    public void setTotal_likes(int total_likes) {
         this.total_likes = total_likes;
     }
     public int getTotal_likes() {
         return total_likes;
     }

    public void setClick_block(boolean click_block) {
         this.click_block = click_block;
     }
     public boolean getClick_block() {
         return click_block;
     }

    public void setCount_block(boolean count_block) {
         this.count_block = count_block;
     }
     public boolean getCount_block() {
         return count_block;
     }

    public void setGuild_emo_text(String guild_emo_text) {
         this.guild_emo_text = guild_emo_text;
     }
     public String getGuild_emo_text() {
         return guild_emo_text;
     }

    public void setGuild_dm_text(String guild_dm_text) {
         this.guild_dm_text = guild_dm_text;
     }
     public String getGuild_dm_text() {
         return guild_dm_text;
     }

    public void setLike_dm_text(String like_dm_text) {
         this.like_dm_text = like_dm_text;
     }
     public String getLike_dm_text() {
         return like_dm_text;
     }

    public void setHand_icons(List<String> hand_icons) {
         this.hand_icons = hand_icons;
     }
     public List<String> getHand_icons() {
         return hand_icons;
     }

    public void setDm_icons(List<String> dm_icons) {
         this.dm_icons = dm_icons;
     }
     public List<String> getDm_icons() {
         return dm_icons;
     }

    public void setEggshells_icon(String eggshells_icon) {
         this.eggshells_icon = eggshells_icon;
     }
     public String getEggshells_icon() {
         return eggshells_icon;
     }

    public void setCount_show_time(int count_show_time) {
         this.count_show_time = count_show_time;
     }
     public int getCount_show_time() {
         return count_show_time;
     }

    public void setProcess_icon(String process_icon) {
         this.process_icon = process_icon;
     }
     public String getProcess_icon() {
         return process_icon;
     }

    public void setProcess_color(String process_color) {
         this.process_color = process_color;
     }
     public String getProcess_color() {
         return process_color;
     }

    public void setReport_click_limit(int report_click_limit) {
         this.report_click_limit = report_click_limit;
     }
     public int getReport_click_limit() {
         return report_click_limit;
     }

    public void setReport_time_min(int report_time_min) {
         this.report_time_min = report_time_min;
     }
     public int getReport_time_min() {
         return report_time_min;
     }

    public void setReport_time_max(int report_time_max) {
         this.report_time_max = report_time_max;
     }
     public int getReport_time_max() {
         return report_time_max;
     }

    public void setIcon(String icon) {
         this.icon = icon;
     }
     public String getIcon() {
         return icon;
     }

    public void setCooldown(double cooldown) {
         this.cooldown = cooldown;
     }
     public double getCooldown() {
         return cooldown;
     }

    public void setHand_use_face(boolean hand_use_face) {
         this.hand_use_face = hand_use_face;
     }
     public boolean getHand_use_face() {
         return hand_use_face;
     }

    public void setGuide_icon_urls(List<String> guide_icon_urls) {
         this.guide_icon_urls = guide_icon_urls;
     }
     public List<String> getGuide_icon_urls() {
         return guide_icon_urls;
     }

    public void setGuide_icon_ratio(double guide_icon_ratio) {
         this.guide_icon_ratio = guide_icon_ratio;
     }
     public double getGuide_icon_ratio() {
         return guide_icon_ratio;
     }

}