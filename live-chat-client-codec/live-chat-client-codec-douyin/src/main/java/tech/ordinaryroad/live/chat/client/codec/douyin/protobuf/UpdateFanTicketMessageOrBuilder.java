// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: douyin.proto

// Protobuf Java Version: 3.25.5
package tech.ordinaryroad.live.chat.client.codec.douyin.protobuf;

public interface UpdateFanTicketMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:UpdateFanTicketMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.Common common = 1;</code>
   * @return Whether the common field is set.
   */
  boolean hasCommon();
  /**
   * <code>.Common common = 1;</code>
   * @return The common.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Common getCommon();
  /**
   * <code>.Common common = 1;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommonOrBuilder getCommonOrBuilder();

  /**
   * <code>string roomFanTicketCountText = 2;</code>
   * @return The roomFanTicketCountText.
   */
  java.lang.String getRoomFanTicketCountText();
  /**
   * <code>string roomFanTicketCountText = 2;</code>
   * @return The bytes for roomFanTicketCountText.
   */
  com.google.protobuf.ByteString
      getRoomFanTicketCountTextBytes();

  /**
   * <code>uint64 roomFanTicketCount = 3;</code>
   * @return The roomFanTicketCount.
   */
  long getRoomFanTicketCount();

  /**
   * <code>bool forceUpdate = 4;</code>
   * @return The forceUpdate.
   */
  boolean getForceUpdate();
}
