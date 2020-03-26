package io.appwish.graphqlapi.dto;

public class Author {

  private final String id;

  public Author(final String authorId) {
    this.id = authorId;
  }

  public String getId() {
    return id;
  }
}
