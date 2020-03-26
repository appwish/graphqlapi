package io.appwish.graphqlapi.graphql.fetcher;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.grpc.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Sends events on the event bus to resolve needs of wish-related part of GraphQL schema.
 */
public class WishFetcher {

  private static final String ID = "id";
  private static final String INPUT = "input";
  private static final String TITLE = "title";
  private static final String MARKDOWN = "markdown";
  private static final String COVER_IMAGE_URL = "coverImageUrl";
  private static final String USER_ID = "userId";
  private static final String USER = "user";

  private final EventBus eventBus;

  public WishFetcher(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public CompletionStage<List<WishProtoWrapper>> allWish(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<List<WishProtoWrapper>> completableFuture = new CompletableFuture<>();
    final AllWishQueryProto query = AllWishQueryProto.newBuilder().build();
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);

    eventBus.<AllWishReplyProto>request(
      Address.ALL_WISH.get(), query, options, event -> {
        if (event.succeeded()) {
          completableFuture.complete(event.result().body().getWishesList().stream().map(WishProtoWrapper::new).collect(Collectors.toList()));
        } else {
          completableFuture.completeExceptionally(event.cause());
        }
      });

    return completableFuture;
  }

  public CompletionStage<WishProtoWrapper> wish(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<WishProtoWrapper> completableFuture = new CompletableFuture<>();
    final WishQueryProto query = WishQueryProto.newBuilder().setId(Long.valueOf(dataFetchingEnvironment.getArgument(ID))).build();
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);

    eventBus.<WishReplyProto>request(
      Address.WISH.get(), query, options, event -> {
        if (event.succeeded() && event.result().body().hasWish()) {
          completableFuture.complete(new WishProtoWrapper(event.result().body().getWish()));
        } else if (event.succeeded()) {
          completableFuture.complete(null);
        } else {
          completableFuture.completeExceptionally(event.cause());
        }
      });

    return completableFuture;
  }

  public CompletionStage<WishProtoWrapper> createWish(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<WishProtoWrapper> completableFuture = new CompletableFuture<>();
    final Map<String, String> input = dataFetchingEnvironment.getArgument(INPUT);
    final String title = input.get(TITLE);
    final String markdown = input.get(MARKDOWN);
    final String coverImageUrl = input.get(COVER_IMAGE_URL);
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);
    final WishInputProto wishInput = WishInputProto.newBuilder()
      .setTitle(title)
      .setMarkdown(markdown)
      .setCoverImageUrl(coverImageUrl)
      .build();

    eventBus.<WishReplyProto>request(
      Address.CREATE_WISH.get(), wishInput, options, event -> {
        if (event.succeeded() && event.result().body().hasWish()) {
          completableFuture.complete(new WishProtoWrapper(event.result().body().getWish()));
        } else if (event.succeeded()) {
          completableFuture.completeExceptionally(new AssertionError("It should always create and return a wish"));
        } else {
          completableFuture.completeExceptionally(event.cause());
        }
      });

    return completableFuture;
  }

  public CompletionStage<WishProtoWrapper> updateWish(
    final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<WishProtoWrapper> completableFuture = new CompletableFuture<>();
    final Map<String, String> input = dataFetchingEnvironment.getArgument(INPUT);
    final String title = input.get(TITLE);
    final String markdown = input.get(MARKDOWN);
    final String coverImageUrl = input.get(COVER_IMAGE_URL);
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);
    final UpdateWishInputProto wishInput = UpdateWishInputProto.newBuilder()
      .setTitle(title)
      .setMarkdown(markdown)
      .setCoverImageUrl(coverImageUrl)
      .setId(Long.valueOf(dataFetchingEnvironment.getArgument(ID)))
      .build();

    eventBus.<WishReplyProto>request(
      Address.UPDATE_WISH.get(), wishInput, options, event -> {
        if (event.succeeded() && event.result().body().hasWish()) {
          completableFuture.complete(new WishProtoWrapper(event.result().body().getWish()));
        } else if (event.succeeded()) {
          completableFuture.complete(null);
        } else {
          completableFuture.completeExceptionally(event.cause());
        }
      });

    return completableFuture;
  }

  public CompletionStage<Boolean> deleteWish(final DataFetchingEnvironment dataFetchingEnvironment) { final CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
    final WishQueryProto query = WishQueryProto.newBuilder().setId(Long.valueOf(dataFetchingEnvironment.getArgument(ID))).build();
    final String userId = getUserIdFrom(dataFetchingEnvironment);
    final DeliveryOptions options = isNull(userId) ? new DeliveryOptions() : new DeliveryOptions().addHeader(USER_ID, userId);

    eventBus.<WishDeleteReplyProto>request(
      Address.DELETE_WISH.get(), query, options, event -> {
        if (event.succeeded()) {
          completableFuture.complete(event.result().body().getDeleted());
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
