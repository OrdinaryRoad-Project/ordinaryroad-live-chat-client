// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: douyin.proto

// Protobuf Java Version: 3.25.5
package tech.ordinaryroad.live.chat.client.codec.douyin.protobuf;

public interface TextPieceOrBuilder extends
    // @@protoc_insertion_point(interface_extends:TextPiece)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint32 type = 1;</code>
   * @return The type.
   */
  int getType();

  /**
   * <code>.TextFormat format = 2;</code>
   * @return Whether the format field is set.
   */
  boolean hasFormat();
  /**
   * <code>.TextFormat format = 2;</code>
   * @return The format.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextFormat getFormat();
  /**
   * <code>.TextFormat format = 2;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextFormatOrBuilder getFormatOrBuilder();

  /**
   * <code>string value_ref = 3;</code>
   * @return The valueRef.
   */
  java.lang.String getValueRef();
  /**
   * <code>string value_ref = 3;</code>
   * @return The bytes for valueRef.
   */
  com.google.protobuf.ByteString
      getValueRefBytes();

  /**
   * <code>string string_value = 11;</code>
   * @return The stringValue.
   */
  java.lang.String getStringValue();
  /**
   * <code>string string_value = 11;</code>
   * @return The bytes for stringValue.
   */
  com.google.protobuf.ByteString
      getStringValueBytes();

  /**
   * <code>.TextPieceUser uservalue = 21;</code>
   * @return Whether the uservalue field is set.
   */
  boolean hasUservalue();
  /**
   * <code>.TextPieceUser uservalue = 21;</code>
   * @return The uservalue.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPieceUser getUservalue();
  /**
   * <code>.TextPieceUser uservalue = 21;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPieceUserOrBuilder getUservalueOrBuilder();

  /**
   * <code>.TextPieceGift giftvalue = 22;</code>
   * @return Whether the giftvalue field is set.
   */
  boolean hasGiftvalue();
  /**
   * <code>.TextPieceGift giftvalue = 22;</code>
   * @return The giftvalue.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPieceGift getGiftvalue();
  /**
   * <code>.TextPieceGift giftvalue = 22;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPieceGiftOrBuilder getGiftvalueOrBuilder();

  /**
   * <code>.TextPieceHeart heartvalue = 23;</code>
   * @return Whether the heartvalue field is set.
   */
  boolean hasHeartvalue();
  /**
   * <code>.TextPieceHeart heartvalue = 23;</code>
   * @return The heartvalue.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPieceHeart getHeartvalue();
  /**
   * <code>.TextPieceHeart heartvalue = 23;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPieceHeartOrBuilder getHeartvalueOrBuilder();

  /**
   * <code>.TextPiecePatternRef patternrefvalue = 24;</code>
   * @return Whether the patternrefvalue field is set.
   */
  boolean hasPatternrefvalue();
  /**
   * <code>.TextPiecePatternRef patternrefvalue = 24;</code>
   * @return The patternrefvalue.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPiecePatternRef getPatternrefvalue();
  /**
   * <code>.TextPiecePatternRef patternrefvalue = 24;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPiecePatternRefOrBuilder getPatternrefvalueOrBuilder();

  /**
   * <code>.TextPieceImage imagevalue = 25;</code>
   * @return Whether the imagevalue field is set.
   */
  boolean hasImagevalue();
  /**
   * <code>.TextPieceImage imagevalue = 25;</code>
   * @return The imagevalue.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPieceImage getImagevalue();
  /**
   * <code>.TextPieceImage imagevalue = 25;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextPieceImageOrBuilder getImagevalueOrBuilder();

  /**
   * <code>string schema_key = 100;</code>
   * @return The schemaKey.
   */
  java.lang.String getSchemaKey();
  /**
   * <code>string schema_key = 100;</code>
   * @return The bytes for schemaKey.
   */
  com.google.protobuf.ByteString
      getSchemaKeyBytes();
}
