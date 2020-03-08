package io.appwish.graphqlapi.graphql.fetcher;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.grpc.ItemTypeProto;
import io.appwish.grpc.VoteInputProto;
import io.appwish.grpc.VoteReplyProto;
import io.appwish.grpc.VoteTypeProto;
import io.vertx.core.eventbus.EventBus;
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

  private final EventBus eventBus;

  public VoteFetcher(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public CompletionStage<VoteProtoWrapper> vote(
    final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<VoteProtoWrapper> completableFuture = new CompletableFuture<>();
    final Map<String, String> input = dataFetchingEnvironment.getArgument(INPUT);
    final String itemId = input.get(ITEM_ID);
    final String itemType = input.get(ITEM_TYPE);
    final String voteType = input.get(VOTE_TYPE);

    final VoteInputProto voteInput = VoteInputProto.newBuilder()
      .setItemId(Long.valueOf(itemId))
      .setItemType(ItemTypeProto.valueOf(itemType)
      ).setVoteType(VoteTypeProto.valueOf(voteType)).build();

    eventBus.<VoteReplyProto>request(Address.VOTE.get(), voteInput, event -> {
      if (event.succeeded() && event.result().body().hasVote()) {
        completableFuture.complete(new VoteProtoWrapper(event.result().body().getVote()));
      } else if (event.succeeded()) {
        completableFuture.completeExceptionally(new AssertionError("Should never happen"));
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }
}
