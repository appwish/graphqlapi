package io.appwish.graphqlapi.verticle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.graphqlapi.eventbus.EventBusConfigurer;
import io.appwish.graphqlapi.grpc.GrpcServiceStubsProvider;
import io.appwish.graphqlapi.testutil.DummyWishService;
import io.appwish.graphqlapi.testutil.TestData;
import io.appwish.graphqlapi.testutil.TestGrpcServer;
import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.AllWishReplyProto;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class GrpcClientVerticleTest {

  private static final int WISH_SERVICE_PORT = 9999;
  private static final String WISH_SERVICE_HOST = "localhost";

  private TestGrpcServer server;

  @Test
  void should_deploy_verticle(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final JsonObject config = new JsonObject()
      .put("wishServicePort", WISH_SERVICE_PORT)
      .put("wishServiceHost", WISH_SERVICE_HOST);
    final GrpcServiceStubsProvider stubsProvider = new GrpcServiceStubsProvider(vertx, config);
    final GrpcClientVerticle verticle = new GrpcClientVerticle(stubsProvider);

    // when
    vertx.deployVerticle(verticle,

      // then
      testContext.succeeding(it -> testContext.completeNow()));
  }

  @Test
  void shuld_expose_wish_service_stub_on_event_bus(final Vertx vertx, final VertxTestContext testContext) {
    // given
    mockSuccessfulResponsesFromWishservice();
    final JsonObject config = new JsonObject()
      .put("wishServicePort", WISH_SERVICE_PORT)
      .put("wishServiceHost", WISH_SERVICE_HOST);
    final GrpcServiceStubsProvider stubsProvider = new GrpcServiceStubsProvider(vertx, config);
    final GrpcClientVerticle verticle = new GrpcClientVerticle(stubsProvider);
    final EventBusConfigurer eventBusConfigurer = new EventBusConfigurer(vertx.eventBus());
    final AllWishQueryProto query = AllWishQueryProto.newBuilder().build();
    eventBusConfigurer.registerCodecs();
    vertx.deployVerticle(verticle, event -> {

      // when
      vertx.eventBus().<AllWishReplyProto>request(Address.ALL_WISH.get(), query, res -> {

          // then
          testContext.verify(() -> {
            assertTrue(res.succeeded());
            assertEquals(TestData.WISHES, res.result().body().getWishesList());
            testContext.completeNow();
          });
        });
    });
  }

  private void mockSuccessfulResponsesFromWishservice() {
    try {
      server = new TestGrpcServer<>(DummyWishService.class, WISH_SERVICE_PORT);
      server.start();
    } catch (Exception e) {
      throw new AssertionError("Server should always start", e);
    }
  }
}
