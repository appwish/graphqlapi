package io.appwish.graphqlapi.graphql.fetcher;


import io.appwish.grpc.VoteProto;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class VoteProtoWrapper {

  private long id;
  private long userId;
  private long itemId;
  private String itemType;
  private OffsetDateTime createdAt;
  private String voteType;

  public VoteProtoWrapper() {
    super();
  }

  public VoteProtoWrapper(final VoteProto proto) {
    super();
    this.id = proto.getId();
    this.userId = proto.getUserId();
    this.itemId = proto.getItemId();
    this.itemType = proto.getItemType().toString();
    this.createdAt = OffsetDateTime.of(LocalDateTime.from(LocalDateTime.ofInstant(Instant.ofEpochSecond(proto.getCreatedAt().getSeconds()), ZoneOffset.UTC)), ZoneOffset.UTC);
    this.voteType = proto.getVoteType().toString();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getItemId() {
    return itemId;
  }

  public void setItemId(long itemId) {
    this.itemId = itemId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoteProtoWrapper that = (VoteProtoWrapper) o;
    return id == that.id &&
      userId == that.userId &&
      itemId == that.itemId &&
      itemType.equals(that.itemType) &&
      createdAt.equals(that.createdAt) &&
      voteType.equals(that.voteType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, itemId, itemType, createdAt, voteType);
  }
}
