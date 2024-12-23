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
public class Activity_init_info {

    private List<String> eventList;
    private WeekInfo weekInfo;
    private String giftName;
    private Lego lego;
    public void setEventList(List<String> eventList) {
         this.eventList = eventList;
     }
     public List<String> getEventList() {
         return eventList;
     }

    public void setWeekInfo(WeekInfo weekInfo) {
         this.weekInfo = weekInfo;
     }
     public WeekInfo getWeekInfo() {
         return weekInfo;
     }

    public void setGiftName(String giftName) {
         this.giftName = giftName;
     }
     public String getGiftName() {
         return giftName;
     }

    public void setLego(Lego lego) {
         this.lego = lego;
     }
     public Lego getLego() {
         return lego;
     }

}