package io.appwish.graphqlapi.graphql.fetcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.graphqlapi.eventbus.EventBusConfigurer;
import io.appwish.graphqlapi.testutil.TestData;
import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.AllWishReplyProto;
import io.appwish.grpc.UpdateWishInputProto;
import io.appwish.grpc.WishDeleteReplyProto;
import io.appwish.grpc.WishInputProto;
import io.appwish.grpc.WishProto;
import io.appwish.grpc.WishQueryProto;
import io.appwish.grpc.WishReplyProto;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class WishFetcherTest {

  private static final String ID = "id";
  private static final String INPUT = "input";
  private static final String SOME_TITLE = "someTitle";
  private static final String TITLE = "title";
  private static final String MARKDOWN = "markdown";
  private static final String SOME_MARKDOWN = "# Hello World! :)";
  private static final String COVER_IMAGE_URL = "coverImageUrl";
  private static final String SOME_URL = "someUrl";

  private WishFetcher fetcher;
  private EventBus eventBus;

  @BeforeEach
  void setUp(final Vertx vertx, final VertxTestContext context) {
    eventBus = vertx.eventBus();
    final EventBusConfigurer eventBusConfigurer = new EventBusConfigurer(eventBus);
    eventBusConfigurer.registerCodecs();
    fetcher = new WishFetcher(eventBus);
    context.completeNow();
  }

  @Test
  void should_fetch_a_wish_list(final Vertx vertx, final VertxTestContext context) {
    // given
    final AllWishQueryProto query = AllWishQueryProto.newBuilder().build();
    final AllWishReplyProto expected = AllWishReplyProto.newBuilder().addAllWishes(TestData.WISHES).build();
    eventBus.<AllWishQueryProto>consumer(Address.ALL_WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.reply(expected);
      } else {
        context.failNow(new AssertionError("It should be equal to query"));
      }
    });

    // when
    final CompletionStage<List<WishProtoWrapper>> action = fetcher.allWish(mock(DataFetchingEnvironment.class));

    // then
    action.whenComplete((wishProtos, throwable) -> {
      context.verify(() -> {
        assertEquals(expected.getWishesList(), wishProtos);
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_error_on_error_fetching_wish_list(final Vertx vertx, final VertxTestContext context) {
    // given
    final AllWishQueryProto query = AllWishQueryProto.newBuilder().build();
    eventBus.<AllWishQueryProto>consumer(Address.ALL_WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.fail(1, "This time we test failure");
      } else {
        context.failNow(new AssertionError("It should be equal to query"));
      }
    });

    // when
    final CompletionStage<List<WishProtoWrapper>> action = fetcher.allWish(mock(DataFetchingEnvironment.class));

    // then
    action.whenComplete((wishProtos, throwable) -> {
      context.verify(() -> {
        assertNull(wishProtos);
        assertTrue(throwable instanceof ReplyException);
        context.completeNow();
      });
    });
  }

  @Test
  void should_fetch_a_wish(final Vertx vertx, final VertxTestContext context) {
    // given
    final WishQueryProto query = WishQueryProto.newBuilder().setId(TestData.WISH_1.getId()).build();
    final WishReplyProto expected = WishReplyProto.newBuilder().setWish(TestData.WISH_1).build();
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(String.valueOf(TestData.WISH_1.getId()));
    eventBus.<WishQueryProto>consumer(Address.WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.reply(expected);
      } else {
        context.failNow(new AssertionError("It should be equal to query"));
      }
    });

    // when
    final CompletionStage<WishProtoWrapper> action = fetcher.wish(mock);

    // then
    action.whenComplete((wishProto, throwable) -> {
      context.verify(() -> {
        assertEquals(expected.getWish(), wishProto);
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_empty_if_could_not_find_wish(final Vertx vertx,
    final VertxTestContext context) {
    // given
    final WishQueryProto query = WishQueryProto.newBuilder().setId(TestData.WISH_1.getId()).build();
    final WishReplyProto empty = WishReplyProto.newBuilder().build();
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(String.valueOf(TestData.WISH_1.getId()));
    eventBus.<WishQueryProto>consumer(Address.WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.reply(empty);
      } else {
        context.failNow(new AssertionError("It should be equal to query"));
      }
    });

    // when
    final CompletionStage<WishProtoWrapper> action = fetcher.wish(mock);

    // then
    action.whenComplete((wishProto, throwable) -> {
      context.verify(() -> {
        assertNull(wishProto);
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_error_if_fetch_wish_failed(final Vertx vertx, final VertxTestContext context) {
    // given
    final WishQueryProto query = WishQueryProto.newBuilder().setId(TestData.WISH_1.getId()).build();
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(String.valueOf(TestData.WISH_1.getId()));
    eventBus.<WishQueryProto>consumer(Address.WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.fail(1, "This time we test a failure of event");
      } else {
        context.failNow(new AssertionError("It should be equal to query"));
      }
    });

    // when
    final CompletionStage<WishProtoWrapper> action = fetcher.wish(mock);

    // then
    action.whenComplete((wishProto, throwable) -> {
      context.verify(() -> {
        assertTrue(throwable instanceof ReplyException);
        context.completeNow();
      });
    });
  }

  @Test
  void should_create_and_return_created_wish(final Vertx vertx, final VertxTestContext context) {
    // given
    final DataFetchingEnvironment dataFetchingEnvironment = mock(DataFetchingEnvironment.class);
    final Map<String, String> input = Map.of(TITLE, SOME_TITLE, MARKDOWN, SOME_MARKDOWN, COVER_IMAGE_URL, SOME_URL);
    final WishInputProto query = WishInputProto.newBuilder()
      .setTitle(SOME_TITLE)
      .setCoverImageUrl(SOME_URL)
      .setMarkdown(SOME_MARKDOWN).build();
    when(dataFetchingEnvironment.getArgument(INPUT)).thenReturn(input);
    eventBus.<WishInputProto>consumer(Address.CREATE_WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.reply(WishReplyProto.newBuilder().setWish(TestData.WISH_1).build());
      } else {
        context.failNow(new AssertionError("It should pass correct query"));
      }
    });

    // when
    final CompletionStage<WishProtoWrapper> action = fetcher.createWish(dataFetchingEnvironment);

    // then
    action.whenComplete((wishProto, throwable) -> {
      context.verify(() -> {
        assertEquals(TestData.WISH_1, wishProto);
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_error_if_creating_wish_event_failed(final Vertx vertx, final VertxTestContext context) {
    // given
    final DataFetchingEnvironment dataFetchingEnvironment = mock(DataFetchingEnvironment.class);
    final Map<String, String> input = Map.of(TITLE, SOME_TITLE, MARKDOWN, SOME_MARKDOWN, COVER_IMAGE_URL, SOME_URL);
    final WishInputProto query = WishInputProto.newBuilder()
      .setTitle(SOME_TITLE)
      .setCoverImageUrl(SOME_URL)
      .setMarkdown(SOME_MARKDOWN).build();
    when(dataFetchingEnvironment.getArgument(INPUT)).thenReturn(input);
    eventBus.<WishInputProto>consumer(Address.CREATE_WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.fail(1, "Now we test event failure");
      } else {
        context.failNow(new AssertionError("It should pass correct query"));
      }
    });

    // when
    final CompletionStage<WishProtoWrapper> action = fetcher.createWish(dataFetchingEnvironment);

    // then
    action.whenComplete((wishProto, throwable) -> {
      context.verify(() -> {
        assertTrue(throwable instanceof ReplyException);
        context.completeNow();
      });
    });
  }

  @Test
  void should_update_wish_and_return_it(final Vertx vertx, final VertxTestContext context) {
    // given
    final DataFetchingEnvironment dataFetchingEnvironment = mock(DataFetchingEnvironment.class);
    final Map<String, String> input = Map.of(TITLE, SOME_TITLE, MARKDOWN, SOME_MARKDOWN, COVER_IMAGE_URL, SOME_URL);
    final UpdateWishInputProto query = UpdateWishInputProto.newBuilder()
      .setTitle(SOME_TITLE)
      .setId(TestData.WISH_1.getId())
      .setCoverImageUrl(SOME_URL)
      .setMarkdown(SOME_MARKDOWN).build();
    when(dataFetchingEnvironment.getArgument(INPUT)).thenReturn(input);
    when(dataFetchingEnvironment.getArgument(ID)).thenReturn(String.valueOf(TestData.WISH_1.getId()));
    eventBus.<UpdateWishInputProto>consumer(Address.UPDATE_WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.reply(WishReplyProto.newBuilder().setWish(TestData.WISH_1).build());
      } else {
        context.failNow(new AssertionError("It should pass correct query"));
      }
    });

    // when
    final CompletionStage<WishProtoWrapper> action = fetcher.updateWish(dataFetchingEnvironment);

    // then
    action.whenComplete((wishProto, throwable) -> {
      context.verify(() -> {
        assertEquals(TestData.WISH_1, wishProto);
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_empty_when_wish_to_update_doesnt_exist(final Vertx vertx, final VertxTestContext context) {
    // given
    final DataFetchingEnvironment dataFetchingEnvironment = mock(DataFetchingEnvironment.class);
    final Map<String, String> input = Map.of(TITLE, SOME_TITLE, MARKDOWN, SOME_MARKDOWN, COVER_IMAGE_URL, SOME_URL);
    final UpdateWishInputProto query = UpdateWishInputProto.newBuilder()
      .setTitle(SOME_TITLE)
      .setId(TestData.WISH_1.getId())
      .setCoverImageUrl(SOME_URL)
      .setMarkdown(SOME_MARKDOWN).build();
    when(dataFetchingEnvironment.getArgument(INPUT)).thenReturn(input);
    when(dataFetchingEnvironment.getArgument(ID)).thenReturn(String.valueOf(TestData.WISH_1.getId()));
    eventBus.<UpdateWishInputProto>consumer(Address.UPDATE_WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.reply(WishReplyProto.newBuilder().build());
      } else {
        context.failNow(new AssertionError("It should pass correct query"));
      }
    });

    // when
    final CompletionStage<WishProtoWrapper> action = fetcher.updateWish(dataFetchingEnvironment);

    // then
    action.whenComplete((wishProto, throwable) -> {
      context.verify(() -> {
        assertNull(wishProto);
        context.completeNow();
      });
    });
  }

  @Test
  void should_delete_a_wish_and_return_true(final Vertx vertx, final VertxTestContext context) {
    // given
    final WishQueryProto query = WishQueryProto.newBuilder().setId(TestData.WISH_1.getId()).build();
    final WishDeleteReplyProto reply = WishDeleteReplyProto.newBuilder().setDeleted(true).build();
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(String.valueOf(TestData.WISH_1.getId()));
    eventBus.<WishQueryProto>consumer(Address.DELETE_WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.reply(reply);
      } else {
        context.failNow(new AssertionError("It should be equal to query"));
      }
    });

    // when
    final CompletionStage<Boolean> action = fetcher.deleteWish(mock);

    // then
    action.whenComplete((deleted, throwable) -> {
      context.verify(() -> {
        assertEquals(true, deleted);
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_false_if_wish_not_deleted(final Vertx vertx, final VertxTestContext context) {
    // given
    final WishQueryProto query = WishQueryProto.newBuilder().setId(TestData.WISH_1.getId()).build();
    final WishDeleteReplyProto reply = WishDeleteReplyProto.newBuilder().setDeleted(false).build();
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(String.valueOf(TestData.WISH_1.getId()));
    eventBus.<WishQueryProto>consumer(Address.DELETE_WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.reply(reply);
      } else {
        context.failNow(new AssertionError("It should be equal to query"));
      }
    });

    // when
    final CompletionStage<Boolean> action = fetcher.deleteWish(mock);

    // then
    action.whenComplete((deleted, throwable) -> {
      context.verify(() -> {
        assertEquals(false, deleted);
        context.completeNow();
      });
    });
  }

  @Test
  void should_return_error_on_error_deleting_wish(final Vertx vertx, final VertxTestContext context) {
    // given
    final WishQueryProto query = WishQueryProto.newBuilder().setId(TestData.WISH_1.getId()).build();
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(String.valueOf(TestData.WISH_1.getId()));
    eventBus.<WishQueryProto>consumer(Address.DELETE_WISH.get(), event -> {
      if (event.body().equals(query)) {
        event.fail(1, "We test error case not");
      } else {
        context.failNow(new AssertionError("It should be equal to query"));
      }
    });

    // when
    final CompletionStage<Boolean> action = fetcher.deleteWish(mock);

    // then
    action.whenComplete((deleted, throwable) -> {
      context.verify(() -> {
        assertTrue(throwable instanceof ReplyException);
        context.completeNow();
      });
    });
  }
}
