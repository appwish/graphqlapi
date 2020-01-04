package io.appwish.graphqlapi.wiring;

import io.appwish.graphqlapi.fetcher.WishFetcher;
import io.appwish.graphqlapi.wiring.model.GraphQLType;

import java.util.List;

public class WishTypes implements GraphQLTypes {

  private static final String QUERY = "Query";
  private static final String WISH = "wish";
  private static final String ALL_WISH = "allWish";

  private final WishFetcher wishFetcher;

  public WishTypes(final WishFetcher wishFetcher) {
    this.wishFetcher = wishFetcher;
  }

  @Override
  public List<GraphQLType> get() {
    final GraphQLType wish = new GraphQLType(QUERY,
      builder -> builder.dataFetcher(WISH, wishFetcher::findOne));

    final GraphQLType allWish = new GraphQLType(QUERY,
      builder -> builder.dataFetcher(ALL_WISH, wishFetcher::findAll));

    return List.of(wish, allWish);
  }
}
