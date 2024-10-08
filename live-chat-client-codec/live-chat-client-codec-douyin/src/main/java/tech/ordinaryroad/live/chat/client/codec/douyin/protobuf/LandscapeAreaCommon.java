// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: douyin.proto

// Protobuf Java Version: 3.25.5
package tech.ordinaryroad.live.chat.client.codec.douyin.protobuf;

/**
 * Protobuf type {@code LandscapeAreaCommon}
 */
public final class LandscapeAreaCommon extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:LandscapeAreaCommon)
    LandscapeAreaCommonOrBuilder {
private static final long serialVersionUID = 0L;
  // Use LandscapeAreaCommon.newBuilder() to construct.
  private LandscapeAreaCommon(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private LandscapeAreaCommon() {
    colorValueList_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
    commentTypeTagsList_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new LandscapeAreaCommon();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Douyin.internal_static_LandscapeAreaCommon_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Douyin.internal_static_LandscapeAreaCommon_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon.class, tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon.Builder.class);
  }

  public static final int SHOWHEAD_FIELD_NUMBER = 1;
  private boolean showHead_ = false;
  /**
   * <code>bool showHead = 1;</code>
   * @return The showHead.
   */
  @java.lang.Override
  public boolean getShowHead() {
    return showHead_;
  }

  public static final int SHOWNICKNAME_FIELD_NUMBER = 2;
  private boolean showNickname_ = false;
  /**
   * <code>bool showNickname = 2;</code>
   * @return The showNickname.
   */
  @java.lang.Override
  public boolean getShowNickname() {
    return showNickname_;
  }

  public static final int SHOWFONTCOLOR_FIELD_NUMBER = 3;
  private boolean showFontColor_ = false;
  /**
   * <code>bool showFontColor = 3;</code>
   * @return The showFontColor.
   */
  @java.lang.Override
  public boolean getShowFontColor() {
    return showFontColor_;
  }

  public static final int COLORVALUELIST_FIELD_NUMBER = 4;
  @SuppressWarnings("serial")
  private com.google.protobuf.LazyStringArrayList colorValueList_ =
      com.google.protobuf.LazyStringArrayList.emptyList();
  /**
   * <code>repeated string colorValueList = 4;</code>
   * @return A list containing the colorValueList.
   */
  public com.google.protobuf.ProtocolStringList
      getColorValueListList() {
    return colorValueList_;
  }
  /**
   * <code>repeated string colorValueList = 4;</code>
   * @return The count of colorValueList.
   */
  public int getColorValueListCount() {
    return colorValueList_.size();
  }
  /**
   * <code>repeated string colorValueList = 4;</code>
   * @param index The index of the element to return.
   * @return The colorValueList at the given index.
   */
  public java.lang.String getColorValueList(int index) {
    return colorValueList_.get(index);
  }
  /**
   * <code>repeated string colorValueList = 4;</code>
   * @param index The index of the value to return.
   * @return The bytes of the colorValueList at the given index.
   */
  public com.google.protobuf.ByteString
      getColorValueListBytes(int index) {
    return colorValueList_.getByteString(index);
  }

  public static final int COMMENTTYPETAGSLIST_FIELD_NUMBER = 5;
  @SuppressWarnings("serial")
  private java.util.List<java.lang.Integer> commentTypeTagsList_;
  private static final com.google.protobuf.Internal.ListAdapter.Converter<
      java.lang.Integer, tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag> commentTypeTagsList_converter_ =
          new com.google.protobuf.Internal.ListAdapter.Converter<
              java.lang.Integer, tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag>() {
            public tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag convert(java.lang.Integer from) {
              tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag result = tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag.forNumber(from);
              return result == null ? tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag.UNRECOGNIZED : result;
            }
          };
  /**
   * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
   * @return A list containing the commentTypeTagsList.
   */
  @java.lang.Override
  public java.util.List<tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag> getCommentTypeTagsListList() {
    return new com.google.protobuf.Internal.ListAdapter<
        java.lang.Integer, tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag>(commentTypeTagsList_, commentTypeTagsList_converter_);
  }
  /**
   * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
   * @return The count of commentTypeTagsList.
   */
  @java.lang.Override
  public int getCommentTypeTagsListCount() {
    return commentTypeTagsList_.size();
  }
  /**
   * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
   * @param index The index of the element to return.
   * @return The commentTypeTagsList at the given index.
   */
  @java.lang.Override
  public tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag getCommentTypeTagsList(int index) {
    return commentTypeTagsList_converter_.convert(commentTypeTagsList_.get(index));
  }
  /**
   * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
   * @return A list containing the enum numeric values on the wire for commentTypeTagsList.
   */
  @java.lang.Override
  public java.util.List<java.lang.Integer>
  getCommentTypeTagsListValueList() {
    return commentTypeTagsList_;
  }
  /**
   * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
   * @param index The index of the value to return.
   * @return The enum numeric value on the wire of commentTypeTagsList at the given index.
   */
  @java.lang.Override
  public int getCommentTypeTagsListValue(int index) {
    return commentTypeTagsList_.get(index);
  }
  private int commentTypeTagsListMemoizedSerializedSize;

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    getSerializedSize();
    if (showHead_ != false) {
      output.writeBool(1, showHead_);
    }
    if (showNickname_ != false) {
      output.writeBool(2, showNickname_);
    }
    if (showFontColor_ != false) {
      output.writeBool(3, showFontColor_);
    }
    for (int i = 0; i < colorValueList_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, colorValueList_.getRaw(i));
    }
    if (getCommentTypeTagsListList().size() > 0) {
      output.writeUInt32NoTag(42);
      output.writeUInt32NoTag(commentTypeTagsListMemoizedSerializedSize);
    }
    for (int i = 0; i < commentTypeTagsList_.size(); i++) {
      output.writeEnumNoTag(commentTypeTagsList_.get(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (showHead_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(1, showHead_);
    }
    if (showNickname_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(2, showNickname_);
    }
    if (showFontColor_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(3, showFontColor_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < colorValueList_.size(); i++) {
        dataSize += computeStringSizeNoTag(colorValueList_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getColorValueListList().size();
    }
    {
      int dataSize = 0;
      for (int i = 0; i < commentTypeTagsList_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeEnumSizeNoTag(commentTypeTagsList_.get(i));
      }
      size += dataSize;
      if (!getCommentTypeTagsListList().isEmpty()) {  size += 1;
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32SizeNoTag(dataSize);
      }commentTypeTagsListMemoizedSerializedSize = dataSize;
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon)) {
      return super.equals(obj);
    }
    tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon other = (tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon) obj;

    if (getShowHead()
        != other.getShowHead()) return false;
    if (getShowNickname()
        != other.getShowNickname()) return false;
    if (getShowFontColor()
        != other.getShowFontColor()) return false;
    if (!getColorValueListList()
        .equals(other.getColorValueListList())) return false;
    if (!commentTypeTagsList_.equals(other.commentTypeTagsList_)) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + SHOWHEAD_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getShowHead());
    hash = (37 * hash) + SHOWNICKNAME_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getShowNickname());
    hash = (37 * hash) + SHOWFONTCOLOR_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getShowFontColor());
    if (getColorValueListCount() > 0) {
      hash = (37 * hash) + COLORVALUELIST_FIELD_NUMBER;
      hash = (53 * hash) + getColorValueListList().hashCode();
    }
    if (getCommentTypeTagsListCount() > 0) {
      hash = (37 * hash) + COMMENTTYPETAGSLIST_FIELD_NUMBER;
      hash = (53 * hash) + commentTypeTagsList_.hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code LandscapeAreaCommon}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:LandscapeAreaCommon)
      tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommonOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Douyin.internal_static_LandscapeAreaCommon_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Douyin.internal_static_LandscapeAreaCommon_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon.class, tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon.Builder.class);
    }

    // Construct using tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      showHead_ = false;
      showNickname_ = false;
      showFontColor_ = false;
      colorValueList_ =
          com.google.protobuf.LazyStringArrayList.emptyList();
      commentTypeTagsList_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000010);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.Douyin.internal_static_LandscapeAreaCommon_descriptor;
    }

    @java.lang.Override
    public tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon getDefaultInstanceForType() {
      return tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon.getDefaultInstance();
    }

    @java.lang.Override
    public tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon build() {
      tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon buildPartial() {
      tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon result = new tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon result) {
      if (((bitField0_ & 0x00000010) != 0)) {
        commentTypeTagsList_ = java.util.Collections.unmodifiableList(commentTypeTagsList_);
        bitField0_ = (bitField0_ & ~0x00000010);
      }
      result.commentTypeTagsList_ = commentTypeTagsList_;
    }

    private void buildPartial0(tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.showHead_ = showHead_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.showNickname_ = showNickname_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.showFontColor_ = showFontColor_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        colorValueList_.makeImmutable();
        result.colorValueList_ = colorValueList_;
      }
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon) {
        return mergeFrom((tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon other) {
      if (other == tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon.getDefaultInstance()) return this;
      if (other.getShowHead() != false) {
        setShowHead(other.getShowHead());
      }
      if (other.getShowNickname() != false) {
        setShowNickname(other.getShowNickname());
      }
      if (other.getShowFontColor() != false) {
        setShowFontColor(other.getShowFontColor());
      }
      if (!other.colorValueList_.isEmpty()) {
        if (colorValueList_.isEmpty()) {
          colorValueList_ = other.colorValueList_;
          bitField0_ |= 0x00000008;
        } else {
          ensureColorValueListIsMutable();
          colorValueList_.addAll(other.colorValueList_);
        }
        onChanged();
      }
      if (!other.commentTypeTagsList_.isEmpty()) {
        if (commentTypeTagsList_.isEmpty()) {
          commentTypeTagsList_ = other.commentTypeTagsList_;
          bitField0_ = (bitField0_ & ~0x00000010);
        } else {
          ensureCommentTypeTagsListIsMutable();
          commentTypeTagsList_.addAll(other.commentTypeTagsList_);
        }
        onChanged();
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              showHead_ = input.readBool();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 16: {
              showNickname_ = input.readBool();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 24: {
              showFontColor_ = input.readBool();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 34: {
              java.lang.String s = input.readStringRequireUtf8();
              ensureColorValueListIsMutable();
              colorValueList_.add(s);
              break;
            } // case 34
            case 40: {
              int tmpRaw = input.readEnum();
              ensureCommentTypeTagsListIsMutable();
              commentTypeTagsList_.add(tmpRaw);
              break;
            } // case 40
            case 42: {
              int length = input.readRawVarint32();
              int oldLimit = input.pushLimit(length);
              while(input.getBytesUntilLimit() > 0) {
                int tmpRaw = input.readEnum();
                ensureCommentTypeTagsListIsMutable();
                commentTypeTagsList_.add(tmpRaw);
              }
              input.popLimit(oldLimit);
              break;
            } // case 42
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private boolean showHead_ ;
    /**
     * <code>bool showHead = 1;</code>
     * @return The showHead.
     */
    @java.lang.Override
    public boolean getShowHead() {
      return showHead_;
    }
    /**
     * <code>bool showHead = 1;</code>
     * @param value The showHead to set.
     * @return This builder for chaining.
     */
    public Builder setShowHead(boolean value) {

      showHead_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>bool showHead = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearShowHead() {
      bitField0_ = (bitField0_ & ~0x00000001);
      showHead_ = false;
      onChanged();
      return this;
    }

    private boolean showNickname_ ;
    /**
     * <code>bool showNickname = 2;</code>
     * @return The showNickname.
     */
    @java.lang.Override
    public boolean getShowNickname() {
      return showNickname_;
    }
    /**
     * <code>bool showNickname = 2;</code>
     * @param value The showNickname to set.
     * @return This builder for chaining.
     */
    public Builder setShowNickname(boolean value) {

      showNickname_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>bool showNickname = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearShowNickname() {
      bitField0_ = (bitField0_ & ~0x00000002);
      showNickname_ = false;
      onChanged();
      return this;
    }

    private boolean showFontColor_ ;
    /**
     * <code>bool showFontColor = 3;</code>
     * @return The showFontColor.
     */
    @java.lang.Override
    public boolean getShowFontColor() {
      return showFontColor_;
    }
    /**
     * <code>bool showFontColor = 3;</code>
     * @param value The showFontColor to set.
     * @return This builder for chaining.
     */
    public Builder setShowFontColor(boolean value) {

      showFontColor_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>bool showFontColor = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearShowFontColor() {
      bitField0_ = (bitField0_ & ~0x00000004);
      showFontColor_ = false;
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringArrayList colorValueList_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
    private void ensureColorValueListIsMutable() {
      if (!colorValueList_.isModifiable()) {
        colorValueList_ = new com.google.protobuf.LazyStringArrayList(colorValueList_);
      }
      bitField0_ |= 0x00000008;
    }
    /**
     * <code>repeated string colorValueList = 4;</code>
     * @return A list containing the colorValueList.
     */
    public com.google.protobuf.ProtocolStringList
        getColorValueListList() {
      colorValueList_.makeImmutable();
      return colorValueList_;
    }
    /**
     * <code>repeated string colorValueList = 4;</code>
     * @return The count of colorValueList.
     */
    public int getColorValueListCount() {
      return colorValueList_.size();
    }
    /**
     * <code>repeated string colorValueList = 4;</code>
     * @param index The index of the element to return.
     * @return The colorValueList at the given index.
     */
    public java.lang.String getColorValueList(int index) {
      return colorValueList_.get(index);
    }
    /**
     * <code>repeated string colorValueList = 4;</code>
     * @param index The index of the value to return.
     * @return The bytes of the colorValueList at the given index.
     */
    public com.google.protobuf.ByteString
        getColorValueListBytes(int index) {
      return colorValueList_.getByteString(index);
    }
    /**
     * <code>repeated string colorValueList = 4;</code>
     * @param index The index to set the value at.
     * @param value The colorValueList to set.
     * @return This builder for chaining.
     */
    public Builder setColorValueList(
        int index, java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      ensureColorValueListIsMutable();
      colorValueList_.set(index, value);
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string colorValueList = 4;</code>
     * @param value The colorValueList to add.
     * @return This builder for chaining.
     */
    public Builder addColorValueList(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      ensureColorValueListIsMutable();
      colorValueList_.add(value);
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string colorValueList = 4;</code>
     * @param values The colorValueList to add.
     * @return This builder for chaining.
     */
    public Builder addAllColorValueList(
        java.lang.Iterable<java.lang.String> values) {
      ensureColorValueListIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, colorValueList_);
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string colorValueList = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearColorValueList() {
      colorValueList_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000008);;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string colorValueList = 4;</code>
     * @param value The bytes of the colorValueList to add.
     * @return This builder for chaining.
     */
    public Builder addColorValueListBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      ensureColorValueListIsMutable();
      colorValueList_.add(value);
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }

    private java.util.List<java.lang.Integer> commentTypeTagsList_ =
      java.util.Collections.emptyList();
    private void ensureCommentTypeTagsListIsMutable() {
      if (!((bitField0_ & 0x00000010) != 0)) {
        commentTypeTagsList_ = new java.util.ArrayList<java.lang.Integer>(commentTypeTagsList_);
        bitField0_ |= 0x00000010;
      }
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @return A list containing the commentTypeTagsList.
     */
    public java.util.List<tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag> getCommentTypeTagsListList() {
      return new com.google.protobuf.Internal.ListAdapter<
          java.lang.Integer, tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag>(commentTypeTagsList_, commentTypeTagsList_converter_);
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @return The count of commentTypeTagsList.
     */
    public int getCommentTypeTagsListCount() {
      return commentTypeTagsList_.size();
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @param index The index of the element to return.
     * @return The commentTypeTagsList at the given index.
     */
    public tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag getCommentTypeTagsList(int index) {
      return commentTypeTagsList_converter_.convert(commentTypeTagsList_.get(index));
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @param index The index to set the value at.
     * @param value The commentTypeTagsList to set.
     * @return This builder for chaining.
     */
    public Builder setCommentTypeTagsList(
        int index, tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureCommentTypeTagsListIsMutable();
      commentTypeTagsList_.set(index, value.getNumber());
      onChanged();
      return this;
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @param value The commentTypeTagsList to add.
     * @return This builder for chaining.
     */
    public Builder addCommentTypeTagsList(tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureCommentTypeTagsListIsMutable();
      commentTypeTagsList_.add(value.getNumber());
      onChanged();
      return this;
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @param values The commentTypeTagsList to add.
     * @return This builder for chaining.
     */
    public Builder addAllCommentTypeTagsList(
        java.lang.Iterable<? extends tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag> values) {
      ensureCommentTypeTagsListIsMutable();
      for (tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.CommentTypeTag value : values) {
        commentTypeTagsList_.add(value.getNumber());
      }
      onChanged();
      return this;
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearCommentTypeTagsList() {
      commentTypeTagsList_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000010);
      onChanged();
      return this;
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @return A list containing the enum numeric values on the wire for commentTypeTagsList.
     */
    public java.util.List<java.lang.Integer>
    getCommentTypeTagsListValueList() {
      return java.util.Collections.unmodifiableList(commentTypeTagsList_);
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @param index The index of the value to return.
     * @return The enum numeric value on the wire of commentTypeTagsList at the given index.
     */
    public int getCommentTypeTagsListValue(int index) {
      return commentTypeTagsList_.get(index);
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @param index The index to set the value at.
     * @param value The enum numeric value on the wire for commentTypeTagsList to set.
     * @return This builder for chaining.
     */
    public Builder setCommentTypeTagsListValue(
        int index, int value) {
      ensureCommentTypeTagsListIsMutable();
      commentTypeTagsList_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @param value The enum numeric value on the wire for commentTypeTagsList to add.
     * @return This builder for chaining.
     */
    public Builder addCommentTypeTagsListValue(int value) {
      ensureCommentTypeTagsListIsMutable();
      commentTypeTagsList_.add(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated .CommentTypeTag commentTypeTagsList = 5;</code>
     * @param values The enum numeric values on the wire for commentTypeTagsList to add.
     * @return This builder for chaining.
     */
    public Builder addAllCommentTypeTagsListValue(
        java.lang.Iterable<java.lang.Integer> values) {
      ensureCommentTypeTagsListIsMutable();
      for (int value : values) {
        commentTypeTagsList_.add(value);
      }
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:LandscapeAreaCommon)
  }

  // @@protoc_insertion_point(class_scope:LandscapeAreaCommon)
  private static final tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon();
  }

  public static tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<LandscapeAreaCommon>
      PARSER = new com.google.protobuf.AbstractParser<LandscapeAreaCommon>() {
    @java.lang.Override
    public LandscapeAreaCommon parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<LandscapeAreaCommon> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<LandscapeAreaCommon> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public tech.ordinaryroad.live.chat.client.codec.douyin.protobuf.LandscapeAreaCommon getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

