package io.appwish.graphqlapi.grpc;

import static io.appwish.graphqlapi.testutil.TestData.INVALID_ID;
import static io.appwish.graphqlapi.testutil.TestData.SOME_MARKDOWN;
import static io.appwish.graphqlapi.testutil.TestData.SOME_TITLE;
import static io.appwish.graphqlapi.testutil.TestData.SOME_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.graphqlapi.eventbus.EventBusConfigurer;
import io.appwish.graphqlapi.testutil.DummyFailureWishService;
import io.appwish.graphqlapi.testutil.TestGrpcServer;
import io.appwish.graphqlapi.testutil.DummyWishService;
import io.appwish.graphqlapi.testutil.TestData;
import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.AllWishReplyProto;
import io.appwish.grpc.UpdateWishInputProto;
import io.appwish.grpc.WishDeleteReplyProto;
import io.appwish.grpc.WishInputProto;
import io.appwish.grpc.WishQueryProto;
import io.appwish.grpc.WishReplyProto;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class WishGrpcClientServiceTest {

  private static final String GRPC_SERVER_HOST = "localhost";
  private static final int GRPC_SERVER_PORT = 7890;

  private WishGrpcClientService service;
  private EventBus eventBus;
  private GrpcServiceStubsProvider stubsProvider;
  private TestGrpcServer server;

  @BeforeEach
  void setUp(final Vertx vertx, final VertxTestContext context) {
    final JsonObject config = new JsonObject()
      .put("wishServiceHost", GRPC_SERVER_HOST)
      .put("wishServicePort", GRPC_SERVER_PORT);
    eventBus = vertx.eventBus();
    stubsProvider = new GrpcServiceStubsProvider(vertx, config);
    final EventBusConfigurer configurer = new EventBusConfigurer(eventBus);
    configurer.registerCodecs();
    service = new WishGrpcClientService(eventBus, stubsProvider.wishServiceStub());
    service.register();
    context.completeNow();
  }

  @AfterEach
  void tearDown() {
    server.stop();
  }

  @Test
  void should_reply_with_a_list_of_wish(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromWishservice();
    final AllWishQueryProto query = AllWishQueryProto.newBuilder().build();

    // when
    eventBus.<AllWishReplyProto>request(Address.ALL_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.succeeded());
        assertEquals(event.result().body().getWishesList(), TestData.WISHES);
        context.completeNow();
      });
    });
  }

  @Test
  void should_reply_with_error_if_could_not_fetch_wishes(final Vertx vertx, final VertxTestContext context) {
    // given
    final AllWishQueryProto query = AllWishQueryProto.newBuilder().build();
    mockUnuccessfulResponsesFromWishservice();

    // when
    eventBus.<AllWishReplyProto>request(Address.ALL_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.failed());
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_found_wish(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromWishservice();
    final WishQueryProto query = WishQueryProto.newBuilder().setId(TestData.WISH_1.getId()).build();

    // when
    eventBus.<WishReplyProto>request(Address.WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.succeeded());
        assertEquals(event.result().body().getWish(), TestData.WISH_1);
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_empty_if_wish_not_found(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromWishservice();
    final WishQueryProto query = WishQueryProto.newBuilder().setId(INVALID_ID).build();

    // when
    eventBus.<WishReplyProto>request(Address.WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.succeeded());
        assertFalse(event.result().body().hasWish());
        context.completeNow();
      });
    });
  }

  @Test
  void should_reply_error_on_error_fetching_wish(final Vertx vertx, final VertxTestContext context) {
    // given
    mockUnuccessfulResponsesFromWishservice();
    final WishQueryProto query = WishQueryProto.newBuilder().setId(INVALID_ID).build();

    // when
    eventBus.<WishReplyProto>request(Address.WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.failed());
        context.completeNow();
      });
    });
  }

  @Test
  void should_create_and_return_created_wish_data(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromWishservice();
    final WishInputProto query = WishInputProto.newBuilder()
      .setMarkdown(SOME_MARKDOWN)
      .setCoverImageUrl(SOME_URL)
      .setTitle(SOME_TITLE).build();

    // when
    eventBus.<WishReplyProto>request(Address.CREATE_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.succeeded());
        assertEquals(SOME_MARKDOWN, event.result().body().getWish().getMarkdown());
        assertEquals(SOME_TITLE, event.result().body().getWish().getTitle());
        assertEquals(SOME_URL, event.result().body().getWish().getCoverImageUrl());
        context.completeNow();
      });
    });
  }

  @Test
  void should_reply_error_on_error_creating_wish(final Vertx vertx,
    final VertxTestContext context) {
    // given
    mockUnuccessfulResponsesFromWishservice();
    final WishInputProto query = WishInputProto.newBuilder()
      .setMarkdown(SOME_MARKDOWN)
      .setCoverImageUrl(SOME_URL)
      .setTitle(SOME_TITLE).build();

    // when
    eventBus.<WishReplyProto>request(Address.CREATE_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.failed());
        context.completeNow();
      });
    });
  }

  @Test
  void should_update_and_return_updated_wish(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromWishservice();
    final UpdateWishInputProto query = UpdateWishInputProto.newBuilder()
      .setMarkdown(SOME_MARKDOWN)
      .setCoverImageUrl(SOME_URL)
      .setId(TestData.WISH_1.getId())
      .setTitle(SOME_TITLE).build();

    // when
    eventBus.<WishReplyProto>request(Address.UPDATE_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.succeeded());
        assertEquals(SOME_MARKDOWN, event.result().body().getWish().getMarkdown());
        assertEquals(SOME_TITLE, event.result().body().getWish().getTitle());
        assertEquals(SOME_URL, event.result().body().getWish().getCoverImageUrl());
        assertEquals(TestData.WISH_1.getId(), event.result().body().getWish().getId());
        context.completeNow();
      });
    });
  }

  @Test
  void should_not_update_and_return_empty_when_wish_not_found(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromWishservice();
    final UpdateWishInputProto query = UpdateWishInputProto.newBuilder()
      .setMarkdown(SOME_MARKDOWN)
      .setCoverImageUrl(SOME_URL)
      .setId(INVALID_ID)
      .setTitle(SOME_TITLE).build();

    // when
    eventBus.<WishReplyProto>request(Address.UPDATE_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.succeeded());
        assertFalse(event.result().body().hasWish());
        context.completeNow();
      });
    });
  }

  @Test
  void should_reply_error_when_error_updating_wish(final Vertx vertx, final VertxTestContext context) {
    // given
    mockUnuccessfulResponsesFromWishservice();
    final UpdateWishInputProto query = UpdateWishInputProto.newBuilder()
      .setMarkdown(SOME_MARKDOWN)
      .setCoverImageUrl(SOME_URL)
      .setId(INVALID_ID)
      .setTitle(SOME_TITLE).build();

    // when
    eventBus.<WishReplyProto>request(Address.UPDATE_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.failed());
        context.completeNow();
      });
    });
  }

  @Test
  void should_delete_and_return_true_when_wish_found(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromWishservice();
    final WishQueryProto query = WishQueryProto.newBuilder().setId(TestData.WISH_1.getId()).build();

    // when
    eventBus.<WishDeleteReplyProto>request(Address.DELETE_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.succeeded());
        assertTrue(event.result().body().getDeleted());
        context.completeNow();
      });
    });
  }

  @Test
  void should_not_delete_and_return_false_if_wish_not_found(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromWishservice();
    final WishQueryProto query = WishQueryProto.newBuilder().setId(132141244).build();

    // when
    eventBus.<WishDeleteReplyProto>request(Address.DELETE_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.succeeded());
        assertFalse(event.result().body().getDeleted());
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_error_if_error_deleting_wish(final Vertx vertx, final VertxTestContext context) {
    // given
    mockUnuccessfulResponsesFromWishservice();
    final WishQueryProto query = WishQueryProto.newBuilder().setId(INVALID_ID).build();

    // when
    eventBus.<WishDeleteReplyProto>request(Address.DELETE_WISH.get(), query, event -> {

      //then
      context.verify(() -> {
        assertTrue(event.failed());
        context.completeNow();
      });
    });
  }

  private void mockSuccessfulResponsesFromWishservice() {
    try {
      server = new TestGrpcServer<>(DummyWishService.class, GRPC_SERVER_PORT);
      server.start();
    } catch (final Exception e) {
      throw new AssertionError("Server should always start", e);
    }
  }

  private void mockUnuccessfulResponsesFromWishservice() {
    try {
      server = new TestGrpcServer<>(DummyFailureWishService.class, GRPC_SERVER_PORT);
      server.start();
    } catch (final Exception e) {
      throw new AssertionError("Server should always start", e);
    }
  }
}
