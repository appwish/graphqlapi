package io.appwish.graphqlapi.verticle;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.appwish.graphqlapi.graphql.fetcher.WishFetcher;
import io.appwish.graphqlapi.graphql.schema.SchemaDefinitionProvider;
import io.appwish.graphqlapi.graphql.wiring.RuntimeWiringProvider;
import io.appwish.graphqlapi.graphql.wiring.TypeRuntimeWiringCollection;
import io.appwish.graphqlapi.graphql.wiring.WishTypeRuntimeWiring;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;

/**
 * Verticle exposing a GraphQL server.
 * */
public class GraphqlServerVerticle extends AbstractVerticle {

  public static final String GRAPHQL_ROUTE = "/graphql";
  public static final String GRAPHIQL_ROUTE = "/graphiql/*";

  private static final String ENV = "env";
  private static final String DEV_ENV = "dev";
  private static final String GRAPHQL_SERVER_PORT = "appPort";

  private final JsonObject config;

  public GraphqlServerVerticle(final JsonObject config) {
    this.config = config;
  }

  @Override
  public void start(final Promise<Void> startPromise) {
    final int graphqlServerPort = config.getInteger(GRAPHQL_SERVER_PORT);
    // schema definition
    final SchemaGenerator schemaGenerator = new SchemaGenerator();
    final SchemaDefinitionProvider schemaDefinitionProvider = new SchemaDefinitionProvider(vertx, config);
    final TypeDefinitionRegistry definitionRegistry = schemaDefinitionProvider.createSchemaDefinition();

    // type runtime wiring
    final WishFetcher wishFetcher = new WishFetcher(vertx.eventBus());
    final TypeRuntimeWiringCollection wishTypes = new WishTypeRuntimeWiring(wishFetcher);
    final RuntimeWiringProvider runtimeWiringProvider = new RuntimeWiringProvider(wishTypes);

    final GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(definitionRegistry, runtimeWiringProvider.createRuntimeWiring());
    final GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    final GraphiQLHandlerOptions graphiqlOptions = new GraphiQLHandlerOptions().setEnabled(true);

    final Router router = prepareRouter(vertx, graphQL, graphiqlOptions, config);
    vertx.createHttpServer().requestHandler(router::accept).listen(graphqlServerPort);
    startPromise.complete();
  }

  private Router prepareRouter(final Vertx vertx, final GraphQL graphQL, final GraphiQLHandlerOptions options, final JsonObject config) {
    final Router router = Router.router(vertx);
    final String environment = config.getString(ENV);

    if (environment.equals(DEV_ENV)) {
      router.route(GRAPHIQL_ROUTE).handler(GraphiQLHandler.create(options));
    }

    router.route(GRAPHQL_ROUTE).handler(GraphQLHandler.create(graphQL));

    return router;
  }
}
