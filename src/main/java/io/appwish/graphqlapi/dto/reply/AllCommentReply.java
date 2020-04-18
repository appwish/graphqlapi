package io.appwish.graphqlapi.dto.reply;

import io.appwish.graphqlapi.dto.Comment;
import java.util.List;

public class AllCommentReply {

  private final List<Comment> comments;

  public AllCommentReply(final List<Comment> comments) {
    this.comments = comments;
  }

  public List<Comment> getComments() {
    return comments;
  }
}
