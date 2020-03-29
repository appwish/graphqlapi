package io.appwish.graphqlapi.grpc;

import static java.util.Objects.isNull;

import io.appwish.graphqlapi.dto.input.VoteInput;
import io.appwish.graphqlapi.dto.query.VoteSelector;
import io.appwish.graphqlapi.dto.reply.HasVotedReply;
import io.appwish.graphqlapi.dto.reply.UnvoteReply;
import io.appwish.graphqlapi.dto.reply.VoteReply;
import io.appwish.graphqlapi.dto.reply.VoteScoreReply;
import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.grpc.VoteInputProto;
import io.appwish.grpc.VoteSelectorProto;
import io.appwish.grpc.VoteServiceGrpc.VoteServiceVertxStub;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.badata.protobuf.converter.Converter;

/**
 * Exposes methods of vote service gRPC client on the event bus.
 */
public class VoteGrpcClientService extends AbstractGrpcClientService<VoteServiceVertxStub> {

  private static final String USER_ID = "userId";
  private static final int FAILURE_CODE = 1;

  private final Converter converter;

  public VoteGrpcClientService(final EventBus eventBus, final VoteServiceVertxStub stub) {
    super(eventBus, stub);
    this.converter = Converter.create();
  }

  @Override
  public void register() {
    eventBus.consumer(Address.VOTE.get(), voteHandler());
    eventBus.consumer(Address.UNVOTE.get(), unvoteHandler());
    eventBus.consumer(Address.HAS_VOTED.get(), hasVotedHandler());
    eventBus.consumer(Address.VOTE_SCORE.get(), voteScoreHandler());
  }

  private Handler<Message<VoteInput>> voteHandler() {
    return event -> {
      final Metadata metadata = new Metadata();
      final String userId = event.headers().get(USER_ID);

      if (!isNull(userId)) {
        metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), userId);
      }

      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata))
        .vote(converter.toProtobuf(VoteInputProto.class, event.body()), grpc -> {
          if (grpc.succeeded()) {
            event.reply(converter.toDomain(VoteReply.class, grpc.result()));
          } else {
            event.fail(FAILURE_CODE, grpc.cause().getMessage());
          }
        });
    };
  }

  private Handler<Message<VoteSelector>> unvoteHandler() {
    return event -> {
      final Metadata metadata = new Metadata();
      final String userId = event.headers().get(USER_ID);

      if (!isNull(userId)) {
        metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), userId);
      }

      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata))
        .unvote(converter.toProtobuf(VoteSelectorProto.class, event.body()), grpc -> {
          if (grpc.succeeded()) {
            event.reply(converter.toDomain(UnvoteReply.class, grpc.result()));
          } else {
            event.fail(FAILURE_CODE, grpc.cause().getMessage());
          }
        });
    };
  }

  private Handler<Message<VoteSelector>> hasVotedHandler() {
    return event -> {
      final Metadata metadata = new Metadata();
      final String userId = event.headers().get(USER_ID);

      if (!isNull(userId)) {
        metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), userId);
      }

      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata))
        .hasVoted(converter.toProtobuf(VoteSelectorProto.class, event.body()), grpc -> {
          if (grpc.succeeded()) {
            event.reply(converter.toDomain(HasVotedReply.class, grpc.result()));
          } else {
            event.fail(FAILURE_CODE, grpc.cause().getMessage());
          }
        });
    };
  }

  private Handler<Message<VoteSelector>> voteScoreHandler() {
    return event -> stub.voteScore(converter.toProtobuf(VoteSelectorProto.class, event.body()), grpc -> {
      if (grpc.succeeded()) {
        event.reply(converter.toDomain(VoteScoreReply.class, grpc.result()));
      } else {
        event.fail(FAILURE_CODE, grpc.cause().getMessage());
      }
    });
  }
}
