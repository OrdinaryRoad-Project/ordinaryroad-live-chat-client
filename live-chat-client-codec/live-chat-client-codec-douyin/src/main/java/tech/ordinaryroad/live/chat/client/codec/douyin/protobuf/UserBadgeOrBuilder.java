// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: douyin.proto

// Protobuf Java Version: 3.25.5
package tech.ordinaryroad.live.chat.client.codec.douyin.protobuf;

public interface UserBadgeOrBuilder extends
    // @@protoc_insertion_point(interface_extends:UserBadge)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>map&lt;int32, .Image&gt; icons = 1;</code>
   */
  int getIconsCount();
  /**
   * <code>map&lt;int32, .Image&gt; icons = 1;</code>
   */
  boolean containsIcons(
      int key);
  /**
   * Use {@link #getIconsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.Integer, tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image>
  getIcons();
  /**
   * <code>map&lt;int32, .Image&gt; icons = 1;</code>
   */
  java.util.Map<java.lang.Integer, tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image>
  getIconsMap();
  /**
   * <code>map&lt;int32, .Image&gt; icons = 1;</code>
   */
  /* nullable */
tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getIconsOrDefault(
      int key,
      /* nullable */
tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image defaultValue);
  /**
   * <code>map&lt;int32, .Image&gt; icons = 1;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getIconsOrThrow(
      int key);

  /**
   * <code>string title = 2;</code>
   * @return The title.
   */
  java.lang.String getTitle();
  /**
   * <code>string title = 2;</code>
   * @return The bytes for title.
   */
  com.google.protobuf.ByteString
      getTitleBytes();
}