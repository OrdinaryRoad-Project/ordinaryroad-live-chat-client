// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: douyin.proto

// Protobuf Java Version: 3.25.5
package tech.ordinaryroad.live.chat.client.codec.douyin.protobuf;

public interface MemberMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:MemberMessage)
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
   * <code>.User user = 2;</code>
   * @return Whether the user field is set.
   */
  boolean hasUser();
  /**
   * <code>.User user = 2;</code>
   * @return The user.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.User getUser();
  /**
   * <code>.User user = 2;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.UserOrBuilder getUserOrBuilder();

  /**
   * <code>uint64 memberCount = 3;</code>
   * @return The memberCount.
   */
  long getMemberCount();

  /**
   * <code>.User operator = 4;</code>
   * @return Whether the operator field is set.
   */
  boolean hasOperator();
  /**
   * <code>.User operator = 4;</code>
   * @return The operator.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.User getOperator();
  /**
   * <code>.User operator = 4;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.UserOrBuilder getOperatorOrBuilder();

  /**
   * <code>bool isSetToAdmin = 5;</code>
   * @return The isSetToAdmin.
   */
  boolean getIsSetToAdmin();

  /**
   * <code>bool isTopUser = 6;</code>
   * @return The isTopUser.
   */
  boolean getIsTopUser();

  /**
   * <code>uint64 rankScore = 7;</code>
   * @return The rankScore.
   */
  long getRankScore();

  /**
   * <code>uint64 topUserNo = 8;</code>
   * @return The topUserNo.
   */
  long getTopUserNo();

  /**
   * <code>uint64 enterType = 9;</code>
   * @return The enterType.
   */
  long getEnterType();

  /**
   * <code>uint64 action = 10;</code>
   * @return The action.
   */
  long getAction();

  /**
   * <code>string actionDescription = 11;</code>
   * @return The actionDescription.
   */
  java.lang.String getActionDescription();
  /**
   * <code>string actionDescription = 11;</code>
   * @return The bytes for actionDescription.
   */
  com.google.protobuf.ByteString
      getActionDescriptionBytes();

  /**
   * <code>uint64 userId = 12;</code>
   * @return The userId.
   */
  long getUserId();

  /**
   * <code>.EffectConfig effectConfig = 13;</code>
   * @return Whether the effectConfig field is set.
   */
  boolean hasEffectConfig();
  /**
   * <code>.EffectConfig effectConfig = 13;</code>
   * @return The effectConfig.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.EffectConfig getEffectConfig();
  /**
   * <code>.EffectConfig effectConfig = 13;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.EffectConfigOrBuilder getEffectConfigOrBuilder();

  /**
   * <code>string popStr = 14;</code>
   * @return The popStr.
   */
  java.lang.String getPopStr();
  /**
   * <code>string popStr = 14;</code>
   * @return The bytes for popStr.
   */
  com.google.protobuf.ByteString
      getPopStrBytes();

  /**
   * <code>.EffectConfig enterEffectConfig = 15;</code>
   * @return Whether the enterEffectConfig field is set.
   */
  boolean hasEnterEffectConfig();
  /**
   * <code>.EffectConfig enterEffectConfig = 15;</code>
   * @return The enterEffectConfig.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.EffectConfig getEnterEffectConfig();
  /**
   * <code>.EffectConfig enterEffectConfig = 15;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.EffectConfigOrBuilder getEnterEffectConfigOrBuilder();

  /**
   * <code>.Image backgroundImage = 16;</code>
   * @return Whether the backgroundImage field is set.
   */
  boolean hasBackgroundImage();
  /**
   * <code>.Image backgroundImage = 16;</code>
   * @return The backgroundImage.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getBackgroundImage();
  /**
   * <code>.Image backgroundImage = 16;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getBackgroundImageOrBuilder();

  /**
   * <code>.Image backgroundImageV2 = 17;</code>
   * @return Whether the backgroundImageV2 field is set.
   */
  boolean hasBackgroundImageV2();
  /**
   * <code>.Image backgroundImageV2 = 17;</code>
   * @return The backgroundImageV2.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getBackgroundImageV2();
  /**
   * <code>.Image backgroundImageV2 = 17;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getBackgroundImageV2OrBuilder();

  /**
   * <code>.Text anchorDisplayText = 18;</code>
   * @return Whether the anchorDisplayText field is set.
   */
  boolean hasAnchorDisplayText();
  /**
   * <code>.Text anchorDisplayText = 18;</code>
   * @return The anchorDisplayText.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Text getAnchorDisplayText();
  /**
   * <code>.Text anchorDisplayText = 18;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.TextOrBuilder getAnchorDisplayTextOrBuilder();

  /**
   * <code>.PublicAreaCommon publicAreaCommon = 19;</code>
   * @return Whether the publicAreaCommon field is set.
   */
  boolean hasPublicAreaCommon();
  /**
   * <code>.PublicAreaCommon publicAreaCommon = 19;</code>
   * @return The publicAreaCommon.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.PublicAreaCommon getPublicAreaCommon();
  /**
   * <code>.PublicAreaCommon publicAreaCommon = 19;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.PublicAreaCommonOrBuilder getPublicAreaCommonOrBuilder();

  /**
   * <code>uint64 userEnterTipType = 20;</code>
   * @return The userEnterTipType.
   */
  long getUserEnterTipType();

  /**
   * <code>uint64 anchorEnterTipType = 21;</code>
   * @return The anchorEnterTipType.
   */
  long getAnchorEnterTipType();
}
