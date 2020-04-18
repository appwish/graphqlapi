package io.appwish.graphqlapi.eventbus;

/**
 * Represents all addresses that may be exposed on the event bus.
 */
public enum Address {
  ALL_WISH,
  WISH,
  CREATE_WISH,
  UPDATE_WISH,
  DELETE_WISH,
  VOTE,
  UNVOTE,
  HAS_VOTED,
  VOTE_SCORE,
  COMMENT,
  ALL_COMMENTS,
  DELETE_COMMENT,
  UPDATE_COMMENT;

  public String get() {
    return name();
  }
}
