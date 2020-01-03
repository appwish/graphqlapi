package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.Wish;

import java.util.List;

public class DummyData {
  public static final Wish WISH_1 = Wish.newBuilder().setId("test-id-1").setTitle("test-title-1").setDescription("test-description-1").setCoverImageUrl("http://1-test.url.com").build();
  public static final Wish WISH_2 = Wish.newBuilder().setId("test-id-2").setTitle("test-title-2").setDescription("test-description-2").setCoverImageUrl("http://2-test.url.com").build();
  public static final Wish WISH_3 = Wish.newBuilder().setId("test-id-3").setTitle("test-title-3").setDescription("test-description-3").setCoverImageUrl("http://3-test.url.com").build();
  public static final List<Wish> WISHES = List.of(WISH_1, WISH_2, WISH_3);
}
