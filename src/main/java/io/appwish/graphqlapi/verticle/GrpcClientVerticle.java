package io.appwish.graphqlapi.verticle;

import io.appwish.graphqlapi.grpc.GrpcServiceStubsProvider;
import io.appwish.graphqlapi.grpc.VoteGrpcClientService;
import io.appwish.graphqlapi.grpc.WishGrpcClientService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

/**
 * Verticle exposing other applications on the event bus using gRPC service stubs
 */
public class GrpcClientVerticle extends AbstractVerticle {

  private final GrpcServiceStubsProvider grpcServiceStubsProvider;

  public GrpcClientVerticle(final GrpcServiceStubsProvider stubsProvider) {
    this.grpcServiceStubsProvider = stubsProvider;
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    final WishGrpcClientService wishServiceStub = new WishGrpcClientService(vertx.eventBus(), grpcServiceStubsProvider.wishServiceStub());
    final VoteGrpcClientService voteServiceStub = new VoteGrpcClientService(vertx.eventBus(), grpcServiceStubsProvider.voteServiceStub());
    wishServiceStub.register();
    voteServiceStub.register();
    startPromise.complete();
  }
}
