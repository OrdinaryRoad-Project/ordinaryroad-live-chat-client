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
// source: SCWebEnterRoomAck.proto

// Protobuf Java Version: 3.25.3
package tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf;

public final class SCWebEnterRoomAckOuterClass {
  private SCWebEnterRoomAckOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface SCWebEnterRoomAckOrBuilder extends
      // @@protoc_insertion_point(interface_extends:SCWebEnterRoomAck)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>uint64 minReconnectMs = 1;</code>
     * @return The minReconnectMs.
     */
    long getMinReconnectMs();

    /**
     * <code>uint64 maxReconnectMs = 2;</code>
     * @return The maxReconnectMs.
     */
    long getMaxReconnectMs();

    /**
     * <code>uint64 heartbeatIntervalMs = 3;</code>
     * @return The heartbeatIntervalMs.
     */
    long getHeartbeatIntervalMs();
  }
  /**
   * Protobuf type {@code SCWebEnterRoomAck}
   */
  public static final class SCWebEnterRoomAck extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:SCWebEnterRoomAck)
      SCWebEnterRoomAckOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use SCWebEnterRoomAck.newBuilder() to construct.
    private SCWebEnterRoomAck(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private SCWebEnterRoomAck() {
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new SCWebEnterRoomAck();
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return SCWebEnterRoomAckOuterClass.internal_static_SCWebEnterRoomAck_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return SCWebEnterRoomAckOuterClass.internal_static_SCWebEnterRoomAck_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              SCWebEnterRoomAck.class, Builder.class);
    }

    public static final int MINRECONNECTMS_FIELD_NUMBER = 1;
    private long minReconnectMs_ = 0L;
    /**
     * <code>uint64 minReconnectMs = 1;</code>
     * @return The minReconnectMs.
     */
    @Override
    public long getMinReconnectMs() {
      return minReconnectMs_;
    }

    public static final int MAXRECONNECTMS_FIELD_NUMBER = 2;
    private long maxReconnectMs_ = 0L;
    /**
     * <code>uint64 maxReconnectMs = 2;</code>
     * @return The maxReconnectMs.
     */
    @Override
    public long getMaxReconnectMs() {
      return maxReconnectMs_;
    }

    public static final int HEARTBEATINTERVALMS_FIELD_NUMBER = 3;
    private long heartbeatIntervalMs_ = 0L;
    /**
     * <code>uint64 heartbeatIntervalMs = 3;</code>
     * @return The heartbeatIntervalMs.
     */
    @Override
    public long getHeartbeatIntervalMs() {
      return heartbeatIntervalMs_;
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (minReconnectMs_ != 0L) {
        output.writeUInt64(1, minReconnectMs_);
      }
      if (maxReconnectMs_ != 0L) {
        output.writeUInt64(2, maxReconnectMs_);
      }
      if (heartbeatIntervalMs_ != 0L) {
        output.writeUInt64(3, heartbeatIntervalMs_);
      }
      getUnknownFields().writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (minReconnectMs_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(1, minReconnectMs_);
      }
      if (maxReconnectMs_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(2, maxReconnectMs_);
      }
      if (heartbeatIntervalMs_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(3, heartbeatIntervalMs_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof SCWebEnterRoomAck)) {
        return super.equals(obj);
      }
      SCWebEnterRoomAck other = (SCWebEnterRoomAck) obj;

      if (getMinReconnectMs()
          != other.getMinReconnectMs()) return false;
      if (getMaxReconnectMs()
          != other.getMaxReconnectMs()) return false;
      if (getHeartbeatIntervalMs()
          != other.getHeartbeatIntervalMs()) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + MINRECONNECTMS_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getMinReconnectMs());
      hash = (37 * hash) + MAXRECONNECTMS_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getMaxReconnectMs());
      hash = (37 * hash) + HEARTBEATINTERVALMS_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getHeartbeatIntervalMs());
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static SCWebEnterRoomAck parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static SCWebEnterRoomAck parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static SCWebEnterRoomAck parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static SCWebEnterRoomAck parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static SCWebEnterRoomAck parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static SCWebEnterRoomAck parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static SCWebEnterRoomAck parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static SCWebEnterRoomAck parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static SCWebEnterRoomAck parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }

    public static SCWebEnterRoomAck parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static SCWebEnterRoomAck parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static SCWebEnterRoomAck parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(SCWebEnterRoomAck prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code SCWebEnterRoomAck}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:SCWebEnterRoomAck)
        SCWebEnterRoomAckOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return SCWebEnterRoomAckOuterClass.internal_static_SCWebEnterRoomAck_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return SCWebEnterRoomAckOuterClass.internal_static_SCWebEnterRoomAck_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                SCWebEnterRoomAck.class, Builder.class);
      }

      // Construct using tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.SCWebEnterRoomAckOuterClass.SCWebEnterRoomAck.newBuilder()
      private Builder() {

      }

      private Builder(
          BuilderParent parent) {
        super(parent);

      }
      @Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        minReconnectMs_ = 0L;
        maxReconnectMs_ = 0L;
        heartbeatIntervalMs_ = 0L;
        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return SCWebEnterRoomAckOuterClass.internal_static_SCWebEnterRoomAck_descriptor;
      }

      @Override
      public SCWebEnterRoomAck getDefaultInstanceForType() {
        return SCWebEnterRoomAck.getDefaultInstance();
      }

      @Override
      public SCWebEnterRoomAck build() {
        SCWebEnterRoomAck result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public SCWebEnterRoomAck buildPartial() {
        SCWebEnterRoomAck result = new SCWebEnterRoomAck(this);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartial0(SCWebEnterRoomAck result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.minReconnectMs_ = minReconnectMs_;
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.maxReconnectMs_ = maxReconnectMs_;
        }
        if (((from_bitField0_ & 0x00000004) != 0)) {
          result.heartbeatIntervalMs_ = heartbeatIntervalMs_;
        }
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof SCWebEnterRoomAck) {
          return mergeFrom((SCWebEnterRoomAck)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(SCWebEnterRoomAck other) {
        if (other == SCWebEnterRoomAck.getDefaultInstance()) return this;
        if (other.getMinReconnectMs() != 0L) {
          setMinReconnectMs(other.getMinReconnectMs());
        }
        if (other.getMaxReconnectMs() != 0L) {
          setMaxReconnectMs(other.getMaxReconnectMs());
        }
        if (other.getHeartbeatIntervalMs() != 0L) {
          setHeartbeatIntervalMs(other.getHeartbeatIntervalMs());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new NullPointerException();
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
                minReconnectMs_ = input.readUInt64();
                bitField0_ |= 0x00000001;
                break;
              } // case 8
              case 16: {
                maxReconnectMs_ = input.readUInt64();
                bitField0_ |= 0x00000002;
                break;
              } // case 16
              case 24: {
                heartbeatIntervalMs_ = input.readUInt64();
                bitField0_ |= 0x00000004;
                break;
              } // case 24
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

      private long minReconnectMs_ ;
      /**
       * <code>uint64 minReconnectMs = 1;</code>
       * @return The minReconnectMs.
       */
      @Override
      public long getMinReconnectMs() {
        return minReconnectMs_;
      }
      /**
       * <code>uint64 minReconnectMs = 1;</code>
       * @param value The minReconnectMs to set.
       * @return This builder for chaining.
       */
      public Builder setMinReconnectMs(long value) {

        minReconnectMs_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>uint64 minReconnectMs = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearMinReconnectMs() {
        bitField0_ = (bitField0_ & ~0x00000001);
        minReconnectMs_ = 0L;
        onChanged();
        return this;
      }

      private long maxReconnectMs_ ;
      /**
       * <code>uint64 maxReconnectMs = 2;</code>
       * @return The maxReconnectMs.
       */
      @Override
      public long getMaxReconnectMs() {
        return maxReconnectMs_;
      }
      /**
       * <code>uint64 maxReconnectMs = 2;</code>
       * @param value The maxReconnectMs to set.
       * @return This builder for chaining.
       */
      public Builder setMaxReconnectMs(long value) {

        maxReconnectMs_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       * <code>uint64 maxReconnectMs = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearMaxReconnectMs() {
        bitField0_ = (bitField0_ & ~0x00000002);
        maxReconnectMs_ = 0L;
        onChanged();
        return this;
      }

      private long heartbeatIntervalMs_ ;
      /**
       * <code>uint64 heartbeatIntervalMs = 3;</code>
       * @return The heartbeatIntervalMs.
       */
      @Override
      public long getHeartbeatIntervalMs() {
        return heartbeatIntervalMs_;
      }
      /**
       * <code>uint64 heartbeatIntervalMs = 3;</code>
       * @param value The heartbeatIntervalMs to set.
       * @return This builder for chaining.
       */
      public Builder setHeartbeatIntervalMs(long value) {

        heartbeatIntervalMs_ = value;
        bitField0_ |= 0x00000004;
        onChanged();
        return this;
      }
      /**
       * <code>uint64 heartbeatIntervalMs = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearHeartbeatIntervalMs() {
        bitField0_ = (bitField0_ & ~0x00000004);
        heartbeatIntervalMs_ = 0L;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:SCWebEnterRoomAck)
    }

    // @@protoc_insertion_point(class_scope:SCWebEnterRoomAck)
    private static final SCWebEnterRoomAck DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new SCWebEnterRoomAck();
    }

    public static SCWebEnterRoomAck getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<SCWebEnterRoomAck>
        PARSER = new com.google.protobuf.AbstractParser<SCWebEnterRoomAck>() {
      @Override
      public SCWebEnterRoomAck parsePartialFrom(
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

    public static com.google.protobuf.Parser<SCWebEnterRoomAck> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<SCWebEnterRoomAck> getParserForType() {
      return PARSER;
    }

    @Override
    public SCWebEnterRoomAck getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_SCWebEnterRoomAck_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_SCWebEnterRoomAck_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\027SCWebEnterRoomAck.proto\"`\n\021SCWebEnterR" +
      "oomAck\022\026\n\016minReconnectMs\030\001 \001(\004\022\026\n\016maxRec" +
      "onnectMs\030\002 \001(\004\022\033\n\023heartbeatIntervalMs\030\003 " +
      "\001(\004B<\n:tech.ordinaryroad.live.chat.clien" +
      "t.codec.kuaishou.protobufb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_SCWebEnterRoomAck_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_SCWebEnterRoomAck_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_SCWebEnterRoomAck_descriptor,
        new String[] { "MinReconnectMs", "MaxReconnectMs", "HeartbeatIntervalMs", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}