package io.appwish.graphqlapi;

import io.appwish.graphqlapi.testutil.DummySuccessfulWishService;
import io.appwish.graphqlapi.testutil.InProcessServer;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@ExtendWith(VertxExtension.class)
class GraphQLVerticleTest {

  private InProcessServer<DummySuccessfulWishService> dummySuccessfulWishServiceInProcessServer;
  private ManagedChannel successfulChannel;

  @BeforeEach
  void deploy_verticle(final Vertx vertx, final VertxTestContext testContext) throws IllegalAccessException, IOException, InstantiationException {
    dummySuccessfulWishServiceInProcessServer = new InProcessServer<>(DummySuccessfulWishService.class);
    dummySuccessfulWishServiceInProcessServer.start("AppWishService");
    successfulChannel = InProcessChannelBuilder
      .forName("AppWishService")
      .directExecutor()
      .usePlaintext(true)
      .build();
    vertx.deployVerticle(new GraphQLVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @AfterEach
  void tear_down() {
    successfulChannel.shutdownNow();
    dummySuccessfulWishServiceInProcessServer.stop();
  }

  @Test
  void verticle_deployed(final Vertx vertx, final VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void graphql_endpoint_exposed(final Vertx vertx, final VertxTestContext testContext) throws Throwable {
    // given
    final WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8000));
    final JsonObject request = new JsonObject().put("query", "query { allAppWish { id } }");

    // when
    client.post("/graphql")
      .expect(ResponsePredicate.SC_OK)
      .expect(ResponsePredicate.JSON)
      .as(BodyCodec.jsonObject())
      .sendJsonObject(request, ar -> {

        // then
        if (ar.succeeded()) {
          final JsonObject response = ar.result().body();
          System.out.println("response = " + response.encodePrettily());
          testContext.completeNow();
        } else {
          ar.cause().printStackTrace();
          testContext.failNow(ar.cause());
        }
      });
  }

  @Test
  void graphiql_endpoint_exposed_on_dev_env(final Vertx vertx, final VertxTestContext testContext) throws Throwable {
    // given
    final WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8000));
    final JsonObject request = new JsonObject().put("query", "query { allAppWish { id } }");

    // when
    client.post("/graphiql/")
      .expect(ResponsePredicate.SC_OK)
      .expect(ResponsePredicate.contentType("text/html;charset=utf8"))
      .as(BodyCodec.string())
      .sendJsonObject(request, ar -> {

        // then
        if (ar.succeeded()) {
          final String response = ar.result().body();
          System.out.println("response = " + response);
          testContext.completeNow();
        } else {
          ar.cause().printStackTrace();
          testContext.failNow(ar.cause());
        }
      });
  }
}
