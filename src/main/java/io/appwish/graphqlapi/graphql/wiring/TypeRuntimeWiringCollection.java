package io.appwish.graphqlapi.graphql.wiring;

import graphql.schema.idl.TypeRuntimeWiring;
import java.util.List;

/**
 * Represents collection of GraphQL type-runtime wirings.
 */
public interface TypeRuntimeWiringCollection {

  List<TypeRuntimeWiring> getTypeRuntimeWirings();
}
