package io.appwish.graphqlapi.grpc;

import io.grpc.stub.AbstractStub;
import io.vertx.core.eventbus.EventBus;

/**
 * Subclasses should expose functionality of gRPC client stubs on the event bus.
 */
abstract class AbstractGrpcClientService<T extends AbstractStub> {

  final EventBus eventBus;
  final T stub;

  AbstractGrpcClientService(final EventBus eventBus, final T stub) {
    this.eventBus = eventBus;
    this.stub = stub;
  }

  /**
   * Should expose the functionality of one of stubs on the event bus.
   */
  abstract void register();
}
