package io.appwish.graphqlapi.testutil;

import io.appwish.grpc.AppWish;
import io.appwish.grpc.AppWishList;

public class DummyData {
  public static final AppWish APP_WISH_1 = AppWish.newBuilder().setId("test-id-1").setTitle("test-title-1").setDescription("test-description-1").setCoverImageUrl("http://1-test.url.com").build();
  public static final AppWish APP_WISH_2 = AppWish.newBuilder().setId("test-id-2").setTitle("test-title-2").setDescription("test-description-2").setCoverImageUrl("http://2-test.url.com").build();
  public static final AppWish APP_WISH_3 = AppWish.newBuilder().setId("test-id-3").setTitle("test-title-3").setDescription("test-description-3").setCoverImageUrl("http://3-test.url.com").build();
  public static final AppWishList APP_WISH_LIST = AppWishList.newBuilder()
    .addAppWish(DummyData.APP_WISH_1)
    .addAppWish(DummyData.APP_WISH_2)
    .addAppWish(DummyData.APP_WISH_3)
    .build();
}
