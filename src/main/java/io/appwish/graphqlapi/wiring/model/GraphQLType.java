package io.appwish.graphqlapi.wiring.model;

import graphql.schema.idl.TypeRuntimeWiring;

import java.util.function.UnaryOperator;

public class GraphQLType {

  private final String typeName;
  private final UnaryOperator<TypeRuntimeWiring.Builder> typeBuilderFunction;

  public GraphQLType(final String typeName, final UnaryOperator<TypeRuntimeWiring.Builder> typeBuilderFunction) {
    this.typeName = typeName;
    this.typeBuilderFunction = typeBuilderFunction;
  }

  public String getTypeName() {
    return typeName;
  }

  public UnaryOperator<TypeRuntimeWiring.Builder> getTypeBuilderFunction() {
    return typeBuilderFunction;
  }
}
