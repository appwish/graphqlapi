package io.appwish.graphqlapi.wiring;

import io.appwish.graphqlapi.fetcher.WishFetcher;
import io.appwish.graphqlapi.wiring.model.GraphQLType;

import java.util.List;

public class WishTypes implements GraphQLTypes {

  private final WishFetcher wishFetcher;

  public WishTypes(final WishFetcher wishFetcher) {
    this.wishFetcher = wishFetcher;
  }

  @Override
  public List<GraphQLType> get() {
    final GraphQLType appWish = new GraphQLType("Query",
      builder -> builder.dataFetcher("appWish", wishFetcher::findOne));

    final GraphQLType allAppWish = new GraphQLType("Query",
      builder -> builder.dataFetcher("allAppWish", wishFetcher::findAll));

    return List.of(appWish, allAppWish);
  }
}
