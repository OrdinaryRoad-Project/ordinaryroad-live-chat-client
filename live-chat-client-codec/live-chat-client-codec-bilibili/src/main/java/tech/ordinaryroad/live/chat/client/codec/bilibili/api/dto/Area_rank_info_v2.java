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
public class Area_rank_info_v2 {

    private String items;
    private int rotation_cycle_time;
    private User_last_rank_result user_last_rank_result;
    private boolean user_ab_flag;
    public void setItems(String items) {
         this.items = items;
     }
     public String getItems() {
         return items;
     }

    public void setRotation_cycle_time(int rotation_cycle_time) {
         this.rotation_cycle_time = rotation_cycle_time;
     }
     public int getRotation_cycle_time() {
         return rotation_cycle_time;
     }

    public void setUser_last_rank_result(User_last_rank_result user_last_rank_result) {
         this.user_last_rank_result = user_last_rank_result;
     }
     public User_last_rank_result getUser_last_rank_result() {
         return user_last_rank_result;
     }

    public void setUser_ab_flag(boolean user_ab_flag) {
         this.user_ab_flag = user_ab_flag;
     }
     public boolean getUser_ab_flag() {
         return user_ab_flag;
     }

}