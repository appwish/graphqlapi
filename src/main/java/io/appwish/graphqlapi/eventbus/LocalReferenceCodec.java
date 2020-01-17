package io.appwish.graphqlapi.eventbus;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * MessageCodecs are required to pass custom objects via the event bus.
 *
 * This implementation enables passing objects via their reference.
 *
 * The {@link LocalReferenceCodec} transfer method is invoked during local event bus message
 * exchange and just returns the object reference to avoid any serialization.
 *
 * You should use this class only to pass messages on a single JVM / locally.
 */
public final class LocalReferenceCodec<T> implements MessageCodec<T, T> {

  private final Class<T> targetClass;

  LocalReferenceCodec(final Class<T> targetClass) {
    this.targetClass = targetClass;
  }

  @Override
  public void encodeToWire(final Buffer buffer, final T t) {
    throw new UnsupportedOperationException();
  }

  @Override
  public T decodeFromWire(final int pos, final Buffer buffer) {
    throw new UnsupportedOperationException();
  }

  @Override
  public T transform(final T t) {
    return t;
  }

  @Override
  public String name() {
    return targetClass.getName();
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }

  public Class<T> getTargetClass() {
    return targetClass;
  }
}
