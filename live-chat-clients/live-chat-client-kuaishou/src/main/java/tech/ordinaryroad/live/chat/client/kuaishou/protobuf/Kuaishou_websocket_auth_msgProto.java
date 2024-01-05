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
// source: kuaishou_websocket_auth_msg.proto

package tech.ordinaryroad.live.chat.client.kuaishou.protobuf;

public final class Kuaishou_websocket_auth_msgProto {
  private Kuaishou_websocket_auth_msgProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_tech_ordinaryroad_live_chat_client_kuaishou_protobuf_kuaishou_websocket_auth_msg_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_tech_ordinaryroad_live_chat_client_kuaishou_protobuf_kuaishou_websocket_auth_msg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n!kuaishou_websocket_auth_msg.proto\0224tec" +
      "h.ordinaryroad.live.chat.client.kuaishou" +
      ".protobuf\"T\n\033kuaishou_websocket_auth_msg" +
      "\022\r\n\005token\030\001 \001(\t\022\024\n\014liveStreamId\030\002 \001(\t\022\020\n" +
      "\010string_3\030\003 \001(\tB`\n4tech.ordinaryroad.liv" +
      "e.chat.client.kuaishou.protobufB Kuaisho" +
      "u_websocket_auth_msgProtoP\001\242\002\003GPBb\006proto" +
      "3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_tech_ordinaryroad_live_chat_client_kuaishou_protobuf_kuaishou_websocket_auth_msg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_tech_ordinaryroad_live_chat_client_kuaishou_protobuf_kuaishou_websocket_auth_msg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_tech_ordinaryroad_live_chat_client_kuaishou_protobuf_kuaishou_websocket_auth_msg_descriptor,
        new java.lang.String[] { "Token", "LiveStreamId", "String3", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}