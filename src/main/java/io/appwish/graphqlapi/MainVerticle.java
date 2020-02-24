package io.appwish.graphqlapi;

import io.appwish.graphqlapi.eventbus.EventBusConfigurer;
import io.appwish.graphqlapi.grpc.GrpcServiceStubsProvider;
import io.appwish.graphqlapi.verticle.GraphqlServerVerticle;
import io.appwish.graphqlapi.verticle.GrpcClientVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

/**
 * Fetches configuration and deploys all other verticles
 */
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    final ConfigStoreOptions envs = new ConfigStoreOptions().setType("env");
    final ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file").setConfig(new JsonObject().put("path", "conf/config.json"));
    final ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore).addStore(envs);
    final ConfigRetriever configRetriever = ConfigRetriever.create(vertx, options);

    configRetriever.getConfig(event -> {
      final GrpcServiceStubsProvider grpcServiceStubsProvider = new GrpcServiceStubsProvider(vertx, event.result());
      final EventBusConfigurer eventBusConfigurer = new EventBusConfigurer(vertx.eventBus());

      eventBusConfigurer.registerCodecs();

      CompositeFuture.all(
        deployGrpcVerticle(grpcServiceStubsProvider),
        deployGraphqlVerticle(event.result())
      ).setHandler(deploy -> {
        if (deploy.succeeded()) {
          startPromise.complete();
        } else {
          startPromise.fail(deploy.cause());
        }
      });
    });
  }

  private Future<Void> deployGraphqlVerticle(final JsonObject config) {
    final Promise<Void> promise = Promise.promise();

    vertx.deployVerticle(new GraphqlServerVerticle(config), new DeploymentOptions(), res -> {
      if (res.failed()) {
        promise.fail(res.cause());
      } else {
        promise.complete();
      }
    });

    return promise.future();
  }

  private Future<Void> deployGrpcVerticle(final GrpcServiceStubsProvider stubsProvider) {
    final Promise<Void> promise = Promise.promise();

    vertx.deployVerticle(new GrpcClientVerticle(stubsProvider), new DeploymentOptions(), res -> {
      if (res.failed()) {
        promise.fail(res.cause());
      } else {
        promise.complete();
      }
    });

    return promise.future();
  }
}
