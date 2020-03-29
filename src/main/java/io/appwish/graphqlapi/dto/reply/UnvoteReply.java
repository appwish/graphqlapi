package io.appwish.graphqlapi.dto.reply;

import io.appwish.grpc.UnvoteReplyProto;
import java.util.Objects;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;

/**
 * Represents data to return for delete vote query.
 * <p>
 * {@link ProtoClass} and {@link ProtoField} annotations are used by {@link net.badata.protobuf.converter.Converter} to convert back/forth between
 * protobuf data transfer objects and model objects.
 * <p>
 * The converter requires a POJO with getters, setters and a default constructor.
 */
@ProtoClass(UnvoteReplyProto.class)
public class UnvoteReply {

  @ProtoField
  private boolean deleted;

  public UnvoteReply(final boolean deleted) {
    this.deleted = deleted;
  }

  public UnvoteReply() {
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(final boolean deleted) {
    this.deleted = deleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnvoteReply that = (UnvoteReply) o;
    return deleted == that.deleted;
  }

  @Override
  public int hashCode() {
    return Objects.hash(deleted);
  }

  @Override
  public String toString() {
    return "UnvoteReply{" +
      "deleted=" + deleted +
      '}';
  }
}
