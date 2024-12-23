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
public class Player_throttle_info {

    private int status;
    private int normal_sleep_time;
    private int fullscreen_sleep_time;
    private int tab_sleep_time;
    private int prompt_time;
    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setNormal_sleep_time(int normal_sleep_time) {
         this.normal_sleep_time = normal_sleep_time;
     }
     public int getNormal_sleep_time() {
         return normal_sleep_time;
     }

    public void setFullscreen_sleep_time(int fullscreen_sleep_time) {
         this.fullscreen_sleep_time = fullscreen_sleep_time;
     }
     public int getFullscreen_sleep_time() {
         return fullscreen_sleep_time;
     }

    public void setTab_sleep_time(int tab_sleep_time) {
         this.tab_sleep_time = tab_sleep_time;
     }
     public int getTab_sleep_time() {
         return tab_sleep_time;
     }

    public void setPrompt_time(int prompt_time) {
         this.prompt_time = prompt_time;
     }
     public int getPrompt_time() {
         return prompt_time;
     }

}