package io.appwish.graphqlapi.eventbus;

/**
 * Represents all addresses that may be exposed on the event bus.
 */
public enum Address {
  ALL_WISH("wishservice.get.all.wish"),
  WISH("wishservice.get.one.wish"),
  CREATE_WISH("wishservice.create.one.wish"),
  UPDATE_WISH("wishservice.update.one.wish"),
  DELETE_WISH("wishservice.delete.one.wish");

  private String value;

  Address(final String value) {
    this.value = value;
  }

  public String get() {
    return value;
  }
}
