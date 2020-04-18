package io.appwish.graphqlapi.grpc;

import static java.util.Objects.isNull;

import io.appwish.graphqlapi.dto.Comment;
import io.appwish.graphqlapi.dto.User;
import io.appwish.graphqlapi.dto.input.CommentInput;
import io.appwish.graphqlapi.dto.input.UpdateCommentInput;
import io.appwish.graphqlapi.dto.query.CommentSelector;
import io.appwish.graphqlapi.dto.reply.AllCommentReply;
import io.appwish.graphqlapi.dto.reply.CommentReply;
import io.appwish.graphqlapi.dto.type.ItemType;
import io.appwish.graphqlapi.eventbus.Address;
import io.appwish.grpc.CommentInputProto;
import io.appwish.grpc.CommentQueryProto;
import io.appwish.grpc.CommentSelectorProto;
import io.appwish.grpc.CommentServiceGrpc.CommentServiceVertxStub;
import io.appwish.grpc.UpdateCommentInputProto;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import java.util.stream.Collectors;
import net.badata.protobuf.converter.Converter;

/**
 * Exposes methods of comment service gRPC client on the event bus.
 */
public class CommentGrpcClientService extends AbstractGrpcClientService<CommentServiceVertxStub> {

  private static final String USER_ID = "userId";
  private static final int FAILURE_CODE = 1;

  private final Converter converter;

  public CommentGrpcClientService(final EventBus eventBus, final CommentServiceVertxStub stub) {
    super(eventBus, stub);
    this.converter = Converter.create();
  }

  @Override
  public void register() {
    eventBus.consumer(Address.COMMENT.get(), commentHandler());
    eventBus.consumer(Address.ALL_COMMENTS.get(), allCommentsHandler());
    eventBus.consumer(Address.DELETE_COMMENT.get(), deleteCommentHandler());
    eventBus.consumer(Address.UPDATE_COMMENT.get(), updateHandler());
  }

  private Handler<Message<UpdateCommentInput>> updateHandler() {
    return event -> {
      final Metadata metadata = new Metadata();
      final String userId = event.headers().get(USER_ID);

      if (!isNull(userId)) {
        metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), userId);
      }

      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata))
        .updateComment(UpdateCommentInputProto.newBuilder().setContent(event.body().getContent()).setId(event.body().getId()).build(), grpc -> {
          if (grpc.succeeded()) {
            event.reply(new CommentReply(new Comment(
              grpc.result().getComment().getId(),
              grpc.result().getComment().getUserId(),
              grpc.result().getComment().getItemId(),
              ItemType.valueOf(grpc.result().getComment().getItemType().name()),
              grpc.result().getComment().getContent(),
              grpc.result().getComment().getCreatedAt(),
              grpc.result().getComment().getUpdatedAt()
            ), new User(grpc.result().getComment().getUserId())));
          } else {
            event.fail(FAILURE_CODE, grpc.cause().getMessage());
          }
        });
    };
  }


  private Handler<Message<CommentInput>> commentHandler() {
    return event -> {
      final Metadata metadata = new Metadata();
      final String userId = event.headers().get(USER_ID);

      if (!isNull(userId)) {
        metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), userId);
      }

      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata))
        .createComment(converter.toProtobuf(CommentInputProto.class, event.body()), grpc -> {
          if (grpc.succeeded()) {
            event.reply(new CommentReply(new Comment(
              grpc.result().getComment().getId(),
              grpc.result().getComment().getUserId(),
              grpc.result().getComment().getItemId(),
              ItemType.valueOf(grpc.result().getComment().getItemType().name()),
              grpc.result().getComment().getContent(),
              grpc.result().getComment().getCreatedAt(),
              grpc.result().getComment().getUpdatedAt()
            ), new User(grpc.result().getComment().getUserId())));
          } else {
            event.fail(FAILURE_CODE, grpc.cause().getMessage());
          }
        });
    };
  }

  private Handler<Message<CommentSelector>> allCommentsHandler() {
    return event -> {
      stub.getAllComment(converter.toProtobuf(CommentSelectorProto.class, event.body()), grpc -> {
        if (grpc.succeeded()) {
          event.reply(new AllCommentReply(grpc.result().getCommentsList().stream()
            .map(commentProto -> new Comment(
              commentProto.getId(),
              commentProto.getUserId(),
              commentProto.getItemId(),
              ItemType.valueOf(commentProto.getItemType().name()),
              commentProto.getContent(),
              commentProto.getCreatedAt(),
              commentProto.getUpdatedAt()
            ) )
            .collect(Collectors.toList())));
        } else {
          event.fail(FAILURE_CODE, grpc.cause().getMessage());
        }
      });
    };
  }

  private Handler<Message<Long>> deleteCommentHandler() {
    return event -> {
      final Metadata metadata = new Metadata();
      final String userId = event.headers().get(USER_ID);

      if (!isNull(userId)) {
        metadata.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), userId);
      }

      stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata))
        .deleteComment(CommentQueryProto.newBuilder().setId(event.body()).build(), grpc -> {
          if (grpc.succeeded() && grpc.result().getDeleted()) {
            event.reply(true);
          } else if (grpc.succeeded()) {
            event.reply(false);
          } else {
            event.fail(FAILURE_CODE, grpc.cause().getMessage());
          }
        });
    };
  }
}
