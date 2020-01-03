package io.appwish.graphqlapi.fetcher;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.grpc.AllWishQuery;
import io.appwish.grpc.Wish;
import io.appwish.grpc.WishQuery;
import io.appwish.grpc.WishServiceGrpc;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WishFetcher {

  private final WishServiceGrpc.WishServiceVertxStub wishServiceStub;

  public WishFetcher(final WishServiceGrpc.WishServiceVertxStub wishServiceStub) {
    this.wishServiceStub = wishServiceStub;
  }

  public CompletionStage<List<Wish>> findAll(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<List<Wish>> completableFuture = new CompletableFuture<>();

    wishServiceStub.getAllWish(AllWishQuery.newBuilder().build(), event -> {
      if (event.succeeded()) {
        completableFuture.complete(event.result().getWishesList());
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }

  public CompletionStage<Optional<Wish>> findOne(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<Optional<Wish>> completableFuture = new CompletableFuture<>();
    final WishQuery query = WishQuery.newBuilder().setId(dataFetchingEnvironment.getArgument("id")).build();

    wishServiceStub.getWish(query, event -> {
      if (event.succeeded()) {
        if (event.result().hasWish()) {
          completableFuture.complete(Optional.of(event.result().getWish()));
        } else {
          completableFuture.complete(Optional.empty());
        }
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }
}
