package io.appwish.graphqlapi;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class MainVerticleTest {
  @Test
  void should_deploy_all_succesfully(final Vertx vertx, final VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(event -> testContext.completeNow()));
  }
}
