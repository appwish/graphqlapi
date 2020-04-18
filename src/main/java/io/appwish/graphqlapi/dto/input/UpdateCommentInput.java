package io.appwish.graphqlapi.dto.input;

// todo proto conv
public class UpdateCommentInput {

  private long id;
  private String content;

  public UpdateCommentInput(long id, String content) {
    this.id = id;
    this.content = content;
  }

  public long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }
}
