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
public class Status {

    private int open;
    private int anchor_open;
    private int status;
    private int uid;
    private String user_name;
    private String head_pic;
    private int guard;
    private int start_at;
    private long current_time;
    public void setOpen(int open) {
         this.open = open;
     }
     public int getOpen() {
         return open;
     }

    public void setAnchor_open(int anchor_open) {
         this.anchor_open = anchor_open;
     }
     public int getAnchor_open() {
         return anchor_open;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setUid(int uid) {
         this.uid = uid;
     }
     public int getUid() {
         return uid;
     }

    public void setUser_name(String user_name) {
         this.user_name = user_name;
     }
     public String getUser_name() {
         return user_name;
     }

    public void setHead_pic(String head_pic) {
         this.head_pic = head_pic;
     }
     public String getHead_pic() {
         return head_pic;
     }

    public void setGuard(int guard) {
         this.guard = guard;
     }
     public int getGuard() {
         return guard;
     }

    public void setStart_at(int start_at) {
         this.start_at = start_at;
     }
     public int getStart_at() {
         return start_at;
     }

    public void setCurrent_time(long current_time) {
         this.current_time = current_time;
     }
     public long getCurrent_time() {
         return current_time;
     }

}