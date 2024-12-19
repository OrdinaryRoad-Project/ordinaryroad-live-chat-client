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
public class Live_play_info {

    private boolean show_widget_banner;
    private boolean show_left_entry;
    private int widget_version;
    public void setShow_widget_banner(boolean show_widget_banner) {
         this.show_widget_banner = show_widget_banner;
     }
     public boolean getShow_widget_banner() {
         return show_widget_banner;
     }

    public void setShow_left_entry(boolean show_left_entry) {
         this.show_left_entry = show_left_entry;
     }
     public boolean getShow_left_entry() {
         return show_left_entry;
     }

    public void setWidget_version(int widget_version) {
         this.widget_version = widget_version;
     }
     public int getWidget_version() {
         return widget_version;
     }

}