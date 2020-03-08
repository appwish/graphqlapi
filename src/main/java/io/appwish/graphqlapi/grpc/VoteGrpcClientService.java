package io.appwish.graphqlapi.grpc;

import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.grpc.VoteInputProto;
import io.appwish.grpc.VoteServiceGrpc.VoteServiceVertxStub;
import io.vertx.core.eventbus.EventBus;

/**
 * Exposes methods of vote service gRPC client on the event bus.
 */
public class VoteGrpcClientService extends AbstractGrpcClientService<VoteServiceVertxStub> {

  private static final int FAILURE_CODE = 1;

  public VoteGrpcClientService(final EventBus eventBus, final VoteServiceVertxStub stub) {
    super(eventBus, stub);
  }

  @Override
  public void register() {
    eventBus.<VoteInputProto>consumer(Address.VOTE.get(), event -> {
      stub.createVote(event.body(), grpc -> {
        if (grpc.succeeded()) {
          event.reply(grpc.result());
        } else {
          event.fail(FAILURE_CODE, grpc.cause().getMessage());
        }
      });
    });
  }
}
