package io.appwish.graphqlapi.grpc;

import io.appwish.grpc.WishServiceGrpc;
import io.grpc.ManagedChannel;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.grpc.VertxChannelBuilder;

public class ServiceStubs {

  private static final String WISH_SERVICE_HOST = "wishServiceHost";
  private static final String WISH_SERVICE_PORT = "wishServicePort";

  private final Vertx vertx;
  private final JsonObject config;

  public ServiceStubs(final Vertx vertx, final JsonObject config) {
    this.vertx = vertx;
    this.config = config;
  }

  public WishServiceGrpc.WishServiceVertxStub wishServiceStub() {
    final String wishServiceHost = config.getString(WISH_SERVICE_HOST);
    final Integer wishServicePort = config.getInteger(WISH_SERVICE_PORT);
    final ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, wishServiceHost, wishServicePort)
      .usePlaintext(true)
      .build();

    return WishServiceGrpc.newVertxStub(channel);
  }
}
