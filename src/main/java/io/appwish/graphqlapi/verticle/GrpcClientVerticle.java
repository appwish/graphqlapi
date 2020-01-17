package io.appwish.graphqlapi.verticle;

import io.appwish.graphqlapi.grpc.WishGrpcClientService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Verticle exposing other applications on the event bus using gRPC service stubs
 */
public class GrpcClientVerticle extends AbstractVerticle {

  private final WishGrpcClientService wishGrpcClientService;

  public GrpcClientVerticle(final WishGrpcClientService wishGrpcClientService) {
    this.wishGrpcClientService = wishGrpcClientService;
  }

  @Override
  public void start(final Future<Void> startFuture) throws Exception {
    wishGrpcClientService.registerServiceStub();
    super.start(startFuture);
  }
}
