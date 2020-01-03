package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.*;
import io.vertx.core.Promise;

import java.util.Optional;

public class DummySuccessfulWishService extends AppWishServiceGrpc.AppWishServiceVertxImplBase {
  @Override
  public void getAppWish(final AppWishQuery request, final Promise<AppWishReply> response) {
    final Optional<AppWish> any = DummyData.APP_WISH_LIST.getAppWishList().stream().filter(appWish -> appWish.getId().equals(request.getId())).findAny();
    if (any.isPresent()) {
      response.complete(AppWishReply.newBuilder().setAppWish(any.get()).build());
    } else {
      response.complete();
    }
  }

  @Override
  public void getAllAppWish(final AllAppWishQuery request, final Promise<AllAppWishReply> response) {
    response.complete(AllAppWishReply.newBuilder().setAppWishList(DummyData.APP_WISH_LIST).build());
  }
}
