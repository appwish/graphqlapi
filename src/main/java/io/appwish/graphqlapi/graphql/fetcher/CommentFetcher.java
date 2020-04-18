package io.appwish.graphqlapi.graphql.fetcher;

import static java.util.Objects.isNull;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.graphqlapi.dto.Comment;
import io.appwish.graphqlapi.dto.User;
import io.appwish.graphqlapi.dto.input.CommentInput;
import io.appwish.graphqlapi.dto.input.UpdateCommentInput;
import io.appwish.graphqlapi.dto.query.CommentSelector;
import io.appwish.graphqlapi.dto.reply.AllCommentReply;
import io.appwish.graphqlapi.dto.reply.CommentReply;
import io.appwish.graphqlapi.dto.type.ItemType;
import io.appwish.graphqlapi.eventbus.Address;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Sends events on the event bus to resolve needs of comment-related part of GraphQL schema.
 */
public class CommentFetcher {

  private static final String ITEM_ID = "itemId";
  private static final String INPUT = "input";
  private static final String ITEM_TYPE = "itemType";
  private static final String VOTE_TYPE = "voteType";
  private static final String USER_ID = "userId";
  private static final String USER = "user";
  public static final String SELECTOR = "selector";
  public static final String CONTENT = "content";

  private final EventBus eventBus;

  public CommentFetcher(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public CompletionStage<CommentReply> comment(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<CommentReply> completableFuture = new CompletableFuture<>();
    final Map<String, String> input = dataFetchingEnvironment.getArgument(INPUT);
    final String itemId = input.get(ITEM_ID);
    final String itemType = input.get(ITEM_TYPE);
    final String content = input.get(CONTENT);
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);
    final CommentInput commentInput = new CommentInput(Long.parseLong(itemId), ItemType.valueOf(itemType), content);

    eventBus.<CommentReply>request(Address.COMMENT.get(), commentInput, options, event -> {
      if (event.succeeded() && !isNull(event.result().body())) {
        event.result().body().setAuthor(new User(event.result().body().getComment().getUserId()));
        completableFuture.complete(event.result().body());
      } else if (event.succeeded()) {
        completableFuture.completeExceptionally(new AssertionError("Should never happen"));
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }

  public CompletionStage<CommentReply> updateComment(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<CommentReply> completableFuture = new CompletableFuture<>();
    final String id = dataFetchingEnvironment.getArgument("id");
    final String content = dataFetchingEnvironment.getArgument("content");
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);
    final UpdateCommentInput updateCommentInput = new UpdateCommentInput(Long.parseLong(id), content);

    eventBus.<CommentReply>request(Address.UPDATE_COMMENT.get(), updateCommentInput, options, event -> {
      if (event.succeeded() && !isNull(event.result().body())) {
        event.result().body().setAuthor(new User(event.result().body().getComment().getUserId()));
        completableFuture.complete(event.result().body());
      } else if (event.succeeded()) {
        completableFuture.completeExceptionally(new AssertionError("Could not update comment. Are you an author?"));
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }


  public CompletionStage<List<Comment>> allComments(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<List<Comment>> completableFuture = new CompletableFuture<>();
    final String itemId = dataFetchingEnvironment.getArgument(ITEM_ID);
    final String itemType = dataFetchingEnvironment.getArgument(ITEM_TYPE);
    final CommentSelector commentSelector = new CommentSelector(itemId, ItemType.valueOf(itemType));

    eventBus.<AllCommentReply>request(Address.ALL_COMMENTS.get(), commentSelector, event -> {
      if (event.succeeded() && !isNull(event.result().body())) {
        completableFuture.complete(event.result().body().getComments());
      } else if (event.succeeded()) {
        completableFuture.completeExceptionally(new AssertionError("Should never happen"));
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

  public CompletionStage<Boolean> deleteComment(DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
    final String commentId = dataFetchingEnvironment.getArgument("commentId");
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);

    eventBus.<Boolean>request(Address.DELETE_COMMENT.get(), Long.valueOf(commentId), options, event -> {
      if (event.succeeded() && !isNull(event.result().body())) {
        completableFuture.complete(event.result().body());
      } else if (event.succeeded()) {
        completableFuture.completeExceptionally(new AssertionError("Should never happen"));
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }
}
