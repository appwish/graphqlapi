package io.appwish.graphqlapi.graphql.wiring;

import graphql.schema.idl.TypeRuntimeWiring;
import io.appwish.graphqlapi.graphql.fetcher.VoteFetcher;
import java.util.List;

/**
 * Type runtime wiring is responsible for wiring GraphQL types with runtime. You need to specify how
 * to serve given GraphQL query, for example - how to fetch required data.
 */
public class VoteTypeRuntimeWiring implements TypeRuntimeWiringCollection {

  private static final String QUERY = "Query";
  private static final String MUTATION = "Mutation";
  private static final String VOTE = "vote";

  private final VoteFetcher voteFetcher;

  public VoteTypeRuntimeWiring(final VoteFetcher voteFetcher) {
    this.voteFetcher = voteFetcher;
  }

  /**
   * Returns a list of type-runtime wirings related to votes.
   */
  public List<TypeRuntimeWiring> getTypeRuntimeWirings() {
    final TypeRuntimeWiring vote = TypeRuntimeWiring.newTypeWiring(MUTATION, builder -> builder.dataFetcher(VOTE, voteFetcher::vote));
    return List.of(vote);
  }
}
