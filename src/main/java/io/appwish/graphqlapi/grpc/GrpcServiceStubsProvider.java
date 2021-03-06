package io.appwish.graphqlapi.grpc;

import io.appwish.grpc.CommentServiceGrpc;
import io.appwish.grpc.CommentServiceGrpc.CommentServiceVertxStub;
import io.appwish.grpc.VoteServiceGrpc;
import io.appwish.grpc.VoteServiceGrpc.VoteServiceVertxStub;
import io.appwish.grpc.WishServiceGrpc;
import io.grpc.ManagedChannel;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.grpc.VertxChannelBuilder;

/**
 * Convenience tool for creating gRPC service stubs.
 */
public class GrpcServiceStubsProvider {

  private static final Logger LOG = LoggerFactory.getLogger(GrpcServiceStubsProvider.class);
  private static final String WISH_SERVICE_HOST = "wishServiceHost";
  private static final String WISH_SERVICE_PORT = "wishServicePort";

  private final Vertx vertx;
  private final JsonObject config;

  public GrpcServiceStubsProvider(final Vertx vertx, final JsonObject config) {
    this.vertx = vertx;
    this.config = config;
  }

  /**
   * Wish service stub can be used to fetch data from wish service
   */
  public WishServiceGrpc.WishServiceVertxStub wishServiceStub() {
    final String wishServiceHost = config.getString(WISH_SERVICE_HOST);
    final Integer wishServicePort = config.getInteger(WISH_SERVICE_PORT);

    LOG.info("Creating wishservice gRPC stub for address: " + wishServiceHost + ":" + wishServicePort);

    final ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, wishServiceHost, wishServicePort)
      .usePlaintext()
      .build();

    return WishServiceGrpc.newVertxStub(channel);
  }

  /**
   * Vote service stub can be used to fetch data from vote service
   */
  public VoteServiceVertxStub voteServiceStub() {
    final String voteServiceHost = config.getString("voteServiceHost");
    final Integer voteServicePort = config.getInteger("voteServicePort");

    LOG.info("Creating voteservice gRPC stub for address: " + voteServiceHost + ":" + voteServicePort);

    final ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, voteServiceHost, voteServicePort)
      .usePlaintext()
      .build();

    return VoteServiceGrpc.newVertxStub(channel);
  }

  /**
   * Comment service stub can be used to fetch data from comment service
   */
  public CommentServiceVertxStub commentServiceStub() {
    final String commentServiceHost = config.getString("commentServiceHost");
    final Integer commentServicePort = config.getInteger("commentServicePort");

    LOG.info("Creating commentservice gRPC stub for address: " + commentServiceHost + ":" + commentServicePort);

    final ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, commentServiceHost, commentServicePort)
      .usePlaintext()
      .build();

    return CommentServiceGrpc.newVertxStub(channel);
  }
}
