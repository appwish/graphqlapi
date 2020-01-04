package io.appwish.graphqlapi.wiring;

import io.appwish.graphqlapi.fetcher.WishFetcher;
import io.appwish.graphqlapi.wiring.model.GraphQLType;

import java.util.List;

public class WishTypes implements GraphQLTypes {

  private static final String QUERY = "Query";
  private static final String APP_WISH = "appWish";
  private static final String ALL_APP_WISH = "allAppWish";

  private final WishFetcher wishFetcher;

  public WishTypes(final WishFetcher wishFetcher) {
    this.wishFetcher = wishFetcher;
  }

  @Override
  public List<GraphQLType> get() {
    final GraphQLType appWish = new GraphQLType(QUERY,
      builder -> builder.dataFetcher(APP_WISH, wishFetcher::findOne));

    final GraphQLType allAppWish = new GraphQLType(QUERY,
      builder -> builder.dataFetcher(ALL_APP_WISH, wishFetcher::findAll));

    return List.of(appWish, allAppWish);
  }
}
