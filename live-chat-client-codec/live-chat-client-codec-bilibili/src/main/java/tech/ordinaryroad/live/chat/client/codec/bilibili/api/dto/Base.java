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
public class Base {

    private String name;
    private String face;
    private int name_color;
    private boolean is_mystery;
    private Risk_ctrl_info risk_ctrl_info;
    private Origin_info origin_info;
    private Official_info official_info;
    private String name_color_str;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setFace(String face) {
         this.face = face;
     }
     public String getFace() {
         return face;
     }

    public void setName_color(int name_color) {
         this.name_color = name_color;
     }
     public int getName_color() {
         return name_color;
     }

    public void setIs_mystery(boolean is_mystery) {
         this.is_mystery = is_mystery;
     }
     public boolean getIs_mystery() {
         return is_mystery;
     }

    public void setRisk_ctrl_info(Risk_ctrl_info risk_ctrl_info) {
         this.risk_ctrl_info = risk_ctrl_info;
     }
     public Risk_ctrl_info getRisk_ctrl_info() {
         return risk_ctrl_info;
     }

    public void setOrigin_info(Origin_info origin_info) {
         this.origin_info = origin_info;
     }
     public Origin_info getOrigin_info() {
         return origin_info;
     }

    public void setOfficial_info(Official_info official_info) {
         this.official_info = official_info;
     }
     public Official_info getOfficial_info() {
         return official_info;
     }

    public void setName_color_str(String name_color_str) {
         this.name_color_str = name_color_str;
     }
     public String getName_color_str() {
         return name_color_str;
     }

}