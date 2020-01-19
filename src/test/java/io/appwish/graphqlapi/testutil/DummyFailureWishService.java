package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.AllWishReplyProto;
import io.appwish.grpc.UpdateWishInputProto;
import io.appwish.grpc.WishDeleteReplyProto;
import io.appwish.grpc.WishInputProto;
import io.appwish.grpc.WishQueryProto;
import io.appwish.grpc.WishReplyProto;
import io.appwish.grpc.WishServiceGrpc;
import io.vertx.core.Promise;

/**
 * Dummy wishservice - works as a mock of wishservice in tests. Simply fails all gRPC requests for testing purposes.
 */
public class DummyFailureWishService extends WishServiceGrpc.WishServiceVertxImplBase {

  @Override
  public void getWish(final WishQueryProto request, final Promise<WishReplyProto> response) {
    response.fail(new RuntimeException());
  }

  @Override
  public void getAllWish(final AllWishQueryProto request, final Promise<AllWishReplyProto> response) {
    response.fail(new RuntimeException());
  }

  @Override
  public void createWish(final WishInputProto request, final Promise<WishReplyProto> response) {
    response.fail(new RuntimeException());
  }

  @Override
  public void updateWish(final UpdateWishInputProto request, final Promise<WishReplyProto> response) {
    response.fail(new RuntimeException());
  }

  @Override
  public void deleteWish(final WishQueryProto request, final Promise<WishDeleteReplyProto> response) {
    response.fail(new RuntimeException());
  }
}
