package io.appwish.graphqlapi.graphql.wiring;

import graphql.schema.idl.TypeRuntimeWiring;
import io.appwish.graphqlapi.graphql.fetcher.CommentFetcher;
import java.util.List;

/**
 * Type runtime wiring is responsible for wiring GraphQL types with runtime. You need to specify how
 * to serve given GraphQL query, for example - how to fetch required data.
 */
public class CommentTypeRuntimeWiring implements TypeRuntimeWiringCollection {

  private static final String QUERY = "Query";
  private static final String MUTATION = "Mutation";
  private static final String CREATE_COMMENT = "createComment";
  private static final String ALL_COMMENTS = "allComments";
  private static final String DELETE_COMMENT = "deleteComment";
  private static final String UPDATE_COMMENT = "updateComment";

  private final CommentFetcher commentFetcher;

  public CommentTypeRuntimeWiring(final CommentFetcher commentFetcher) {
    this.commentFetcher = commentFetcher;
  }

  /**
   * Returns a list of type-runtime wirings related to comments
   */
  public List<TypeRuntimeWiring> getTypeRuntimeWirings() {
    final TypeRuntimeWiring createComment = TypeRuntimeWiring.newTypeWiring(MUTATION, builder -> builder.dataFetcher(CREATE_COMMENT, commentFetcher::comment));
    final TypeRuntimeWiring allComments = TypeRuntimeWiring.newTypeWiring(QUERY, builder -> builder.dataFetcher(ALL_COMMENTS, commentFetcher::allComments));
    final TypeRuntimeWiring updateComment = TypeRuntimeWiring.newTypeWiring(MUTATION, builder -> builder.dataFetcher(UPDATE_COMMENT, commentFetcher::updateComment));
    final TypeRuntimeWiring deleteComment = TypeRuntimeWiring.newTypeWiring(MUTATION, builder -> builder.dataFetcher(DELETE_COMMENT, commentFetcher::deleteComment));
    return List.of(createComment, allComments, deleteComment, updateComment);
  }
}
