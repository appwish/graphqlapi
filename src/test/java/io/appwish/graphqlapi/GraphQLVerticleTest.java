package io.appwish.graphqlapi;

import io.appwish.graphqlapi.testutil.DummyGRPCServer;
import io.appwish.graphqlapi.testutil.DummySuccessWishService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(VertxExtension.class)
class GraphQLVerticleTest {

  private static final int APP_PORT = 8000;
  private static final int DUMMY_WISH_SERVICE_PORT = 7777;
  private static final String GRAPHQL_PATH = "/graphql";
  private static final String GRAPHIQL_PATH = "/graphiql/";
  private static final String TEXT_HTML = "text/html;charset=utf8";

  private DummyGRPCServer<DummySuccessWishService> wishServiceDummyGRPCServer;

  @BeforeEach
  void deploy_verticle(final Vertx vertx, final VertxTestContext testContext) throws IllegalAccessException, IOException, InstantiationException {
    wishServiceDummyGRPCServer = new DummyGRPCServer<>(DummySuccessWishService.class, DUMMY_WISH_SERVICE_PORT);
    wishServiceDummyGRPCServer.start();
    vertx.deployVerticle(new GraphQLVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @AfterEach
  void tear_down() {
    wishServiceDummyGRPCServer.stop();
  }

  @Test
  void verticle_deployed(final Vertx vertx, final VertxTestContext testContext) {
    testContext.completeNow();
  }

  @Test
  void graphql_endpoint_exposed(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(APP_PORT));
    final JsonObject request = new JsonObject().put("query", "query { allAppWish { id } }");

    // when
    client.post(GRAPHQL_PATH)
      .expect(ResponsePredicate.SC_OK)
      .expect(ResponsePredicate.JSON)
      .as(BodyCodec.jsonObject())
      .sendJsonObject(request, ar -> {

        // then
        if (ar.succeeded()) {
          final JsonObject response = ar.result().body();
          assertNotNull(response);
          testContext.completeNow();
        } else {
          testContext.failNow(ar.cause());
        }
      });
  }

  @Test
  void graphiql_endpoint_exposed_on_dev_env(final Vertx vertx, final VertxTestContext testContext) throws Throwable {
    // given
    final WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(APP_PORT));
    final JsonObject request = new JsonObject().put("query", "query { allAppWish { id } }");

    // when
    client.post(GRAPHIQL_PATH)
      .expect(ResponsePredicate.SC_OK)
      .expect(ResponsePredicate.contentType(TEXT_HTML))
      .as(BodyCodec.string())
      .sendJsonObject(request, ar -> {

        // then
        if (ar.succeeded()) {
          final String response = ar.result().body();
          assertNotNull(response);
          testContext.completeNow();
        } else {
          testContext.failNow(ar.cause());
        }
      });
  }

  @Test
  void graphql_query_should_return_all_app_wishes(final Vertx vertx, final VertxTestContext testContext) throws Throwable {
    // given
    final WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(APP_PORT));
    final JsonObject request = new JsonObject().put("query", "query { allAppWish { id } }");

    // when
    client.post(GRAPHQL_PATH)
      .expect(ResponsePredicate.SC_OK)
      .expect(ResponsePredicate.JSON)
      .as(BodyCodec.jsonObject())
      .sendJsonObject(request, ar -> {

        // then
        if (ar.succeeded()) {
          final JsonObject response = ar.result().body();
          testContext.verify(() -> {
            final JsonArray allAppWish = ar.result().body().getJsonObject("data").getJsonArray("allAppWish");
            assertEquals(3, allAppWish.size());
            testContext.completeNow();
          });
        } else {
          testContext.failNow(ar.cause());
        }
      });
  }

  @Test
  void graphql_query_should_return_selected_app_wish(final Vertx vertx, final VertxTestContext testContext) throws Throwable {
    // given
    final WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(APP_PORT));
    final String testId = "test-id-1";
    final JsonObject request = new JsonObject()
      .put("query", "query AppWish($id: ID) { appWish(id: $id) { id } }")
      .put("variables", new JsonObject().put("id", testId));

    // when
    client.post(GRAPHQL_PATH)
      .expect(ResponsePredicate.SC_OK)
      .expect(ResponsePredicate.JSON)
      .as(BodyCodec.jsonObject())
      .sendJsonObject(request, ar -> {

        // then
        if (ar.succeeded()) {
          testContext.verify(() -> {
            final JsonObject appWish = ar.result().body().getJsonObject("data").getJsonObject("appWish");
            assertEquals(testId, appWish.getString("id"));
            testContext.completeNow();
          });
        } else {
          ar.cause().printStackTrace();
          testContext.failNow(ar.cause());
        }
      });
  }
}
