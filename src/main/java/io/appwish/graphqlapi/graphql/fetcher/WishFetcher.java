package io.appwish.graphqlapi.graphql.fetcher;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.AllWishReplyProto;
import io.appwish.grpc.UpdateWishInputProto;
import io.appwish.grpc.WishDeleteReplyProto;
import io.appwish.grpc.WishInputProto;
import io.appwish.grpc.WishQueryProto;
import io.appwish.grpc.WishReplyProto;
import io.vertx.core.eventbus.EventBus;

/**
 * Sends events on the event bus to resolve needs of wish-related part of GraphQL schema.
 */
public class WishFetcher {

  private static final String ID = "id";
  private static final String INPUT = "input";
  private static final String TITLE = "title";
  private static final String MARKDOWN = "markdown";
  private static final String COVER_IMAGE_URL = "coverImageUrl";

  private final EventBus eventBus;

  public WishFetcher(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public CompletionStage<List<WishProtoWrapper>> allWish(final DataFetchingEnvironment dataFetchingEnvironment) {
    final RoutingContext context = dataFetchingEnvironment.getLocalContext();
    final JsonObject user = context.get("user");
    final String email = user.getString("email");
    final CompletableFuture<List<WishProtoWrapper>> completableFuture = new CompletableFuture<>();
    final AllWishQueryProto query = AllWishQueryProto.newBuilder().build();

    eventBus.<AllWishReplyProto>request(Address.ALL_WISH.get(), query, new DeliveryOptions().addHeader("email", email), event -> {
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

    eventBus.<WishReplyProto>request(Address.WISH.get(), query, event -> {
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
    final WishInputProto wishInput = WishInputProto.newBuilder()
      .setTitle(title)
      .setMarkdown(markdown)
      .setCoverImageUrl(coverImageUrl)
      .build();

    eventBus.<WishReplyProto>request(Address.CREATE_WISH.get(), wishInput, event -> {
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
    final UpdateWishInputProto wishInput = UpdateWishInputProto.newBuilder()
      .setTitle(title)
      .setMarkdown(markdown)
      .setCoverImageUrl(coverImageUrl)
      .setId(Long.valueOf(dataFetchingEnvironment.getArgument(ID)))
      .build();

    eventBus.<WishReplyProto>request(Address.UPDATE_WISH.get(), wishInput,
      event -> {
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

    eventBus.<WishDeleteReplyProto>request(Address.DELETE_WISH.get(), query,
      event -> {
        if (event.succeeded()) {
          completableFuture.complete(event.result().body().getDeleted());
        } else {
          completableFuture.completeExceptionally(event.cause());
        }
      });

    return completableFuture;
  }
}
