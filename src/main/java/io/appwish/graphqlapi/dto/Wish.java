package io.appwish.graphqlapi.dto;

import io.appwish.grpc.WishProto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Wish {

  private final long id;
  private final String title;
  private final String markdown;
  private final String coverImageUrl;
  private final Author author;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;

  public Wish(final WishProto proto) {
    final LocalDateTime updatedAt = LocalDateTime.from(LocalDateTime.ofInstant(Instant.ofEpochSecond(proto.getUpdatedAt().getSeconds()), ZoneOffset.UTC));
    final LocalDateTime createdAt = LocalDateTime.from(LocalDateTime.ofInstant(Instant.ofEpochSecond(proto.getCreatedAt().getSeconds()), ZoneOffset.UTC));
    this.createdAt = OffsetDateTime.of(createdAt, ZoneOffset.UTC);
    this.updatedAt = OffsetDateTime.of(updatedAt, ZoneOffset.UTC);
    this.author = new Author(proto.getAuthorId());
    this.id = proto.getId();
    this.title = proto.getTitle();
    this.markdown = proto.getMarkdown();
    this.coverImageUrl = proto.getCoverImageUrl();
  }

  public String getTitle() {
    return title;
  }

  public Author getAuthor() {
    return author;
  }

  public String getMarkdown() {
    return markdown;
  }

  public String getCoverImageUrl() {
    return coverImageUrl;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public long getId() {
    return id;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }
}
