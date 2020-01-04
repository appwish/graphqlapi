package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.*;
import io.vertx.core.Promise;

public class DummyFailureWishService extends WishServiceGrpc.WishServiceVertxImplBase {
  @Override
  public void getWish(final WishQuery request, final Promise<WishReply> response) {
    response.fail(new RuntimeException());
  }

  @Override
  public void getAllWish(final AllWishQuery request, final Promise<AllWishReply> response) {
    response.fail(new RuntimeException());
  }
}
