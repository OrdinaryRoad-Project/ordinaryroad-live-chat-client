// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: douyin.proto

// Protobuf Java Version: 3.25.5
package tech.ordinaryroad.live.chat.client.codec.douyin.protobuf;

public interface PayGradeOrBuilder extends
    // @@protoc_insertion_point(interface_extends:PayGrade)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 totalDiamondCount = 1;</code>
   * @return The totalDiamondCount.
   */
  long getTotalDiamondCount();

  /**
   * <code>.Image diamondIcon = 2;</code>
   * @return Whether the diamondIcon field is set.
   */
  boolean hasDiamondIcon();
  /**
   * <code>.Image diamondIcon = 2;</code>
   * @return The diamondIcon.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getDiamondIcon();
  /**
   * <code>.Image diamondIcon = 2;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getDiamondIconOrBuilder();

  /**
   * <code>string name = 3;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <code>string name = 3;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>.Image icon = 4;</code>
   * @return Whether the icon field is set.
   */
  boolean hasIcon();
  /**
   * <code>.Image icon = 4;</code>
   * @return The icon.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getIcon();
  /**
   * <code>.Image icon = 4;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getIconOrBuilder();

  /**
   * <code>string nextName = 5;</code>
   * @return The nextName.
   */
  java.lang.String getNextName();
  /**
   * <code>string nextName = 5;</code>
   * @return The bytes for nextName.
   */
  com.google.protobuf.ByteString
      getNextNameBytes();

  /**
   * <code>int64 level = 6;</code>
   * @return The level.
   */
  long getLevel();

  /**
   * <code>.Image nextIcon = 7;</code>
   * @return Whether the nextIcon field is set.
   */
  boolean hasNextIcon();
  /**
   * <code>.Image nextIcon = 7;</code>
   * @return The nextIcon.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getNextIcon();
  /**
   * <code>.Image nextIcon = 7;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getNextIconOrBuilder();

  /**
   * <code>int64 nextDiamond = 8;</code>
   * @return The nextDiamond.
   */
  long getNextDiamond();

  /**
   * <code>int64 nowDiamond = 9;</code>
   * @return The nowDiamond.
   */
  long getNowDiamond();

  /**
   * <code>int64 thisGradeMinDiamond = 10;</code>
   * @return The thisGradeMinDiamond.
   */
  long getThisGradeMinDiamond();

  /**
   * <code>int64 thisGradeMaxDiamond = 11;</code>
   * @return The thisGradeMaxDiamond.
   */
  long getThisGradeMaxDiamond();

  /**
   * <code>int64 payDiamondBak = 12;</code>
   * @return The payDiamondBak.
   */
  long getPayDiamondBak();

  /**
   * <code>string gradeDescribe = 13;</code>
   * @return The gradeDescribe.
   */
  java.lang.String getGradeDescribe();
  /**
   * <code>string gradeDescribe = 13;</code>
   * @return The bytes for gradeDescribe.
   */
  com.google.protobuf.ByteString
      getGradeDescribeBytes();

  /**
   * <code>repeated .GradeIcon gradeIconList = 14;</code>
   */
  java.util.List<tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.GradeIcon> 
      getGradeIconListList();
  /**
   * <code>repeated .GradeIcon gradeIconList = 14;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.GradeIcon getGradeIconList(int index);
  /**
   * <code>repeated .GradeIcon gradeIconList = 14;</code>
   */
  int getGradeIconListCount();
  /**
   * <code>repeated .GradeIcon gradeIconList = 14;</code>
   */
  java.util.List<? extends tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.GradeIconOrBuilder> 
      getGradeIconListOrBuilderList();
  /**
   * <code>repeated .GradeIcon gradeIconList = 14;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.GradeIconOrBuilder getGradeIconListOrBuilder(
      int index);

  /**
   * <code>int64 screenChatType = 15;</code>
   * @return The screenChatType.
   */
  long getScreenChatType();

  /**
   * <code>.Image imIcon = 16;</code>
   * @return Whether the imIcon field is set.
   */
  boolean hasImIcon();
  /**
   * <code>.Image imIcon = 16;</code>
   * @return The imIcon.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getImIcon();
  /**
   * <code>.Image imIcon = 16;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getImIconOrBuilder();

  /**
   * <code>.Image imIconWithLevel = 17;</code>
   * @return Whether the imIconWithLevel field is set.
   */
  boolean hasImIconWithLevel();
  /**
   * <code>.Image imIconWithLevel = 17;</code>
   * @return The imIconWithLevel.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getImIconWithLevel();
  /**
   * <code>.Image imIconWithLevel = 17;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getImIconWithLevelOrBuilder();

  /**
   * <code>.Image liveIcon = 18;</code>
   * @return Whether the liveIcon field is set.
   */
  boolean hasLiveIcon();
  /**
   * <code>.Image liveIcon = 18;</code>
   * @return The liveIcon.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getLiveIcon();
  /**
   * <code>.Image liveIcon = 18;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getLiveIconOrBuilder();

  /**
   * <code>.Image newImIconWithLevel = 19;</code>
   * @return Whether the newImIconWithLevel field is set.
   */
  boolean hasNewImIconWithLevel();
  /**
   * <code>.Image newImIconWithLevel = 19;</code>
   * @return The newImIconWithLevel.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getNewImIconWithLevel();
  /**
   * <code>.Image newImIconWithLevel = 19;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getNewImIconWithLevelOrBuilder();

  /**
   * <code>.Image newLiveIcon = 20;</code>
   * @return Whether the newLiveIcon field is set.
   */
  boolean hasNewLiveIcon();
  /**
   * <code>.Image newLiveIcon = 20;</code>
   * @return The newLiveIcon.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getNewLiveIcon();
  /**
   * <code>.Image newLiveIcon = 20;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getNewLiveIconOrBuilder();

  /**
   * <code>int64 upgradeNeedConsume = 21;</code>
   * @return The upgradeNeedConsume.
   */
  long getUpgradeNeedConsume();

  /**
   * <code>string nextPrivileges = 22;</code>
   * @return The nextPrivileges.
   */
  java.lang.String getNextPrivileges();
  /**
   * <code>string nextPrivileges = 22;</code>
   * @return The bytes for nextPrivileges.
   */
  com.google.protobuf.ByteString
      getNextPrivilegesBytes();

  /**
   * <code>.Image background = 23;</code>
   * @return Whether the background field is set.
   */
  boolean hasBackground();
  /**
   * <code>.Image background = 23;</code>
   * @return The background.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getBackground();
  /**
   * <code>.Image background = 23;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getBackgroundOrBuilder();

  /**
   * <code>.Image backgroundBack = 24;</code>
   * @return Whether the backgroundBack field is set.
   */
  boolean hasBackgroundBack();
  /**
   * <code>.Image backgroundBack = 24;</code>
   * @return The backgroundBack.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getBackgroundBack();
  /**
   * <code>.Image backgroundBack = 24;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getBackgroundBackOrBuilder();

  /**
   * <code>int64 score = 25;</code>
   * @return The score.
   */
  long getScore();

  /**
   * <code>.GradeBuffInfo buffInfo = 26;</code>
   * @return Whether the buffInfo field is set.
   */
  boolean hasBuffInfo();
  /**
   * <code>.GradeBuffInfo buffInfo = 26;</code>
   * @return The buffInfo.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.GradeBuffInfo getBuffInfo();
  /**
   * <code>.GradeBuffInfo buffInfo = 26;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.GradeBuffInfoOrBuilder getBuffInfoOrBuilder();

  /**
   * <code>string gradeBanner = 1001;</code>
   * @return The gradeBanner.
   */
  java.lang.String getGradeBanner();
  /**
   * <code>string gradeBanner = 1001;</code>
   * @return The bytes for gradeBanner.
   */
  com.google.protobuf.ByteString
      getGradeBannerBytes();

  /**
   * <code>.Image profileDialogBg = 1002;</code>
   * @return Whether the profileDialogBg field is set.
   */
  boolean hasProfileDialogBg();
  /**
   * <code>.Image profileDialogBg = 1002;</code>
   * @return The profileDialogBg.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getProfileDialogBg();
  /**
   * <code>.Image profileDialogBg = 1002;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getProfileDialogBgOrBuilder();

  /**
   * <code>.Image profileDialogBgBack = 1003;</code>
   * @return Whether the profileDialogBgBack field is set.
   */
  boolean hasProfileDialogBgBack();
  /**
   * <code>.Image profileDialogBgBack = 1003;</code>
   * @return The profileDialogBgBack.
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Image getProfileDialogBgBack();
  /**
   * <code>.Image profileDialogBgBack = 1003;</code>
   */
  tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.ImageOrBuilder getProfileDialogBgBackOrBuilder();
}
