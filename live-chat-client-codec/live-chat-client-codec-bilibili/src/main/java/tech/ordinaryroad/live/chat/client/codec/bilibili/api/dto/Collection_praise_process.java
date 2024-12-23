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
public class Collection_praise_process {

    private int id;
    private int uid;
    private int target_praise;
    private int current_praise;
    private int start_time;
    private int end_time;
    private String benefit;
    private boolean isSuccess;
    private boolean exist;
    private int audit_status;
    private String jump_url;
    private String current_praise_text;
    private String icon_url;
    private String live_id;
    public void setId(int id) {
         this.id = id;
     }
     public int getId() {
         return id;
     }

    public void setUid(int uid) {
         this.uid = uid;
     }
     public int getUid() {
         return uid;
     }

    public void setTarget_praise(int target_praise) {
         this.target_praise = target_praise;
     }
     public int getTarget_praise() {
         return target_praise;
     }

    public void setCurrent_praise(int current_praise) {
         this.current_praise = current_praise;
     }
     public int getCurrent_praise() {
         return current_praise;
     }

    public void setStart_time(int start_time) {
         this.start_time = start_time;
     }
     public int getStart_time() {
         return start_time;
     }

    public void setEnd_time(int end_time) {
         this.end_time = end_time;
     }
     public int getEnd_time() {
         return end_time;
     }

    public void setBenefit(String benefit) {
         this.benefit = benefit;
     }
     public String getBenefit() {
         return benefit;
     }

    public void setIsSuccess(boolean isSuccess) {
         this.isSuccess = isSuccess;
     }
     public boolean getIsSuccess() {
         return isSuccess;
     }

    public void setExist(boolean exist) {
         this.exist = exist;
     }
     public boolean getExist() {
         return exist;
     }

    public void setAudit_status(int audit_status) {
         this.audit_status = audit_status;
     }
     public int getAudit_status() {
         return audit_status;
     }

    public void setJump_url(String jump_url) {
         this.jump_url = jump_url;
     }
     public String getJump_url() {
         return jump_url;
     }

    public void setCurrent_praise_text(String current_praise_text) {
         this.current_praise_text = current_praise_text;
     }
     public String getCurrent_praise_text() {
         return current_praise_text;
     }

    public void setIcon_url(String icon_url) {
         this.icon_url = icon_url;
     }
     public String getIcon_url() {
         return icon_url;
     }

    public void setLive_id(String live_id) {
         this.live_id = live_id;
     }
     public String getLive_id() {
         return live_id;
     }

}