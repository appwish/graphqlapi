package io.appwish.graphqlapi.dto.reply;

import io.appwish.graphqlapi.dto.Vote;
import java.util.Objects;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;

/**
 * Represents data to return for hasVoted query.
 * <p>
 * {@link ProtoClass} and {@link ProtoField} annotations are used by {@link net.badata.protobuf.converter.Converter} to convert back/forth between
 * protobuf data transfer objects and model objects.
 * <p>
 * The converter requires a POJO with getters, setters and a default constructor.
 */
public class HasVotedReply {

  @ProtoField
  private boolean voted;

  @ProtoField
  private Vote vote;

  public HasVotedReply() {
  }

  public boolean isVoted() {
    return voted;
  }

  public void setVoted(boolean voted) {
    this.voted = voted;
  }

  public Vote getVote() {
    return vote;
  }

  public void setVote(Vote vote) {
    this.vote = vote;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HasVotedReply that = (HasVotedReply) o;
    return voted == that.voted &&
      Objects.equals(vote, that.vote);
  }

  @Override
  public int hashCode() {
    return Objects.hash(voted, vote);
  }

  @Override
  public String toString() {
    return "HasVotedReply{" +
      "voted=" + voted +
      ", vote=" + vote +
      '}';
  }
}
