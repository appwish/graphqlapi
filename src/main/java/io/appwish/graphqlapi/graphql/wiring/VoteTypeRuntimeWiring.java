package io.appwish.graphqlapi.graphql.wiring;

import graphql.schema.idl.TypeRuntimeWiring;
import io.appwish.graphqlapi.graphql.fetcher.VoteFetcher;
import java.util.List;

/**
 * Type runtime wiring is responsible for wiring GraphQL types with runtime. You need to specify how to serve given GraphQL query, for example - how
 * to fetch required data.
 */
public class VoteTypeRuntimeWiring implements TypeRuntimeWiringCollection {

  private static final String QUERY = "Query";
  private static final String MUTATION = "Mutation";
  private static final String VOTE = "vote";
  private static final String UNVOTE = "unvote";
  private static final String HAS_VOTED = "hasVoted";
  public static final String VOTE_SCORE = "voteScore";

  private final VoteFetcher voteFetcher;

  public VoteTypeRuntimeWiring(final VoteFetcher voteFetcher) {
    this.voteFetcher = voteFetcher;
  }

  /**
   * Returns a list of type-runtime wirings related to votes.
   */
  public List<TypeRuntimeWiring> getTypeRuntimeWirings() {
    final TypeRuntimeWiring vote = TypeRuntimeWiring.newTypeWiring(MUTATION, builder -> builder.dataFetcher(VOTE, voteFetcher::vote));
    final TypeRuntimeWiring unvote = TypeRuntimeWiring.newTypeWiring(MUTATION, builder -> builder.dataFetcher(UNVOTE, voteFetcher::unvote));
    final TypeRuntimeWiring hasVoted = TypeRuntimeWiring.newTypeWiring(QUERY, builder -> builder.dataFetcher(HAS_VOTED, voteFetcher::hasVoted));
    final TypeRuntimeWiring voteScore = TypeRuntimeWiring.newTypeWiring(QUERY, builder -> builder.dataFetcher(VOTE_SCORE, voteFetcher::voteScore));

    return List.of(vote, unvote, hasVoted, voteScore);
  }
}
