package io.appwish.graphqlapi.fetcher;

import graphql.schema.DataFetchingEnvironment;
import io.appwish.grpc.AllAppWishQuery;
import io.appwish.grpc.AppWish;
import io.appwish.grpc.AppWishQuery;
import io.appwish.grpc.AppWishServiceGrpc;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WishFetcher {

  private final AppWishServiceGrpc.AppWishServiceVertxStub wishServiceStub;

  public WishFetcher(final AppWishServiceGrpc.AppWishServiceVertxStub wishServiceStub) {
    this.wishServiceStub = wishServiceStub;
  }

  public CompletionStage<List<AppWish>> findAll(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<List<AppWish>> completableFuture = new CompletableFuture<>();

    wishServiceStub.getAllAppWish(AllAppWishQuery.newBuilder().build(), event -> {
      if (event.succeeded()) {
        completableFuture.complete(event.result().getAppWishList().getAppWishList());
      } else {
        completableFuture.completeExceptionally(event.cause());
      }
    });

    return completableFuture;
  }

  public CompletionStage<Optional<AppWish>> findOne(final DataFetchingEnvironment dataFetchingEnvironment) {
    final CompletableFuture<Optional<AppWish>> completableFuture = new CompletableFuture<>();

    wishServiceStub.getAppWish(AppWishQuery.newBuilder().setId(dataFetchingEnvironment.getArgument("id")).build(), event -> {
      if (event.succeeded()) {
        if (event.result().hasAppWish()) {
          completableFuture.complete(Optional.of(event.result().getAppWish()));
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
