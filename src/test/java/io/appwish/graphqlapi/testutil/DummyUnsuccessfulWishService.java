package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.*;
import io.vertx.core.Promise;

public class DummyUnsuccessfulWishService extends AppWishServiceGrpc.AppWishServiceVertxImplBase {
  @Override
  public void getAppWish(final AppWishQuery request, final Promise<AppWishReply> response) {
    response.fail(new RuntimeException("BOOM"));
  }

  @Override
  public void getAllAppWish(final AllAppWishQuery request, final Promise<AllAppWishReply> response) {
    response.fail(new RuntimeException("BOOM"));
  }
}
