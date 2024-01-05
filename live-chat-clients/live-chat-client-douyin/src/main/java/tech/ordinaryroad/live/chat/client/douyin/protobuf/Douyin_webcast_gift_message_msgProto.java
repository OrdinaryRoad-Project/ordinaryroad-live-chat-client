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

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: douyin_webcast_gift_message_msg.proto

package tech.ordinaryroad.live.chat.client.douyin.protobuf;

public final class Douyin_webcast_gift_message_msgProto {
  private Douyin_webcast_gift_message_msgProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tech_ordinaryroad_live_chat_client_douyin_protobuf_douyin_webcast_gift_message_msg_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tech_ordinaryroad_live_chat_client_douyin_protobuf_douyin_webcast_gift_message_msg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n%douyin_webcast_gift_message_msg.proto\022" +
      "2tech.ordinaryroad.live.chat.client.douy" +
      "in.protobuf\032\014Common.proto\032\nUser.proto\032\020T" +
      "extEffect.proto\032\nText.proto\032\024GiftIMPrior" +
      "ity.proto\032\020GiftStruct.proto\032\026PublicAreaC" +
      "ommon.proto\"\272\006\n\037douyin_webcast_gift_mess" +
      "age_msg\022\027\n\006common\030\001 \001(\0132\007.Common\022\017\n\007gift" +
      "_id\030\002 \001(\004\022\030\n\020fan_ticket_count\030\003 \001(\004\022\023\n\013g" +
      "roup_count\030\004 \001(\004\022\024\n\014repeat_count\030\005 \001(\004\022\023" +
      "\n\013combo_count\030\006 \001(\004\022\023\n\004user\030\007 \001(\0132\005.User" +
      "\022\026\n\007to_user\030\010 \001(\0132\005.User\022\022\n\nrepeat_end\030\t" +
      " \001(\r\022 \n\013text_effect\030\n \001(\0132\013.TextEffect\022\020" +
      "\n\010group_id\030\013 \001(\004\022\030\n\020income_taskgifts\030\014 \001" +
      "(\004\022\035\n\025room_fan_ticket_count\030\r \001(\004\022!\n\010pri" +
      "ority\030\016 \001(\0132\017.GiftIMPriority\022\031\n\004gift\030\017 \001" +
      "(\0132\013.GiftStruct\022\016\n\006log_id\030\020 \001(\t\022\021\n\tsend_" +
      "type\030\021 \001(\004\022-\n\022public_area_common\030\022 \001(\0132\021" +
      ".PublicAreaCommon\022 \n\021tray_display_text\030\023" +
      " \001(\0132\005.Text\022\036\n\026banned_display_effects\030\024 " +
      "\001(\004\022\030\n\020display_for_self\030\031 \001(\010\022\032\n\022interac" +
      "t_gift_info\030\032 \001(\t\022\025\n\rdiy_item_info\030\033 \001(\t" +
      "\022\032\n\022min_asset_set_list\030\034 \003(\004\022\023\n\013total_co" +
      "unt\030\035 \001(\004\022\032\n\022client_gift_source\030\036 \001(\r\022\030\n" +
      "\020to_user_ids_list\030  \003(\004\022\022\n\nsend_timet\030! " +
      "\001(\004\022\036\n\026force_display_effectst\030\" \001(\004\022\020\n\010t" +
      "race_id\030# \001(\t\022\031\n\021effect_display_ts\030$ \001(\004" +
      "Bb\n2tech.ordinaryroad.live.chat.client.d" +
      "ouyin.protobufB$Douyin_webcast_gift_mess" +
      "age_msgProtoP\001\242\002\003GPBb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.CommonOuterClass.getDescriptor(),
          tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.UserOuterClass.getDescriptor(),
          tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.TextEffectOuterClass.getDescriptor(),
          tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.TextOuterClass.getDescriptor(),
          tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.GiftIMPriorityOuterClass.getDescriptor(),
          tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.GiftStructOuterClass.getDescriptor(),
          tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.PublicAreaCommonOuterClass.getDescriptor(),
        });
    internal_static_tech_ordinaryroad_live_chat_client_douyin_protobuf_douyin_webcast_gift_message_msg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_tech_ordinaryroad_live_chat_client_douyin_protobuf_douyin_webcast_gift_message_msg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tech_ordinaryroad_live_chat_client_douyin_protobuf_douyin_webcast_gift_message_msg_descriptor,
        new java.lang.String[] { "Common", "GiftId", "FanTicketCount", "GroupCount", "RepeatCount", "ComboCount", "User", "ToUser", "RepeatEnd", "TextEffect", "GroupId", "IncomeTaskgifts", "RoomFanTicketCount", "Priority", "Gift", "LogId", "SendType", "PublicAreaCommon", "TrayDisplayText", "BannedDisplayEffects", "DisplayForSelf", "InteractGiftInfo", "DiyItemInfo", "MinAssetSetList", "TotalCount", "ClientGiftSource", "ToUserIdsList", "SendTimet", "ForceDisplayEffectst", "TraceId", "EffectDisplayTs", });
    tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.CommonOuterClass.getDescriptor();
    tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.UserOuterClass.getDescriptor();
    tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.TextEffectOuterClass.getDescriptor();
    tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.TextOuterClass.getDescriptor();
    tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.GiftIMPriorityOuterClass.getDescriptor();
    tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.GiftStructOuterClass.getDescriptor();
    tech.ordinaryroad.live.chat.client.douyin.protobuf.dto.PublicAreaCommonOuterClass.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}