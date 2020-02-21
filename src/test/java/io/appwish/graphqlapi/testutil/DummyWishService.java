package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.AllWishReplyProto;
import io.appwish.grpc.UpdateWishInputProto;
import io.appwish.grpc.WishDeleteReplyProto;
import io.appwish.grpc.WishInputProto;
import io.appwish.grpc.WishProto;
import io.appwish.grpc.WishQueryProto;
import io.appwish.grpc.WishReplyProto;
import io.appwish.grpc.WishServiceGrpc;
import io.vertx.core.Promise;
import java.util.Optional;

/**
 * Dummy wishservice - works as a mock of wishservice in tests. Returns some hardcoded data for
 * testing purposes.
 */
public class DummyWishService extends WishServiceGrpc.WishServiceVertxImplBase {

  /**
   * Returns any wish from {@link TestData#WISHES} that id matches query id
   */
  @Override
  public void getWish(final WishQueryProto request, final Promise<WishReplyProto> response) {
    final Optional<WishProto> any = TestData.WISHES.stream()
      .filter(wish -> wish.getId() == request.getId())
      .findAny();

    if (any.isPresent()) {
      response.complete(WishReplyProto.newBuilder().setWish(any.get()).build());
    } else {
      response.complete();
    }
  }

  /**
   * Returns hardcoded list of {@link TestData#WISHES}
   */
  @Override
  public void getAllWish(final AllWishQueryProto request, final Promise<AllWishReplyProto> response) {
    response.complete(AllWishReplyProto.newBuilder().addAllWishes(TestData.WISHES).build());
  }

  /**
   * Returns wish with values from request + hardcoded values for those which are generated or added
   * based on some business logic in wishservice.
   */
  @Override
  public void createWish(final WishInputProto request, final Promise<WishReplyProto> response) {
    final WishProto wish = WishProto.newBuilder()
      .setSlug("NOT PASSED WITH REQUEST, IDs are also not passed so they're hardcoded")
      .setAuthorId(1)
      .setId(1)
      .setCoverImageUrl(request.getCoverImageUrl())
      .setTitle(request.getTitle())
      .setMarkdown(request.getMarkdown())
      .build();

    response.complete(WishReplyProto.newBuilder().setWish(wish).build());
  }

  /**
   * If id matches one of ids in the {@link TestData#WISHES}, it 'updates' the wish by returning a new wish with values
   * set from those in update wish input, otherwise returns empty if wish not found
   */
  @Override
  public void updateWish(UpdateWishInputProto request, Promise<WishReplyProto> response) {
    final Optional<WishProto> any = TestData.WISHES.stream()
      .filter(wish -> wish.getId() == request.getId())
      .findAny();

    if (any.isPresent()) {
      final WishProto wish = WishProto.newBuilder()
        .setSlug(any.get().getSlug())
        .setAuthorId(any.get().getAuthorId())
        .setId(any.get().getId())
        .setCoverImageUrl(request.getCoverImageUrl())
        .setTitle(request.getTitle())
        .setMarkdown(request.getMarkdown())
        .build();

      response.complete(WishReplyProto.newBuilder().setWish(wish).build());
    } else {
      response.complete(WishReplyProto.newBuilder().build());
    }
  }

  /**
   * Returns true if ID hit one of {@link TestData#WISHES} or false otherwise
   */
  @Override
  public void deleteWish(WishQueryProto request, Promise<WishDeleteReplyProto> response) {
    final Optional<WishProto> any = TestData.WISHES.stream()
      .filter(wish -> wish.getId() == request.getId()).findAny();
    if (any.isPresent()) {
      response.complete(WishDeleteReplyProto.newBuilder().setDeleted(true).build());
    } else {
      response.complete(WishDeleteReplyProto.newBuilder().setDeleted(false).build());
    }
  }
}
