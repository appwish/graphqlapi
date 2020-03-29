package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.HasVotedReplyProto;
import io.appwish.grpc.UnvoteReplyProto;
import io.appwish.grpc.VoteInputProto;
import io.appwish.grpc.VoteReplyProto;
import io.appwish.grpc.VoteScoreReplyProto;
import io.appwish.grpc.VoteSelectorProto;
import io.appwish.grpc.VoteServiceGrpc;
import io.vertx.core.Promise;

/**
 * Dummy wishservice - works as a mock of wishservice in tests. Simply fails all gRPC requests for testing purposes.
 */
public class DummyFailureVoteService extends VoteServiceGrpc.VoteServiceVertxImplBase {

  @Override
  public void vote(VoteInputProto request, Promise<VoteReplyProto> response) {
    response.fail(new RuntimeException());
  }

  @Override
  public void unvote(VoteSelectorProto request, Promise<UnvoteReplyProto> response) {
    response.fail(new RuntimeException());
  }

  @Override
  public void hasVoted(VoteSelectorProto request, Promise<HasVotedReplyProto> response) {
    response.fail(new RuntimeException());
  }

  @Override
  public void voteScore(VoteSelectorProto request, Promise<VoteScoreReplyProto> response) {
    response.fail(new RuntimeException());
  }
}
