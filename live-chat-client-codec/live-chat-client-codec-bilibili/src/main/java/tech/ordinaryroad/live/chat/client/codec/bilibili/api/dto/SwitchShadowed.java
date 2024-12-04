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
public class SwitchShadowed {

    private String text;
    private String switchShadowed;
    private Ui_type ui_type;
    private String comment;
    public void setText(String text) {
         this.text = text;
     }
     public String getText() {
         return text;
     }

    public void setSwitchShadowed(String switchShadowed) {
         this.switchShadowed = switchShadowed;
     }
     public String getSwitchShadowed() {
         return switchShadowed;
     }

    public void setUi_type(Ui_type ui_type) {
         this.ui_type = ui_type;
     }
     public Ui_type getUi_type() {
         return ui_type;
     }

    public void setComment(String comment) {
         this.comment = comment;
     }
     public String getComment() {
         return comment;
     }

}