package io.appwish.graphqlapi.dto.input;

import io.appwish.graphqlapi.dto.converter.ItemTypeConverter;
import io.appwish.graphqlapi.dto.converter.VoteTypeConverter;
import io.appwish.graphqlapi.dto.type.ItemType;
import io.appwish.graphqlapi.dto.type.VoteType;
import io.appwish.grpc.VoteInputProto;
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
@ProtoClass(VoteInputProto.class)
public class VoteInput {

  @ProtoField
  private long itemId;

  @ProtoField(converter = ItemTypeConverter.class)
  private ItemType itemType;

  @ProtoField(converter = VoteTypeConverter.class)
  private VoteType voteType;

  public VoteInput() {
  }

  public VoteInput(long itemId, ItemType itemType, VoteType voteType) {
    super();
    this.itemId = itemId;
    this.itemType = itemType;
    this.voteType = voteType;
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

  public VoteType getVoteType() {
    return voteType;
  }

  public void setVoteType(VoteType voteType) {
    this.voteType = voteType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (itemId ^ (itemId >>> 32));
    result = prime * result + ((itemType == null) ? 0 : itemType.hashCode());
    result = prime * result + ((voteType == null) ? 0 : voteType.hashCode());
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
    VoteInput other = (VoteInput) obj;
    if (itemId != other.itemId) {
      return false;
    }
    if (itemType != other.itemType) {
      return false;
    }
    if (voteType != other.voteType) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("VoteInput [itemId=");
    builder.append(itemId);
    builder.append(", itemType=");
    builder.append(itemType);
    builder.append(", voteType=");
    builder.append(voteType);
    builder.append("]");
    return builder.toString();
  }
}
