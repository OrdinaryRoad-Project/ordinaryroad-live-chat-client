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
public class Tab {

    private String type;
    private String title;
    private int status;
    private int defaultShadowed;
    private String comment;
    private String desc_url;
    private String switchShadowed;
    private List<Sub_tab> sub_tab;
    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setDefaultShadowed(int defaultShadowed) {
         this.defaultShadowed = defaultShadowed;
     }
     public int getDefaultShadowed() {
         return defaultShadowed;
     }

    public void setComment(String comment) {
         this.comment = comment;
     }
     public String getComment() {
         return comment;
     }

    public void setDesc_url(String desc_url) {
         this.desc_url = desc_url;
     }
     public String getDesc_url() {
         return desc_url;
     }

    public void setSwitchShadowed(String switchShadowed) {
         this.switchShadowed = switchShadowed;
     }
     public String getSwitchShadowed() {
         return switchShadowed;
     }

    public void setSub_tab(List<Sub_tab> sub_tab) {
         this.sub_tab = sub_tab;
     }
     public List<Sub_tab> getSub_tab() {
         return sub_tab;
     }

}