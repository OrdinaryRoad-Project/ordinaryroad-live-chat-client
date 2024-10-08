// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: WebPauseType.proto

// Protobuf Java Version: 3.25.5
package tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf;

public final class WebPauseTypeOuterClass {
  private WebPauseTypeOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * Protobuf enum {@code WebPauseType}
   */
  public enum WebPauseType
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>UNKNOWN_PAUSE_TYPE = 0;</code>
     */
    UNKNOWN_PAUSE_TYPE(0),
    /**
     * <code>TELEPHONE = 1;</code>
     */
    TELEPHONE(1),
    /**
     * <code>SHARE = 2;</code>
     */
    SHARE(2),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>UNKNOWN_PAUSE_TYPE = 0;</code>
     */
    public static final int UNKNOWN_PAUSE_TYPE_VALUE = 0;
    /**
     * <code>TELEPHONE = 1;</code>
     */
    public static final int TELEPHONE_VALUE = 1;
    /**
     * <code>SHARE = 2;</code>
     */
    public static final int SHARE_VALUE = 2;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static WebPauseType valueOf(int value) {
      return forNumber(value);
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     */
    public static WebPauseType forNumber(int value) {
      switch (value) {
        case 0: return UNKNOWN_PAUSE_TYPE;
        case 1: return TELEPHONE;
        case 2: return SHARE;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<WebPauseType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        WebPauseType> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<WebPauseType>() {
            public WebPauseType findValueByNumber(int number) {
              return WebPauseType.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalStateException(
            "Can't get the descriptor of an unrecognized enum value.");
      }
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return tech.ordinaryroad.live.chat.client.codec.kuaishou.protobuf.WebPauseTypeOuterClass.getDescriptor().getEnumTypes().get(0);
    }

    private static final WebPauseType[] VALUES = values();

    public static WebPauseType valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private WebPauseType(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:WebPauseType)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022WebPauseType.proto*@\n\014WebPauseType\022\026\n\022" +
      "UNKNOWN_PAUSE_TYPE\020\000\022\r\n\tTELEPHONE\020\001\022\t\n\005S" +
      "HARE\020\002B<\n:tech.ordinaryroad.live.chat.cl" +
      "ient.codec.kuaishou.protobufb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
