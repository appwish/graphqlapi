package io.appwish.graphqlapi.grpc;

import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.grpc.AllWishQueryProto;
import io.appwish.grpc.UpdateWishInputProto;
import io.appwish.grpc.WishInputProto;
import io.appwish.grpc.WishQueryProto;
import io.appwish.grpc.WishServiceGrpc.WishServiceVertxStub;
import io.vertx.core.eventbus.EventBus;

/**
 * Exposes methods of wish service gRPC client on the event bus.
 */
public class WishGrpcClientService extends AbstractGrpcClientService<WishServiceVertxStub> {

  private static final int FAILURE_CODE = 1;
  private static final String WISH_SERVICE_COMMUNICATION_ERROR_MESSAGE = "Could not access wish service";

  public WishGrpcClientService(final EventBus eventBus, final WishServiceVertxStub stub) {
    super(eventBus, stub);
  }

  @Override
  public void register() {
    eventBus.<AllWishQueryProto>consumer(Address.ALL_WISH.get(), event -> {
      stub.getAllWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, WISH_SERVICE_COMMUNICATION_ERROR_MESSAGE);
        }
      });
    });

    eventBus.<WishQueryProto>consumer(Address.WISH.get(), event -> {
      stub.getWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, WISH_SERVICE_COMMUNICATION_ERROR_MESSAGE);
        }
      });
    });

    eventBus.<WishInputProto>consumer(Address.CREATE_WISH.get(), event -> {
      stub.createWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, WISH_SERVICE_COMMUNICATION_ERROR_MESSAGE);
        }
      });
    });

    eventBus.<WishQueryProto>consumer(Address.DELETE_WISH.get(), event -> {
      stub.deleteWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, WISH_SERVICE_COMMUNICATION_ERROR_MESSAGE);
        }
      });
    });

    eventBus.<UpdateWishInputProto>consumer(Address.UPDATE_WISH.get(), event -> {
      stub.updateWish(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, WISH_SERVICE_COMMUNICATION_ERROR_MESSAGE);
        }
      });
    });
  }
}
