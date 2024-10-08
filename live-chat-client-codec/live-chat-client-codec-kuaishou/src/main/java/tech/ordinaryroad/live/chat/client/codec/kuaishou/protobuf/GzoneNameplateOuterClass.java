// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: GzoneNameplate.proto

// Protobuf Java Version: 3.25.5
package tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf;

public final class GzoneNameplateOuterClass {
  private GzoneNameplateOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface GzoneNameplateOrBuilder extends
      // @@protoc_insertion_point(interface_extends:GzoneNameplate)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int64 id = 1;</code>
     * @return The id.
     */
    long getId();

    /**
     * <code>string name = 2;</code>
     * @return The name.
     */
    java.lang.String getName();
    /**
     * <code>string name = 2;</code>
     * @return The bytes for name.
     */
    com.google.protobuf.ByteString
        getNameBytes();

    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    java.util.List<tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl> 
        getUrlsList();
    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl getUrls(int index);
    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    int getUrlsCount();
    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    java.util.List<? extends tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrlOrBuilder> 
        getUrlsOrBuilderList();
    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrlOrBuilder getUrlsOrBuilder(
        int index);
  }
  /**
   * Protobuf type {@code GzoneNameplate}
   */
  public static final class GzoneNameplate extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:GzoneNameplate)
      GzoneNameplateOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use GzoneNameplate.newBuilder() to construct.
    private GzoneNameplate(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private GzoneNameplate() {
      name_ = "";
      urls_ = java.util.Collections.emptyList();
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
        UnusedPrivateParameter unused) {
      return new GzoneNameplate();
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.internal_static_GzoneNameplate_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.internal_static_GzoneNameplate_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate.class, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate.Builder.class);
    }

    public static final int ID_FIELD_NUMBER = 1;
    private long id_ = 0L;
    /**
     * <code>int64 id = 1;</code>
     * @return The id.
     */
    @java.lang.Override
    public long getId() {
      return id_;
    }

    public static final int NAME_FIELD_NUMBER = 2;
    @SuppressWarnings("serial")
    private volatile java.lang.Object name_ = "";
    /**
     * <code>string name = 2;</code>
     * @return The name.
     */
    @java.lang.Override
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        name_ = s;
        return s;
      }
    }
    /**
     * <code>string name = 2;</code>
     * @return The bytes for name.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int URLS_FIELD_NUMBER = 3;
    @SuppressWarnings("serial")
    private java.util.List<tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl> urls_;
    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    @java.lang.Override
    public java.util.List<tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl> getUrlsList() {
      return urls_;
    }
    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    @java.lang.Override
    public java.util.List<? extends tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrlOrBuilder> 
        getUrlsOrBuilderList() {
      return urls_;
    }
    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    @java.lang.Override
    public int getUrlsCount() {
      return urls_.size();
    }
    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    @java.lang.Override
    public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl getUrls(int index) {
      return urls_.get(index);
    }
    /**
     * <code>repeated .PicUrl urls = 3;</code>
     */
    @java.lang.Override
    public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrlOrBuilder getUrlsOrBuilder(
        int index) {
      return urls_.get(index);
    }

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
      if (id_ != 0L) {
        output.writeInt64(1, id_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, name_);
      }
      for (int i = 0; i < urls_.size(); i++) {
        output.writeMessage(3, urls_.get(i));
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (id_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, id_);
      }
      if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, name_);
      }
      for (int i = 0; i < urls_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, urls_.get(i));
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
      if (!(obj instanceof tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate)) {
        return super.equals(obj);
      }
      tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate other = (tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate) obj;

      if (getId()
          != other.getId()) return false;
      if (!getName()
          .equals(other.getName())) return false;
      if (!getUrlsList()
          .equals(other.getUrlsList())) return false;
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
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getId());
      hash = (37 * hash) + NAME_FIELD_NUMBER;
      hash = (53 * hash) + getName().hashCode();
      if (getUrlsCount() > 0) {
        hash = (37 * hash) + URLS_FIELD_NUMBER;
        hash = (53 * hash) + getUrlsList().hashCode();
      }
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }

    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate parseFrom(
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
    public static Builder newBuilder(tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate prototype) {
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
     * Protobuf type {@code GzoneNameplate}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:GzoneNameplate)
        tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplateOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.internal_static_GzoneNameplate_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.internal_static_GzoneNameplate_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate.class, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate.Builder.class);
      }

      // Construct using tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate.newBuilder()
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
        id_ = 0L;
        name_ = "";
        if (urlsBuilder_ == null) {
          urls_ = java.util.Collections.emptyList();
        } else {
          urls_ = null;
          urlsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.internal_static_GzoneNameplate_descriptor;
      }

      @java.lang.Override
      public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate getDefaultInstanceForType() {
        return tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate.getDefaultInstance();
      }

      @java.lang.Override
      public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate build() {
        tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate buildPartial() {
        tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate result = new tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate(this);
        buildPartialRepeatedFields(result);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartialRepeatedFields(tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate result) {
        if (urlsBuilder_ == null) {
          if (((bitField0_ & 0x00000004) != 0)) {
            urls_ = java.util.Collections.unmodifiableList(urls_);
            bitField0_ = (bitField0_ & ~0x00000004);
          }
          result.urls_ = urls_;
        } else {
          result.urls_ = urlsBuilder_.build();
        }
      }

      private void buildPartial0(tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.id_ = id_;
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.name_ = name_;
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
        if (other instanceof tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate) {
          return mergeFrom((tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate other) {
        if (other == tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate.getDefaultInstance()) return this;
        if (other.getId() != 0L) {
          setId(other.getId());
        }
        if (!other.getName().isEmpty()) {
          name_ = other.name_;
          bitField0_ |= 0x00000002;
          onChanged();
        }
        if (urlsBuilder_ == null) {
          if (!other.urls_.isEmpty()) {
            if (urls_.isEmpty()) {
              urls_ = other.urls_;
              bitField0_ = (bitField0_ & ~0x00000004);
            } else {
              ensureUrlsIsMutable();
              urls_.addAll(other.urls_);
            }
            onChanged();
          }
        } else {
          if (!other.urls_.isEmpty()) {
            if (urlsBuilder_.isEmpty()) {
              urlsBuilder_.dispose();
              urlsBuilder_ = null;
              urls_ = other.urls_;
              bitField0_ = (bitField0_ & ~0x00000004);
              urlsBuilder_ = 
                com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                   getUrlsFieldBuilder() : null;
            } else {
              urlsBuilder_.addAllMessages(other.urls_);
            }
          }
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
                id_ = input.readInt64();
                bitField0_ |= 0x00000001;
                break;
              } // case 8
              case 18: {
                name_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000002;
                break;
              } // case 18
              case 26: {
                tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl m =
                    input.readMessage(
                        tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.parser(),
                        extensionRegistry);
                if (urlsBuilder_ == null) {
                  ensureUrlsIsMutable();
                  urls_.add(m);
                } else {
                  urlsBuilder_.addMessage(m);
                }
                break;
              } // case 26
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

      private long id_ ;
      /**
       * <code>int64 id = 1;</code>
       * @return The id.
       */
      @java.lang.Override
      public long getId() {
        return id_;
      }
      /**
       * <code>int64 id = 1;</code>
       * @param value The id to set.
       * @return This builder for chaining.
       */
      public Builder setId(long value) {

        id_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>int64 id = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        id_ = 0L;
        onChanged();
        return this;
      }

      private java.lang.Object name_ = "";
      /**
       * <code>string name = 2;</code>
       * @return The name.
       */
      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string name = 2;</code>
       * @return The bytes for name.
       */
      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string name = 2;</code>
       * @param value The name to set.
       * @return This builder for chaining.
       */
      public Builder setName(
          java.lang.String value) {
        if (value == null) { throw new NullPointerException(); }
        name_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       * <code>string name = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearName() {
        name_ = getDefaultInstance().getName();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      /**
       * <code>string name = 2;</code>
       * @param value The bytes for name to set.
       * @return This builder for chaining.
       */
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        name_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }

      private java.util.List<tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl> urls_ =
        java.util.Collections.emptyList();
      private void ensureUrlsIsMutable() {
        if (!((bitField0_ & 0x00000004) != 0)) {
          urls_ = new java.util.ArrayList<tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl>(urls_);
          bitField0_ |= 0x00000004;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilderV3<
          tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrlOrBuilder> urlsBuilder_;

      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public java.util.List<tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl> getUrlsList() {
        if (urlsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(urls_);
        } else {
          return urlsBuilder_.getMessageList();
        }
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public int getUrlsCount() {
        if (urlsBuilder_ == null) {
          return urls_.size();
        } else {
          return urlsBuilder_.getCount();
        }
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl getUrls(int index) {
        if (urlsBuilder_ == null) {
          return urls_.get(index);
        } else {
          return urlsBuilder_.getMessage(index);
        }
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public Builder setUrls(
          int index, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl value) {
        if (urlsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUrlsIsMutable();
          urls_.set(index, value);
          onChanged();
        } else {
          urlsBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public Builder setUrls(
          int index, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder builderForValue) {
        if (urlsBuilder_ == null) {
          ensureUrlsIsMutable();
          urls_.set(index, builderForValue.build());
          onChanged();
        } else {
          urlsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public Builder addUrls(tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl value) {
        if (urlsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUrlsIsMutable();
          urls_.add(value);
          onChanged();
        } else {
          urlsBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public Builder addUrls(
          int index, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl value) {
        if (urlsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUrlsIsMutable();
          urls_.add(index, value);
          onChanged();
        } else {
          urlsBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public Builder addUrls(
          tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder builderForValue) {
        if (urlsBuilder_ == null) {
          ensureUrlsIsMutable();
          urls_.add(builderForValue.build());
          onChanged();
        } else {
          urlsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public Builder addUrls(
          int index, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder builderForValue) {
        if (urlsBuilder_ == null) {
          ensureUrlsIsMutable();
          urls_.add(index, builderForValue.build());
          onChanged();
        } else {
          urlsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public Builder addAllUrls(
          java.lang.Iterable<? extends tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl> values) {
        if (urlsBuilder_ == null) {
          ensureUrlsIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
              values, urls_);
          onChanged();
        } else {
          urlsBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public Builder clearUrls() {
        if (urlsBuilder_ == null) {
          urls_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
          onChanged();
        } else {
          urlsBuilder_.clear();
        }
        return this;
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public Builder removeUrls(int index) {
        if (urlsBuilder_ == null) {
          ensureUrlsIsMutable();
          urls_.remove(index);
          onChanged();
        } else {
          urlsBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder getUrlsBuilder(
          int index) {
        return getUrlsFieldBuilder().getBuilder(index);
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrlOrBuilder getUrlsOrBuilder(
          int index) {
        if (urlsBuilder_ == null) {
          return urls_.get(index);  } else {
          return urlsBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public java.util.List<? extends tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrlOrBuilder> 
           getUrlsOrBuilderList() {
        if (urlsBuilder_ != null) {
          return urlsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(urls_);
        }
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder addUrlsBuilder() {
        return getUrlsFieldBuilder().addBuilder(
            tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.getDefaultInstance());
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder addUrlsBuilder(
          int index) {
        return getUrlsFieldBuilder().addBuilder(
            index, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.getDefaultInstance());
      }
      /**
       * <code>repeated .PicUrl urls = 3;</code>
       */
      public java.util.List<tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder> 
           getUrlsBuilderList() {
        return getUrlsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilderV3<
          tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrlOrBuilder> 
          getUrlsFieldBuilder() {
        if (urlsBuilder_ == null) {
          urlsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
              tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrl.Builder, tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.PicUrlOrBuilder>(
                  urls_,
                  ((bitField0_ & 0x00000004) != 0),
                  getParentForChildren(),
                  isClean());
          urls_ = null;
        }
        return urlsBuilder_;
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


      // @@protoc_insertion_point(builder_scope:GzoneNameplate)
    }

    // @@protoc_insertion_point(class_scope:GzoneNameplate)
    private static final tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate();
    }

    public static tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<GzoneNameplate>
        PARSER = new com.google.protobuf.AbstractParser<GzoneNameplate>() {
      @java.lang.Override
      public GzoneNameplate parsePartialFrom(
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

    public static com.google.protobuf.Parser<GzoneNameplate> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<GzoneNameplate> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.GzoneNameplateOuterClass.GzoneNameplate getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GzoneNameplate_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_GzoneNameplate_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\024GzoneNameplate.proto\032\014PicUrl.proto\"A\n\016" +
      "GzoneNameplate\022\n\n\002id\030\001 \001(\003\022\014\n\004name\030\002 \001(\t" +
      "\022\025\n\004urls\030\003 \003(\0132\007.PicUrlB<\n:tech.ordinary" +
      "road.live.chat.client.codec.kuaishou.pro" +
      "tobufb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.getDescriptor(),
        });
    internal_static_GzoneNameplate_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_GzoneNameplate_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_GzoneNameplate_descriptor,
        new java.lang.String[] { "Id", "Name", "Urls", });
    tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.PicUrlOuterClass.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
