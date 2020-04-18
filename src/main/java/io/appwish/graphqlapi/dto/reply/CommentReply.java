package io.appwish.graphqlapi.dto.reply;

import io.appwish.graphqlapi.dto.Comment;
import io.appwish.graphqlapi.dto.User;
import io.appwish.grpc.VoteReplyProto;
import java.util.Objects;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;

/**
 * Represents data to return for single comment query.
 * <p>
 * {@link ProtoClass} and {@link ProtoField} annotations are used by {@link net.badata.protobuf.converter.Converter} to convert back/forth between
 * protobuf data transfer objects and model objects.
 * <p>
 * The converter requires a POJO with getters, setters and a default constructor.
 */
@ProtoClass(VoteReplyProto.class)
public class CommentReply {

  @ProtoField
  private Comment comment;

  private User author;

  public CommentReply() { }

  public CommentReply(Comment comment, User author) {
    this.comment = comment;
    this.author = author;
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentReply that = (CommentReply) o;
    return comment.equals(that.comment) &&
      author.equals(that.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(comment, author);
  }

  @Override
  public String toString() {
    return "CommentReply{" +
      "comment=" + comment +
      ", author=" + author +
      '}';
  }
}
