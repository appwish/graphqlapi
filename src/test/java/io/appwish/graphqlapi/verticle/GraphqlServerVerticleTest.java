package io.appwish.graphqlapi.verticle;

import static io.appwish.graphqlapi.verticle.GraphqlServerVerticle.GRAPHQL_ROUTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class GraphqlServerVerticleTest {

  private static final int APP_PORT = 9797;
  private static final String SCHEMA_DIR = "schema";
  private static final String DEV = "dev";
  private static final String PROD = "prod";
  private static final String APP_PORT_ENV = "appPort";
  private static final String SCHEMA_DIR_PATH_ENV = "schemaDirPath";
  private static final String ENV_ENV = "env";

  private GraphqlServerVerticle verticle;

  @AfterEach
  void sec(final Vertx vertx, final VertxTestContext testContext) {
    vertx.undeploy(verticle.deploymentID(), testContext.succeeding(event -> testContext.completeNow()));
  }

  @Test
  void should_deploy_verticle(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final JsonObject config = new JsonObject()
      .put(APP_PORT_ENV, APP_PORT)
      .put(SCHEMA_DIR_PATH_ENV, SCHEMA_DIR)
      .put(ENV_ENV, PROD);
    verticle = new GraphqlServerVerticle(config);

    // when
    vertx.deployVerticle(verticle,

      // then
      testContext.succeeding(it -> testContext.completeNow()));
  }

  @Test
  void should_expose_graphql_endpoint(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final WebClientOptions options = new WebClientOptions().setDefaultPort(APP_PORT);
    final WebClient client = WebClient.create(vertx, options);
    final JsonObject request = new JsonObject().put("query", "query { allWish { id } }");
    final JsonObject config = new JsonObject()
      .put(APP_PORT_ENV, APP_PORT)
      .put(SCHEMA_DIR_PATH_ENV, SCHEMA_DIR)
      .put(ENV_ENV, PROD);
    verticle = new GraphqlServerVerticle(config);

    vertx.deployVerticle(verticle, event -> {

      // when
      client.post(GRAPHQL_ROUTE).as(BodyCodec.jsonObject()).sendJsonObject(request, ar -> {

        // then
        if (ar.succeeded()) {
          final JsonObject response = ar.result().body();
          assertNotNull(response);
          assertEquals(HttpResponseStatus.OK.code(), ar.result().statusCode());
          testContext.completeNow();
        } else {
          testContext.failNow(ar.cause());
        }
      });
    });
  }

  @Test
  void should_expose_graphiql_endpoint_dev_env(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final WebClientOptions options = new WebClientOptions().setDefaultPort(APP_PORT);
    final WebClient client = WebClient.create(vertx, options);
    final JsonObject config = new JsonObject()
      .put(APP_PORT_ENV, APP_PORT)
      .put(SCHEMA_DIR_PATH_ENV, SCHEMA_DIR)
      .put(ENV_ENV, DEV);
    verticle = new GraphqlServerVerticle(config);

    vertx.deployVerticle(verticle, event -> {

      // when
      client.get("/graphiql/")
        .as(BodyCodec.string())
        .send(it -> {

          // then
          testContext.verify(() -> {
            assertTrue(it.succeeded());
            assertEquals(HttpResponseStatus.OK.code(), it.result().statusCode());
            testContext.completeNow();
          });
        });
    });
  }

  @Test
  void should_not_expose_graphiql_endpoint_when_not_dev_environment(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final WebClientOptions options = new WebClientOptions().setDefaultPort(APP_PORT);
    final WebClient client = WebClient.create(vertx, options);
    final JsonObject config = new JsonObject()
      .put(APP_PORT_ENV, APP_PORT)
      .put(SCHEMA_DIR_PATH_ENV, SCHEMA_DIR)
      .put(ENV_ENV, PROD);
    verticle = new GraphqlServerVerticle(config);

    vertx.deployVerticle(verticle, event -> {

      // when
      client.get("/graphiql/")
        .as(BodyCodec.string())
        .send(it -> {

          // then
          testContext.verify(() -> {
            assertTrue(it.succeeded());
            assertEquals(HttpResponseStatus.NOT_FOUND.code(), it.result().statusCode());
            testContext.completeNow();
          });
        });
    });
  }
}
