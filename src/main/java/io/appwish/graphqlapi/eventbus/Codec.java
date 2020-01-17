package io.appwish.graphqlapi.eventbus;

import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.AllWishReplyProto;
import io.appwish.grpc.UpdateWishInputProto;
import io.appwish.grpc.WishInputProto;
import io.appwish.grpc.WishProto;
import io.appwish.grpc.WishQueryProto;
import io.appwish.grpc.WishReplyProto;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;

/**
 * These codecs can be used to enable passing custom Java objects on the local event bus.
 *
 * To enable T type to be passed via the event bus, just create a new {@link LocalReferenceCodec}.
 *
 * It's not enough to add the codec here - you need to register them on the event bus using {@link
 * EventBus#registerCodec(MessageCodec)}.
 */
public enum Codec {
  UPDATE_WISH_INPUT(new LocalReferenceCodec<>(UpdateWishInputProto.class)),
  WISH(new LocalReferenceCodec<>(WishProto.class)),
  ALL_WISH_REPLY(new LocalReferenceCodec<>(AllWishReplyProto.class)),
  WISH_REPLY(new LocalReferenceCodec<>(WishReplyProto.class)),
  ALL_WISH_QUERY(new LocalReferenceCodec<>(AllWishQueryProto.class)),
  WISH_QUERY(new LocalReferenceCodec<>(WishQueryProto.class)),
  WISH_INPUT(new LocalReferenceCodec<>(WishInputProto.class));

  private final LocalReferenceCodec codec;

  Codec(final LocalReferenceCodec codec) {
    this.codec = codec;
  }

  public <T> LocalReferenceCodec<T> getCodec() {
    return codec;
  }

  public String getCodecName() {
    return codec.name();
  }
}
