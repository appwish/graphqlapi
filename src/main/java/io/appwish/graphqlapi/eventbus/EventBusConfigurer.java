package io.appwish.graphqlapi.eventbus;

import io.vertx.core.eventbus.EventBus;

/**
 * Encapsulates logic for configuring the event bus
 */
public class EventBusConfigurer {

  private final EventBus eventBus;

  public EventBusConfigurer(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  /**
   * Registers all codecs from {@link Codec} enum
   */
  public void registerCodecs() {
    for (final Codec codec : Codec.values()) {
      eventBus.registerDefaultCodec(codec.getCodec().getTargetClass(), codec.getCodec());
    }
  }
}
