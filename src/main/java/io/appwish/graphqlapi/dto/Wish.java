package io.appwish.graphqlapi.dto;

import io.appwish.grpc.WishProto;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;

// TODO add support to use with converter

/**
 * Represents single user's wish.
 * <p>
 * {@link ProtoClass} and {@link ProtoField} annotations are used by {@link net.badata.protobuf.converter.Converter} to convert back/forth between
 * protobuf data transfer objects and model objects.
 * <p>
 * The converter requires a POJO with getters, setters and a default constructor.
 */
public class Wish {

  private final long id;
  private final String title;
  private final String markdown;
  private final String coverImageUrl;
  private final User user;
  private final OffsetDateTime createdAt;
  private final OffsetDateTime updatedAt;

  public Wish(final WishProto proto) {
    final LocalDateTime updatedAt = LocalDateTime
      .from(LocalDateTime.ofInstant(Instant.ofEpochSecond(proto.getUpdatedAt().getSeconds()), ZoneOffset.UTC));
    final LocalDateTime createdAt = LocalDateTime
      .from(LocalDateTime.ofInstant(Instant.ofEpochSecond(proto.getCreatedAt().getSeconds()), ZoneOffset.UTC));
    this.createdAt = OffsetDateTime.of(createdAt, ZoneOffset.UTC);
    this.updatedAt = OffsetDateTime.of(updatedAt, ZoneOffset.UTC);
    this.user = new User(proto.getAuthorId());
    this.id = proto.getId();
    this.title = proto.getTitle();
    this.markdown = proto.getMarkdown();
    this.coverImageUrl = proto.getCoverImageUrl();
  }

  public String getTitle() {
    return title;
  }

  public User getUser() {
    return user;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Wish wish = (Wish) o;
    return id == wish.id &&
      title.equals(wish.title) &&
      markdown.equals(wish.markdown) &&
      Objects.equals(coverImageUrl, wish.coverImageUrl) &&
      user.equals(wish.user) &&
      createdAt.equals(wish.createdAt) &&
      updatedAt.equals(wish.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, markdown, coverImageUrl, user, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return "Wish{" +
      "id=" + id +
      ", title='" + title + '\'' +
      ", markdown='" + markdown + '\'' +
      ", coverImageUrl='" + coverImageUrl + '\'' +
      ", user=" + user +
      ", createdAt=" + createdAt +
      ", updatedAt=" + updatedAt +
      '}';
  }
}
