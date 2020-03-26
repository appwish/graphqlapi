package io.appwish.graphqlapi.dto;

import java.util.Objects;

public class Author {

  private final String id;

  public Author(final String authorId) {
    this.id = authorId;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Author author = (Author) o;
    return id.equals(author.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
