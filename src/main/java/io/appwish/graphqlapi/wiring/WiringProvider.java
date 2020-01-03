package io.appwish.graphqlapi.wiring;

import graphql.schema.idl.RuntimeWiring;
import io.appwish.graphqlapi.wiring.model.GraphQLType;

import java.util.ArrayList;
import java.util.List;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

public class WiringProvider {

  private final List<GraphQLType> graphQLTypes;

  public WiringProvider(final GraphQLTypes... graphQLTypes) {
    this.graphQLTypes = new ArrayList<>();

    for (final GraphQLTypes types : graphQLTypes) {
      this.graphQLTypes.addAll(types.get());
    }
  }

  public RuntimeWiring runtimeWiring() {
    final RuntimeWiring.Builder wiringBuilder = newRuntimeWiring();
    graphQLTypes.forEach(graphQLType -> wiringBuilder.type(graphQLType.getTypeName(), graphQLType.getTypeBuilderFunction()));
    return wiringBuilder.build();
  }
}
