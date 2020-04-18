package io.appwish.graphqlapi.dto;

import com.google.protobuf.Timestamp;
import io.appwish.graphqlapi.dto.converter.ItemTypeConverter;
import io.appwish.graphqlapi.dto.type.ItemType;
import io.appwish.grpc.CommentProto;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;

/**
 * {@link ProtoClass} and {@link ProtoField} annotations are used by {@link
 * net.badata.protobuf.converter.Converter} to convert back/forth between protobuf data transfer
 * objects and model objects.
 *
 * The converter requires a POJO with getters, setters and a default constructor.
 */
@ProtoClass(CommentProto.class)
public class Comment {

  @ProtoField
  private long id;

  @ProtoField
  private String userId;

  @ProtoField
  private String itemId;

  @ProtoField(converter = ItemTypeConverter.class)
  private ItemType itemType;

  @ProtoField
  private String content;

  @ProtoField
  private OffsetDateTime createdAt;

  @ProtoField
  private OffsetDateTime updatedAt;

  public Comment(long id, String userId, String itemId,
      ItemType itemType, String content, Timestamp protoCreatedAt,
      Timestamp protoUpdatedAt) {
    this.id = id;
    this.userId = userId;
    this.itemId = itemId;
    this.itemType = itemType;
    this.content = content;
    final LocalDateTime updatedAt = LocalDateTime.from(LocalDateTime.ofInstant(Instant.ofEpochSecond(protoUpdatedAt.getSeconds()), ZoneOffset.UTC));
    final LocalDateTime createdAt = LocalDateTime.from(LocalDateTime.ofInstant(Instant.ofEpochSecond(protoCreatedAt.getSeconds()), ZoneOffset.UTC));
    this.createdAt = OffsetDateTime.of(createdAt, ZoneOffset.UTC);
    this.updatedAt = OffsetDateTime.of(updatedAt, ZoneOffset.UTC);
  }

  public Comment() {
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

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public ItemType getItemType() {
    return itemType;
  }

  public void setItemType(ItemType itemType) {
    this.itemType = itemType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return id == comment.id &&
        userId == comment.userId &&
        itemId == comment.itemId &&
        itemType == comment.itemType &&
        content.equals(comment.content) &&
        createdAt.equals(comment.createdAt) &&
        updatedAt.equals(comment.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, itemId, itemType, content, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return "Comment{" +
        "id=" + id +
        ", userId=" + userId +
        ", parentId=" + itemId +
        ", parentType=" + itemType +
        ", content='" + content + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
