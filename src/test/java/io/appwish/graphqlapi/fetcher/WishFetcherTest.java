package io.appwish.graphqlapi.fetcher;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.graphqlapi.testutil.DummyData;
import io.appwish.graphqlapi.testutil.DummyFailureWishService;
import io.appwish.graphqlapi.testutil.DummyGRPCServer;
import io.appwish.graphqlapi.testutil.DummySuccessWishService;
import io.appwish.grpc.WishServiceGrpc;
import io.grpc.ManagedChannel;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxChannelBuilder;
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

  private static final String ID = "id";
  private static final String NOT_EXISTING_ID = "sadsadsafmfkmkasd";
  private static final String LOCALHOST = "localhost";
  private static final int FAILURE_CHANNEL_PORT = 7779;
  private static final int SUCCESS_CHANNEL_PORT = 7777;

  private DummyGRPCServer<DummySuccessWishService> dummySuccessWishServiceDummyGRPCServer;
  private DummyGRPCServer<DummyFailureWishService> dummyFailureWishServiceDummyGRPCServer;
  private ManagedChannel successChannel;
  private ManagedChannel failureChannel;
  private WishFetcher fetcher;

  @BeforeEach
  void set_up(final Vertx vertx, final VertxTestContext testContext) throws InstantiationException, IllegalAccessException, IOException {
    dummySuccessWishServiceDummyGRPCServer = new DummyGRPCServer<>(DummySuccessWishService.class, SUCCESS_CHANNEL_PORT);
    dummyFailureWishServiceDummyGRPCServer = new DummyGRPCServer<>(DummyFailureWishService.class, FAILURE_CHANNEL_PORT);
    dummySuccessWishServiceDummyGRPCServer.start();
    dummyFailureWishServiceDummyGRPCServer.start();

    successChannel = VertxChannelBuilder
      .forAddress(vertx, LOCALHOST, SUCCESS_CHANNEL_PORT)
      .usePlaintext(true)
      .build();

    failureChannel = VertxChannelBuilder
      .forAddress(vertx, LOCALHOST, FAILURE_CHANNEL_PORT)
      .usePlaintext(true)
      .build();

    fetcher = new WishFetcher(WishServiceGrpc.newVertxStub(successChannel));

    testContext.completeNow();
  }

  @AfterEach
  void tear_down() {
    successChannel.shutdownNow();
    failureChannel.shutdownNow();
    dummySuccessWishServiceDummyGRPCServer.stop();
    dummyFailureWishServiceDummyGRPCServer.stop();
  }

  @Test
  void should_return_all_wishes(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final DataFetchingEnvironment dataFetchingEnvironment = mock(DataFetchingEnvironment.class);

    // when
    fetcher.findAll(dataFetchingEnvironment)

      // then
      .whenComplete((wishes, throwable) -> testContext.verify(() -> {
        assertEquals(DummyData.WISHES, wishes);
        testContext.completeNow();
      }));
  }

  @Test
  void should_return_found_wish(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(DummyData.WISH_1.getId());

    // when
    fetcher.findOne(mock)

      // then
      .whenComplete((wish, throwable) -> testContext.verify(() -> {
        assertTrue(wish.isPresent());
        assertEquals(DummyData.WISH_1, wish.get());
        testContext.completeNow();
      }));
  }

  @Test
  void should_return_empty_when_wish_not_found(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(NOT_EXISTING_ID);

    // when
    fetcher.findOne(mock)

      // then
      .whenComplete((wish, throwable) -> testContext.verify(() -> {
        assertTrue(wish.isEmpty());
        testContext.completeNow();
      }));
  }

  @Test
  void should_pass_exception_on_find_one_error(final Vertx vertx, final VertxTestContext testContext) {
    // given
    final DataFetchingEnvironment mock = mock(DataFetchingEnvironment.class);
    when(mock.getArgument(ID)).thenReturn(DummyData.WISH_1.getId());

    // using failure path
    fetcher = new WishFetcher(WishServiceGrpc.newVertxStub(failureChannel));

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

    // using failure path
    fetcher = new WishFetcher(WishServiceGrpc.newVertxStub(failureChannel));

    // when
    fetcher.findAll(mock)

      // then
      .whenComplete((appWishes, throwable) -> testContext.verify(() -> {
        assertTrue(throwable instanceof RuntimeException);
        testContext.completeNow();
      }));
  }
}
