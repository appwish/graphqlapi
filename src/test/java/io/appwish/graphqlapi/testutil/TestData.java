package io.appwish.graphqlapi.testutil;


import io.appwish.grpc.WishProto;
import java.util.List;

/**
 * Some random wishes and values that can be used to remove repetition in mocking data in tests
 */
public class TestData {
  public static final WishProto WISH_1 = WishProto.newBuilder().setId(1).setTitle("test-title-1").setMarkdown("test-description-1").setCoverImageUrl("http://1-test.url.com").setAuthorId(1).setSlug("/post-1").build();
  public static final WishProto WISH_2 = WishProto.newBuilder().setId(2).setTitle("test-title-2").setMarkdown("test-description-2").setCoverImageUrl("http://2-test.url.com").setAuthorId(2).setSlug("/post-1").build();
  public static final WishProto WISH_3 = WishProto.newBuilder().setId(3).setTitle("test-title-3").setMarkdown("test-description-3").setCoverImageUrl("http://3-test.url.com").setAuthorId(3).setSlug("/post-1").build();
  public static final List<WishProto> WISHES = List.of(WISH_1, WISH_2, WISH_3);
  public static final String SOME_TITLE = "my title";
  public static final String SOME_URL = "my url";
  public static final String SOME_MARKDOWN = "# Hello World!";
  public static final int INVALID_ID = 123123909;
}
