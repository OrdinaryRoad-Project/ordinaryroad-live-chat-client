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
public class Material_list {

    private long activity_id;
    private String related_id;
    private Mod_resource mod_resource;
    private String backup_url;
    public void setActivity_id(long activity_id) {
         this.activity_id = activity_id;
     }
     public long getActivity_id() {
         return activity_id;
     }

    public void setRelated_id(String related_id) {
         this.related_id = related_id;
     }
     public String getRelated_id() {
         return related_id;
     }

    public void setMod_resource(Mod_resource mod_resource) {
         this.mod_resource = mod_resource;
     }
     public Mod_resource getMod_resource() {
         return mod_resource;
     }

    public void setBackup_url(String backup_url) {
         this.backup_url = backup_url;
     }
     public String getBackup_url() {
         return backup_url;
     }

}