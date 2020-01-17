package io.appwish.graphqlapi.graphql.wiring;

import graphql.schema.idl.TypeRuntimeWiring;
import io.appwish.graphqlapi.graphql.fetcher.WishFetcher;
import java.util.List;

/**
 * Type runtime wiring is responsible for wiring GraphQL types with runtime. You need to specify how
 * to serve given GraphQL type, for example - how to fetch required data
 */
public class WishTypeRuntimeWiring implements TypeRuntimeWiringCollection {

  private static final String QUERY = "Query";
  private static final String MUTATION = "Mutation";
  private static final String WISH = "wish";
  private static final String ALL_WISH = "allWish";
  private static final String CREATE_WISH = "createWish";

  private final WishFetcher wishFetcher;

  public WishTypeRuntimeWiring(final WishFetcher wishFetcher) {
    this.wishFetcher = wishFetcher;
  }

  /**
   * Returns a list of type-runtime wirings related to wishes
   */
  public List<TypeRuntimeWiring> getTypeRuntimeWirings() {
    final TypeRuntimeWiring wish = TypeRuntimeWiring.newTypeWiring(QUERY, builder -> builder.dataFetcher(WISH, wishFetcher::wish));
    final TypeRuntimeWiring allWish = TypeRuntimeWiring.newTypeWiring(QUERY, builder -> builder.dataFetcher(ALL_WISH, wishFetcher::allWish));
    final TypeRuntimeWiring createWish = TypeRuntimeWiring.newTypeWiring(MUTATION, builder -> builder.dataFetcher(CREATE_WISH, wishFetcher::createWish));
    return List.of(wish, allWish, createWish);
  }
}
