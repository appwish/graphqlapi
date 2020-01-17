package io.appwish.graphqlapi.grpc;

import io.vertx.core.eventbus.EventBus;

/**
 * Template for exposing data received by gRPC service stubs on the event bus.
 */
public abstract class AbstractGrpcClientService {

  protected final EventBus eventBus;
  protected final GrpcServiceStubsProvider serviceStubsProvider;

  public AbstractGrpcClientService(final EventBus eventBus, final GrpcServiceStubsProvider grpcServiceStubsProvider) {
    this.eventBus = eventBus;
    this.serviceStubsProvider = grpcServiceStubsProvider;
  }

  /**
   * Should expose the functionality of one of the service stubs on the event bus.
   */
  abstract void registerServiceStub();
}
