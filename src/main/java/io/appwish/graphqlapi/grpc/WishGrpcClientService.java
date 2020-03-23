package io.appwish.graphqlapi.grpc;

import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.UpdateWishInputProto;
import io.appwish.grpc.WishInputProto;
import io.appwish.grpc.WishQueryProto;
import io.appwish.grpc.WishServiceGrpc.WishServiceVertxStub;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.vertx.core.eventbus.EventBus;

/**
 * Exposes methods of wish service gRPC client on the event bus.
 */
public class WishGrpcClientService extends AbstractGrpcClientService<WishServiceVertxStub> {

  private static final String USER_ID = "userId";
  private static final int FAILURE_CODE = 1;

  public WishGrpcClientService(final EventBus eventBus, final WishServiceVertxStub stub) {
    super(eventBus, stub);
  }

  @Override
  public void register() {
    eventBus.<AllWishQueryProto>consumer(Address.ALL_WISH.get(), event -> {
      final Metadata metadata = new Metadata();
      metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), event.headers().get(USER_ID));
      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata)).getAllWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, grpc.cause().getMessage());
        }
      });
    });

    eventBus.<WishQueryProto>consumer(Address.WISH.get(), event -> {
      final Metadata metadata = new Metadata();
      metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), event.headers().get(USER_ID));
      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata)).getWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, grpc.cause().getMessage());
        }
      });
    });

    eventBus.<WishInputProto>consumer(Address.CREATE_WISH.get(), event -> {
      final Metadata metadata = new Metadata();
      metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), event.headers().get(USER_ID));
      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata)).createWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, grpc.cause().getMessage());
        }
      });
    });

    eventBus.<WishQueryProto>consumer(Address.DELETE_WISH.get(), event -> {
      final Metadata metadata = new Metadata();
      metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), event.headers().get(USER_ID));
      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata)).deleteWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, grpc.cause().getMessage());
        }
      });
    });

    eventBus.<UpdateWishInputProto>consumer(Address.UPDATE_WISH.get(), event -> {
      final Metadata metadata = new Metadata();
      metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), event.headers().get(USER_ID));
      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata)).updateWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, grpc.cause().getMessage());
        }
      });
    });
  }
}
