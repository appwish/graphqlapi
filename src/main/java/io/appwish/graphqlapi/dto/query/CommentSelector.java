package io.appwish.graphqlapi.dto.query;

import io.appwish.graphqlapi.dto.converter.ItemTypeConverter;
import io.appwish.graphqlapi.dto.type.ItemType;
import io.appwish.grpc.CommentSelectorProto;
import java.util.Objects;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;

/**
 * This type should be used to query votes from the database.
 * <p>
 * {@link ProtoClass} and {@link ProtoField} annotations are used by {@link net.badata.protobuf.converter.Converter} to convert back/forth between
 * protobuf data transfer objects and model objects.
 * <p>
 * The converter requires a POJO with getters, setters and a default constructor.
 */
@ProtoClass(CommentSelectorProto.class)
public class CommentSelector {

  @ProtoField
  private String itemId;

  @ProtoField(converter = ItemTypeConverter.class)
  private ItemType itemType;

  public CommentSelector(final String itemId, final ItemType itemType) {
    this.itemId = itemId;
    this.itemType = itemType;
  }

  public CommentSelector() {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentSelector commentSelector = (CommentSelector) o;
    return itemId == commentSelector.itemId &&
      itemType.equals(commentSelector.itemType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, itemType);
  }

  @Override
  public String toString() {
    return "CommentSelector{" +
      "itemId=" + itemId +
      ", itemType='" + itemType + '\'' +
      '}';
  }
}
