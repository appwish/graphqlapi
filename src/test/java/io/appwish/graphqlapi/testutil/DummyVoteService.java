package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.HasVotedReplyProto;
import io.appwish.grpc.UnvoteReplyProto;
import io.appwish.grpc.VoteInputProto;
import io.appwish.grpc.VoteProto;
import io.appwish.grpc.VoteReplyProto;
import io.appwish.grpc.VoteScoreReplyProto;
import io.appwish.grpc.VoteSelectorProto;
import io.appwish.grpc.VoteServiceGrpc;
import io.vertx.core.Promise;

/**
 * Dummy voteservice - works as a mock of voteservice in tests. Returns some hardcoded data fortesting purposes.
 */
public class DummyVoteService extends VoteServiceGrpc.VoteServiceVertxImplBase {

  @Override
  public void vote(VoteInputProto request, Promise<VoteReplyProto> response) {
    response.complete(VoteReplyProto.newBuilder()
      .setVote(
        VoteProto.newBuilder().build()
      )
      .build());
  }

  @Override
  public void unvote(VoteSelectorProto request, Promise<UnvoteReplyProto> response) {
    response.complete(UnvoteReplyProto.newBuilder()
      .setDeleted(true)
      .build());
  }

  @Override
  public void hasVoted(VoteSelectorProto request, Promise<HasVotedReplyProto> response) {
    response.complete(HasVotedReplyProto.newBuilder()
      .setVoted(true)
      .build());
  }

  @Override
  public void voteScore(VoteSelectorProto request, Promise<VoteScoreReplyProto> response) {
    response.complete(VoteScoreReplyProto.newBuilder()
      .setDown(3)
      .setUp(6)
      .setScore(3)
      .build());
  }
}
