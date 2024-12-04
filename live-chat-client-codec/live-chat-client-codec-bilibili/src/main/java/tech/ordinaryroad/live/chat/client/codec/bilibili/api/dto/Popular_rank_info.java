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
public class Popular_rank_info {

    private int rank;
    private int countdown;
    private long timestamp;
    private String url;
    private String on_rank_name;
    private String rank_name;
    private int rank_by_type;
    private String rank_name_by_type;
    private String on_rank_name_by_type;
    private String url_by_type;
    private String default_url;
    public void setRank(int rank) {
         this.rank = rank;
     }
     public int getRank() {
         return rank;
     }

    public void setCountdown(int countdown) {
         this.countdown = countdown;
     }
     public int getCountdown() {
         return countdown;
     }

    public void setTimestamp(long timestamp) {
         this.timestamp = timestamp;
     }
     public long getTimestamp() {
         return timestamp;
     }

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

    public void setOn_rank_name(String on_rank_name) {
         this.on_rank_name = on_rank_name;
     }
     public String getOn_rank_name() {
         return on_rank_name;
     }

    public void setRank_name(String rank_name) {
         this.rank_name = rank_name;
     }
     public String getRank_name() {
         return rank_name;
     }

    public void setRank_by_type(int rank_by_type) {
         this.rank_by_type = rank_by_type;
     }
     public int getRank_by_type() {
         return rank_by_type;
     }

    public void setRank_name_by_type(String rank_name_by_type) {
         this.rank_name_by_type = rank_name_by_type;
     }
     public String getRank_name_by_type() {
         return rank_name_by_type;
     }

    public void setOn_rank_name_by_type(String on_rank_name_by_type) {
         this.on_rank_name_by_type = on_rank_name_by_type;
     }
     public String getOn_rank_name_by_type() {
         return on_rank_name_by_type;
     }

    public void setUrl_by_type(String url_by_type) {
         this.url_by_type = url_by_type;
     }
     public String getUrl_by_type() {
         return url_by_type;
     }

    public void setDefault_url(String default_url) {
         this.default_url = default_url;
     }
     public String getDefault_url() {
         return default_url;
     }

}