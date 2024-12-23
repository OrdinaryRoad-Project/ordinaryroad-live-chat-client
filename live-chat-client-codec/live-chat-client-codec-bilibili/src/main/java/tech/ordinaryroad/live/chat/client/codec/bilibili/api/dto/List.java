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
public class List {

    private long uid;
    private String face;
    private String uname;
    private String score;
    private int rank;
    private int guard_level;
    private int wealth_level;
    private boolean is_mystery;
    private Uinfo uinfo;
    public void setUid(long uid) {
         this.uid = uid;
     }
     public long getUid() {
         return uid;
     }

    public void setFace(String face) {
         this.face = face;
     }
     public String getFace() {
         return face;
     }

    public void setUname(String uname) {
         this.uname = uname;
     }
     public String getUname() {
         return uname;
     }

    public void setScore(String score) {
         this.score = score;
     }
     public String getScore() {
         return score;
     }

    public void setRank(int rank) {
         this.rank = rank;
     }
     public int getRank() {
         return rank;
     }

    public void setGuard_level(int guard_level) {
         this.guard_level = guard_level;
     }
     public int getGuard_level() {
         return guard_level;
     }

    public void setWealth_level(int wealth_level) {
         this.wealth_level = wealth_level;
     }
     public int getWealth_level() {
         return wealth_level;
     }

    public void setIs_mystery(boolean is_mystery) {
         this.is_mystery = is_mystery;
     }
     public boolean getIs_mystery() {
         return is_mystery;
     }

    public void setUinfo(Uinfo uinfo) {
         this.uinfo = uinfo;
     }
     public Uinfo getUinfo() {
         return uinfo;
     }

}