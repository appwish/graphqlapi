package io.appwish.graphqlapi.dto;

import java.util.Objects;

// TODO missing attributes

/**
 * Represents user of the application.
 */
public class User {

  private final String id;

  public User(final String authorId) {
    this.id = authorId;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "User{" +
      "id='" + id + '\'' +
      '}';
  }
}
