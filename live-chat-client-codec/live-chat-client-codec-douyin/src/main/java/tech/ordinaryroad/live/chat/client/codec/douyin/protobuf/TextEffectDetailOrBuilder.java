// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: douyin.proto

// Protobuf Java Version: 3.25.5
package tech.ordinaryroad.live.chat.client.codec.douyin.protobuf;

public interface TextEffectDetailOrBuilder extends
    // @@protoc_insertion_point(interface_extends:TextEffectDetail)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.Text text = 1;</code>
   * @return Whether the text field is set.
   */
  boolean hasText();
  /**
   * <code>.Text text = 1;</code>
   * @return The text.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Text getText();
  /**
   * <code>.Text text = 1;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextOrBuilder getTextOrBuilder();

  /**
   * <code>uint32 textFontSize = 2;</code>
   * @return The textFontSize.
   */
  int getTextFontSize();

  /**
   * <code>.Image background = 3;</code>
   * @return Whether the background field is set.
   */
  boolean hasBackground();
  /**
   * <code>.Image background = 3;</code>
   * @return The background.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getBackground();
  /**
   * <code>.Image background = 3;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getBackgroundOrBuilder();

  /**
   * <code>uint32 start = 4;</code>
   * @return The start.
   */
  int getStart();

  /**
   * <code>uint32 duration = 5;</code>
   * @return The duration.
   */
  int getDuration();

  /**
   * <code>uint32 x = 6;</code>
   * @return The x.
   */
  int getX();

  /**
   * <code>uint32 y = 7;</code>
   * @return The y.
   */
  int getY();

  /**
   * <code>uint32 width = 8;</code>
   * @return The width.
   */
  int getWidth();

  /**
   * <code>uint32 height = 9;</code>
   * @return The height.
   */
  int getHeight();

  /**
   * <code>uint32 shadowDx = 10;</code>
   * @return The shadowDx.
   */
  int getShadowDx();

  /**
   * <code>uint32 shadowDy = 11;</code>
   * @return The shadowDy.
   */
  int getShadowDy();

  /**
   * <code>uint32 shadowRadius = 12;</code>
   * @return The shadowRadius.
   */
  int getShadowRadius();

  /**
   * <code>string shadowColor = 13;</code>
   * @return The shadowColor.
   */
  java.lang.String getShadowColor();
  /**
   * <code>string shadowColor = 13;</code>
   * @return The bytes for shadowColor.
   */
  com.google.protobuf.ByteString
      getShadowColorBytes();

  /**
   * <code>string strokeColor = 14;</code>
   * @return The strokeColor.
   */
  java.lang.String getStrokeColor();
  /**
   * <code>string strokeColor = 14;</code>
   * @return The bytes for strokeColor.
   */
  com.google.protobuf.ByteString
      getStrokeColorBytes();

  /**
   * <code>uint32 strokeWidth = 15;</code>
   * @return The strokeWidth.
   */
  int getStrokeWidth();
}