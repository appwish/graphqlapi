package io.appwish.graphqlapi;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class MainVerticleTest {

  private MainVerticle verticle;

  @Test
  void should_deploy_all_succesfully(final Vertx vertx, final VertxTestContext testContext) {
    verticle = new MainVerticle();
    vertx.deployVerticle(verticle, testContext.succeeding(event -> testContext.completeNow()));
  }

  @AfterEach
  void sec(final Vertx vertx, final VertxTestContext testContext) {
    vertx.undeploy(verticle.deploymentID(), testContext.succeeding(event -> testContext.completeNow()));
  }
}
