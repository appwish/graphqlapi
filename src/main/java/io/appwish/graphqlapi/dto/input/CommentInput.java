package io.appwish.graphqlapi.dto.input;

import io.appwish.graphqlapi.dto.converter.ItemTypeConverter;
import io.appwish.graphqlapi.dto.type.ItemType;
import io.appwish.grpc.CommentInputProto;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;

/**
 * This type should be used for inserting new votes to the database.
 * <p>
 * {@link ProtoClass} and {@link ProtoField} annotations are used by {@link net.badata.protobuf.converter.Converter} to convert back/forth between
 * protobuf data transfer objects and model objects.
 * <p>
 * The converter requires a POJO with getters, setters and a default constructor.
 */
@ProtoClass(CommentInputProto.class)
public class CommentInput {

  @ProtoField
  private long itemId;

  @ProtoField(converter = ItemTypeConverter.class)
  private ItemType itemType;

  @ProtoField
  private String content;

  public CommentInput() {
  }

  public CommentInput(long itemId, ItemType itemType, String content) {
    super();
    this.itemId = itemId;
    this.itemType = itemType;
    this.content = content;
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

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (itemId ^ (itemId >>> 32));
    result = prime * result + ((itemType == null) ? 0 : itemType.hashCode());
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CommentInput other = (CommentInput) obj;
    if (itemId != other.itemId) {
      return false;
    }
    if (itemType != other.itemType) {
      return false;
    }
    if (content != other.content) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("CommentInput [itemId=");
    builder.append(itemId);
    builder.append(", itemType=");
    builder.append(itemType);
    builder.append(", content=");
    builder.append(content);
    builder.append("]");
    return builder.toString();
  }
}
