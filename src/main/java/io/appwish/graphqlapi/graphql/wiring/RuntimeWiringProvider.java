package io.appwish.graphqlapi.graphql.wiring;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;
import java.util.ArrayList;
import java.util.List;

/**
 * Assembles collections of type-runtime wirings. Merges them all into one runtime wiring.
 */
public class RuntimeWiringProvider {

  private final List<TypeRuntimeWiring> typeRuntimeWirings;

  public RuntimeWiringProvider(final TypeRuntimeWiringCollection... typeRuntimeWiringCollections) {
    this.typeRuntimeWirings = new ArrayList<>();

    for (final TypeRuntimeWiringCollection collection : typeRuntimeWiringCollections) {
      this.typeRuntimeWirings.addAll(collection.getTypeRuntimeWirings());
    }
  }

  public RuntimeWiring createRuntimeWiring() {
    final RuntimeWiring.Builder wiringBuilder = newRuntimeWiring();
    typeRuntimeWirings.forEach(wiringBuilder::type);
    return wiringBuilder.build();
  }
}
