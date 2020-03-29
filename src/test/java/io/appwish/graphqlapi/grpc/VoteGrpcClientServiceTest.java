package io.appwish.graphqlapi.grpc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.appwish.graphqlapi.dto.input.VoteInput;
import io.appwish.graphqlapi.dto.query.VoteSelector;
import io.appwish.graphqlapi.dto.type.ItemType;
import io.appwish.graphqlapi.dto.type.VoteType;
import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.graphqlapi.eventbus.EventBusConfigurer;
import io.appwish.graphqlapi.testutil.DummyFailureVoteService;
import io.appwish.graphqlapi.testutil.DummyVoteService;
import io.appwish.graphqlapi.testutil.TestGrpcServer;
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
class VoteGrpcClientServiceTest {

  private static final VoteInput VOTE_INPUT = new VoteInput(1, ItemType.COMMENT, VoteType.UP);
  private static final VoteSelector VOTE_SELECTOR = new VoteSelector(123L, ItemType.COMMENT);
  private static final String GRPC_SERVER_HOST = "localhost";
  private static final int GRPC_SERVER_PORT = 7890;

  private VoteGrpcClientService service;
  private EventBus eventBus;
  private GrpcServiceStubsProvider stubsProvider;
  private TestGrpcServer server;

  @BeforeEach
  void setUp(final Vertx vertx, final VertxTestContext context) {
    final JsonObject config = new JsonObject().put("voteServiceHost", GRPC_SERVER_HOST).put("voteServicePort", GRPC_SERVER_PORT);
    eventBus = vertx.eventBus();
    stubsProvider = new GrpcServiceStubsProvider(vertx, config);
    final EventBusConfigurer configurer = new EventBusConfigurer(eventBus);
    configurer.registerCodecs();
    service = new VoteGrpcClientService(eventBus, stubsProvider.voteServiceStub());
    service.register();
    context.completeNow();
  }

  @AfterEach
  void tearDown() {
    server.stop();
  }

  @Test
  void should_add_vote(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromVoteservice();

    // when
    eventBus.request(Address.VOTE.get(), VOTE_INPUT, event -> {

      // then
      context.verify(() -> {
        assertTrue(event.succeeded());
        context.completeNow();
      });
    });
  }

  @Test
  void should_check_if_voted(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromVoteservice();

    // when
    eventBus.request(Address.HAS_VOTED.get(), VOTE_SELECTOR, event -> {

      // then
      context.verify(() -> {
        assertTrue(event.succeeded());
        context.completeNow();
      });
    });
  }

  @Test
  void should_unvote(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromVoteservice();

    // when
    eventBus.request(Address.UNVOTE.get(), VOTE_SELECTOR, event -> {

      // then
      context.verify(() -> {
        assertTrue(event.succeeded());
        context.completeNow();
      });
    });
  }

  @Test
  void should_check_score(final Vertx vertx, final VertxTestContext context) {
    // given
    mockSuccessfulResponsesFromVoteservice();

    // when
    eventBus.request(Address.VOTE_SCORE.get(), VOTE_SELECTOR, event -> {

      // then
      context.verify(() -> {
        assertTrue(event.succeeded());
        context.completeNow();
      });
    });
  }

  @Test
  void should_report_error_checking_score(final Vertx vertx, final VertxTestContext context) {
    // given
    mockUnuccessfulResponsesFromVoteservice();

    // when
    eventBus.request(Address.VOTE_SCORE.get(), VOTE_SELECTOR, event -> {

      // then
      context.verify(() -> {
        assertFalse(event.succeeded());
        context.completeNow();
      });
    });
  }

  @Test
  void should_report_error_unvoting(final Vertx vertx, final VertxTestContext context) {
    // given
    mockUnuccessfulResponsesFromVoteservice();

    // when
    eventBus.request(Address.UNVOTE.get(), VOTE_SELECTOR, event -> {

      // then
      context.verify(() -> {
        assertFalse(event.succeeded());
        context.completeNow();
      });
    });
  }

  @Test
  void should_report_error_checking_if_voted(final Vertx vertx, final VertxTestContext context) {
    // given
    mockUnuccessfulResponsesFromVoteservice();

    // when
    eventBus.request(Address.HAS_VOTED.get(), VOTE_SELECTOR, event -> {

      // then
      context.verify(() -> {
        assertFalse(event.succeeded());
        context.completeNow();
      });
    });
  }

  @Test
  void should_report_error_adding_vote(final Vertx vertx, final VertxTestContext context) {
    // given
    mockUnuccessfulResponsesFromVoteservice();

    // when
    eventBus.request(Address.VOTE.get(), VOTE_INPUT, event -> {

      // then
      context.verify(() -> {
        assertFalse(event.succeeded());
        context.completeNow();
      });
    });
  }

  private void mockSuccessfulResponsesFromVoteservice() {
    try {
      server = new TestGrpcServer<>(DummyVoteService.class, GRPC_SERVER_PORT);
      server.start();
    } catch (final Exception e) {
      throw new AssertionError("Server should always start", e);
    }
  }

  private void mockUnuccessfulResponsesFromVoteservice() {
    try {
      server = new TestGrpcServer<>(DummyFailureVoteService.class, GRPC_SERVER_PORT);
      server.start();
    } catch (final Exception e) {
      throw new AssertionError("Server should always start", e);
    }
  }
}
