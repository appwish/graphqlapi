package io.appwish.graphqlapi.graphql.fetcher;

// TODO create separate package for data objects
public class Author {
  private final String id;

  public Author(final String authorId) {
    this.id = authorId;
  }

  public String getId() {
    return id;
  }
}
