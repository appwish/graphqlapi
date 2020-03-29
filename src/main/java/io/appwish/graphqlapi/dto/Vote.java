package io.appwish.graphqlapi.dto;

import com.google.protobuf.Timestamp;
import io.appwish.graphqlapi.dto.converter.ItemTypeConverter;
import io.appwish.graphqlapi.dto.converter.VoteTypeConverter;
import io.appwish.graphqlapi.dto.type.ItemType;
import io.appwish.graphqlapi.dto.type.VoteType;
import io.appwish.grpc.VoteProto;
import java.util.Objects;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;

/**
 * Represents single user's vote.
 * <p>
 * {@link ProtoClass} and {@link ProtoField} annotations are used by {@link net.badata.protobuf.converter.Converter} to convert back/forth between
 * protobuf data transfer objects and model objects.
 * <p>
 * The converter requires a POJO with getters, setters and a default constructor.
 */
@ProtoClass(VoteProto.class)
public class Vote {

  @ProtoField
  private long id;

  @ProtoField
  private String userId;

  @ProtoField
  private long itemId;

  @ProtoField(converter = ItemTypeConverter.class)
  private ItemType itemType;

  @ProtoField
  private Timestamp createdAt;

  @ProtoField(converter = VoteTypeConverter.class)
  private VoteType voteType;

  public Vote() {
    super();
  }

  public Vote(final long id, final String userId, final long itemId, final ItemType itemType, final VoteType voteType, final Timestamp createdAt) {
    super();
    this.id = id;
    this.userId = userId;
    this.itemId = itemId;
    this.itemType = itemType;
    this.createdAt = createdAt;
    this.voteType = voteType;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(final String userId) {
    this.userId = userId;
  }

  public long getItemId() {
    return itemId;
  }

  public void setItemId(long itemId) {
    this.itemId = itemId;
  }

  public ItemType getItemType() {
    return itemType;
  }

  public void setItemType(ItemType itemType) {
    this.itemType = itemType;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public VoteType getVoteType() {
    return voteType;
  }

  public void setVoteType(VoteType voteType) {
    this.voteType = voteType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Vote vote = (Vote) o;
    return id == vote.id &&
      itemId == vote.itemId &&
      userId.equals(vote.userId) &&
      itemType == vote.itemType &&
      createdAt.equals(vote.createdAt) &&
      voteType == vote.voteType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, itemId, itemType, createdAt, voteType);
  }

  @Override
  public String toString() {
    return "Vote{" +
      "id=" + id +
      ", userId='" + userId + '\'' +
      ", itemId=" + itemId +
      ", itemType=" + itemType +
      ", createdAt=" + createdAt +
      ", voteType=" + voteType +
      '}';
  }
}
