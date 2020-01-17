package io.appwish.graphqlapi.grpc;

import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.graphqlapi.eventbus.Codec;
import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.UpdateWishInputProto;
import io.appwish.grpc.WishInputProto;
import io.appwish.grpc.WishQueryProto;
import io.appwish.grpc.WishServiceGrpc.WishServiceVertxStub;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;

/**
 * Registers methods of wishservice gRPC client stub on the event bus.
 * */
public class WishGrpcClientService extends AbstractGrpcClientService {

  public WishGrpcClientService(final EventBus eventBus, final GrpcServiceStubsProvider grpcServiceStubsProvider) {
    super(eventBus, grpcServiceStubsProvider);
  }

  @Override
  public void registerServiceStub() {
    final WishServiceVertxStub wishService = serviceStubsProvider.wishServiceStub();

    eventBus.<AllWishQueryProto>consumer(Address.ALL_WISH.get(), event -> {
      wishService.getAllWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(1, "Could not fetch data from wish service");
        }
      });
    });

    eventBus.<WishQueryProto>consumer(Address.WISH.get(), event -> {
      wishService.getWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(1, "Could not fetch data from wish service");
        }
      });
    });

    eventBus.<WishInputProto>consumer(Address.CREATE_WISH.get(), event -> {
      wishService.createWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result().getWish());
        } else {
          event.fail(1, "Could not fetch data from wish service");
        }
      });
    });

    eventBus.<WishQueryProto>consumer(Address.DELETE_WISH.get(), event -> {
      wishService.deleteWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result().getDeleted());
        } else {
          event.fail(1, "Could not fetch data from wish service");
        }
      });
    });

    eventBus.<UpdateWishInputProto>consumer(Address.UPDATE_WISH.get(), event -> {
      wishService.updateWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result(), new DeliveryOptions().setCodecName(Codec.WISH_REPLY.getCodecName()));
        } else {
          event.fail(1, "Could not fetch data from wish service");
        }
      });
    });
  }
}
