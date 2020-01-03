package io.appwish.graphqlapi.grpc;

import io.appwish.grpc.AppWishServiceGrpc;
import io.grpc.ManagedChannel;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.grpc.VertxChannelBuilder;

public class ServiceStubs {

  private final Vertx vertx;
  private final JsonObject config;

  public ServiceStubs(final Vertx vertx, final JsonObject config) {
    this.vertx = vertx;
    this.config = config;
  }

  public AppWishServiceGrpc.AppWishServiceVertxStub wishServiceStub() {
    final String wishServiceAddress = config.getString("wishServiceAddress");
    final Integer wishServicePort = config.getInteger("wishServicePort");
    final ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, wishServiceAddress, wishServicePort)
      .usePlaintext(true)
      .build();

    return AppWishServiceGrpc.newVertxStub(channel);
  }
}
