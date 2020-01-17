package io.appwish.graphqlapi.eventbus;

/**
 * Represents all addresses that may be exposed on the event bus.
 */
public enum Address {
  ALL_WISH("wishservice.get.all"),
  WISH("wishservice.get.one"),
  CREATE_WISH("wishservice.add.one"),
  UPDATE_WISH("wishservice.update.one"),
  DELETE_WISH("wishservice.delete.one");

  private String value;

  Address(final String value) {
    this.value = value;
  }

  public String get() {
    return value;
  }
}
