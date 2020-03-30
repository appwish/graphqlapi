package io.appwish.graphqlapi.graphql.fetcher;

import static java.util.Objects.isNull;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.graphqlapi.dto.User;
import io.appwish.graphqlapi.dto.input.VoteInput;
import io.appwish.graphqlapi.dto.query.VoteSelector;
import io.appwish.graphqlapi.dto.reply.HasVotedReply;
import io.appwish.graphqlapi.dto.reply.UnvoteReply;
import io.appwish.graphqlapi.dto.reply.VoteReply;
import io.appwish.graphqlapi.dto.reply.VoteScoreReply;
import io.appwish.graphqlapi.dto.type.ItemType;
import io.appwish.graphqlapi.dto.type.VoteType;
import io.appwish.graphqlapi.eventbus.Address;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Sends events on the event bus to resolve needs of vote-related part of GraphQL schema.
 */
public class VoteFetcher {

  private static final String ITEM_ID = "itemId";
  private static final String INPUT = "input";
  private static final String ITEM_TYPE = "itemType";
  private static final String VOTE_TYPE = "voteType";
  private static final String USER_ID = "userId";
  private static final String USER = "user";
  public static final String SELECTOR = "selector";

  private final EventBus eventBus;

  public VoteFetcher(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public CompletionStage<VoteReply> vote(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<VoteReply> completableFuture = new CompletableFuture<>();
    final Map<String, String> input = dataFetchingEnvironment.getArgument(INPUT);
    final String itemId = input.get(ITEM_ID);
    final String itemType = input.get(ITEM_TYPE);
    final String voteType = input.get(VOTE_TYPE);
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);
    final VoteInput voteInput = new VoteInput(Long.parseLong(itemId), ItemType.valueOf(itemType), VoteType.valueOf(voteType));

    eventBus.<VoteReply>request(Address.VOTE.get(), voteInput, options, event -> {
      if (event.succeeded() && !isNull(event.result().body())) {
        event.result().body().setVoter(new User(event.result().body().getVote().getUserId()));
        completableFuture.complete(event.result().body());
      } else if (event.succeeded()) {
        completableFuture.completeExceptionally(new AssertionError("Should never happen"));
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }

  public CompletionStage<Boolean> unvote(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
    final Map<String, String> query = dataFetchingEnvironment.getArgument(SELECTOR);
    final long itemId = Long.parseLong(query.get(ITEM_ID));
    final ItemType itemType = ItemType.valueOf(query.get(ITEM_TYPE));
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);
    final VoteSelector selector = new VoteSelector(itemId, itemType);

    eventBus.<UnvoteReply>request(Address.UNVOTE.get(), selector, options, event -> {
      if (event.succeeded() && event.result().body().isDeleted()) {
        completableFuture.complete(true);
      } else if (event.succeeded()) {
        completableFuture.complete(false);
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }

  public CompletionStage<Boolean> hasVoted(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
    final Map<String, String> query = dataFetchingEnvironment.getArgument(SELECTOR);
    final long itemId = Long.parseLong(query.get(ITEM_ID));
    final ItemType itemType = ItemType.valueOf(query.get(ITEM_TYPE));
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);
    final VoteSelector selector = new VoteSelector(itemId, itemType);

    eventBus.<HasVotedReply>request(Address.HAS_VOTED.get(), selector, options, event -> {
      if (event.succeeded() && event.result().body().isVoted()) {
        completableFuture.complete(true);
      } else if (event.succeeded()) {
        completableFuture.complete(false);
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }

  public CompletionStage<VoteScoreReply> voteScore(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<VoteScoreReply> completableFuture = new CompletableFuture<>();
    final Map<String, String> query = dataFetchingEnvironment.getArgument(SELECTOR);
    final long itemId = Long.parseLong(query.get(ITEM_ID));
    final ItemType itemType = ItemType.valueOf(query.get(ITEM_TYPE));
    final VoteSelector selector = new VoteSelector(itemId, itemType);

    eventBus.<VoteScoreReply>request(Address.VOTE_SCORE.get(), selector, event -> {
      if (event.succeeded()) {
        completableFuture.complete(event.result().body());
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }

  private String getUserIdFrom(final DataFetchingEnvironment environment) {
    try {
      final RoutingContext context = environment.getLocalContext();
      final JsonObject user = context.get(USER);
      return user.getString(USER_ID);
    } catch (final NullPointerException e) {
      return null;
    }
  }
}
