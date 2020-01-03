package io.appwish.graphqlapi.fetcher;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.graphqlapi.testutil.InProcessServer;
import io.appwish.graphqlapi.testutil.DummyData;
import io.appwish.graphqlapi.testutil.DummySuccessfulWishService;
import io.appwish.graphqlapi.testutil.DummyUnsuccessfulWishService;
import io.appwish.grpc.AppWishServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(VertxExtension.class)
class WishFetcherTest {

  private static final String SUCCESS_TESTS = "successTests";
  private static final String FAILURE_TESTS = "failureTests";
  private static final String ID = "id";
  private static final String NOT_EXISTING_ID = "sadsadsafmfkmkasd";

  private InProcessServer<DummySuccessfulWishService> dummySuccessfulWishServiceInProcessServer;
  private InProcessServer<DummyUnsuccessfulWishService> dummyUnsuccessfulWishServiceInProcessServer;
  private ManagedChannel successfulChannel;
  private ManagedChannel unsuccessfulChannel;
  private WishFetcher fetcher;

  @BeforeEach
  void set_up() throws InstantiationException, IllegalAccessException, IOException {
    dummySuccessfulWishServiceInProcessServer = new InProcessServer<>(DummySuccessfulWishService.class);
    dummyUnsuccessfulWishServiceInProcessServer = new InProcessServer<>(DummyUnsuccessfulWishService.class);
    dummySuccessfulWishServiceInProcessServer.start(SUCCESS_TESTS);
    dummyUnsuccessfulWishServiceInProcessServer.start(FAILURE_TESTS);
    successfulChannel = InProcessChannelBuilder
      .forName(SUCCESS_TESTS)
      .directExecutor()
      .usePlaintext(true)
      .build();
    unsuccessfulChannel = InProcessChannelBuilder
      .forName(FAILURE_TESTS)
      .directExecutor()
      .usePlaintext(true)
      .build();
    fetcher = new WishFetcher(AppWishServiceGrpc.newVertxStub(successfulChannel));
  }

  @AfterEach
  void tear_down() {
    successfulChannel.shutdownNow();
    unsuccessfulChannel.shutdownNow();
    dummySuccessfulWishServiceInProcessServer.stop();
    dummyUnsuccessfulWishServiceInProcessServer.stop();
  }

  @Test
  void should_return_all_app_wishes(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final DataFetchingEnvironment dataFetchingEnvironment = mock(DataFetchingEnvironment.class);

    // when
    fetcher.findAll(dataFetchingEnvironment)

      // then
      .whenComplete((appWishes, throwable) -> testContext.verify(() -> {
        assertEquals(DummyData.APP_WISH_LIST.getAppWishList(), appWishes);
        testContext.completeNow();
      }));
  }

  @Test
  void should_return_found_app_wish(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(DummyData.APP_WISH_1.getId());

    // when
    fetcher.findOne(mock)

      // then
      .whenComplete((appWish, throwable) -> testContext.verify(() -> {
        assertTrue(appWish.isPresent());
        assertEquals(DummyData.APP_WISH_1, appWish.get());
        testContext.completeNow();
      }));
  }

  @Test
  void should_return_empty_when_app_wish_not_found(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(NOT_EXISTING_ID);

    // when
    fetcher.findOne(mock)

      // then
      .whenComplete((appWishes, throwable) -> testContext.verify(() -> {
        assertTrue(appWishes.isEmpty());
        testContext.completeNow();
      }));
  }

  @Test
  void should_pass_exception_on_find_one_error(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    fetcher = new WishFetcher(AppWishServiceGrpc.newVertxStub(unsuccessfulChannel));
    when(mock.getArgument(ID)).thenReturn(DummyData.APP_WISH_1.getId());

    // when
    fetcher.findOne(mock)

      // then
      .whenComplete((appWishes, throwable) -> testContext.verify(() -> {
        assertTrue(throwable instanceof RuntimeException);
        testContext.completeNow();
      }));
  }

  @Test
  void should_pass_exception_on_find_all_error(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    fetcher = new WishFetcher(AppWishServiceGrpc.newVertxStub(unsuccessfulChannel));

    // when
    fetcher.findAll(mock)

      // then
      .whenComplete((appWishes, throwable) -> testContext.verify(() -> {
        assertTrue(throwable instanceof RuntimeException);
        testContext.completeNow();
      }));
  }
}
