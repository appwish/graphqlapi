package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.*;
import io.vertx.core.Promise;

import java.util.Optional;

public class DummySuccessWishService extends WishServiceGrpc.WishServiceVertxImplBase {
  @Override
  public void getWish(final WishQuery request, final Promise<WishReply> response) {
    final Optional<Wish> any = DummyData.WISHES.stream().filter(appWish -> appWish.getId().equals(request.getId())).findAny();
    if (any.isPresent()) {
      response.complete(WishReply.newBuilder().setWish(any.get()).build());
    } else {
      response.complete();
    }
  }

  @Override
  public void getAllWish(final AllWishQuery request, final Promise<AllWishReply> response) {
    response.complete(AllWishReply.newBuilder().addAllWishes(DummyData.WISHES).build());
  }
}
