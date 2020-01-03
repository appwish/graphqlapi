package io.appwish.graphqlapi;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.appwish.graphqlapi.fetcher.WishFetcher;
import io.appwish.graphqlapi.grpc.ServiceStubs;
import io.appwish.graphqlapi.schema.SchemaDefinitionProvider;
import io.appwish.graphqlapi.wiring.GraphQLTypes;
import io.appwish.graphqlapi.wiring.WiringProvider;
import io.appwish.graphqlapi.wiring.WishTypes;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;

public class GraphQLVerticle extends AbstractVerticle {

  private static final String GRAPHQL_ROUTE = "/graphql";
  private static final String GRAPHIQL_ROUTE = "/graphiql/*";

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    final ConfigRetriever retriever = ConfigRetriever.create(vertx);

    retriever.getConfig(json -> {
      final JsonObject config = json.result();
      final int appPort = config.getInteger("appPort");

      // gRPC
      final ServiceStubs serviceStubs = new ServiceStubs(vertx, config);

      // GraphQL
      final SchemaGenerator schemaGenerator = new SchemaGenerator();
      final SchemaDefinitionProvider schemaDefinitionProvider = new SchemaDefinitionProvider(vertx, config);
      final TypeDefinitionRegistry definitionRegistry = schemaDefinitionProvider.createSchemaDefinition();
      final RuntimeWiring runtimeWiring = prepareWiring(serviceStubs);
      final GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(definitionRegistry, runtimeWiring);
      final GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();
      final GraphiQLHandlerOptions options = new GraphiQLHandlerOptions().setEnabled(true);

      // HTTP
      final Router router = prepareRouter(vertx, graphQL, options, config);
      vertx.createHttpServer().requestHandler(router::accept).listen(appPort);
      startPromise.complete();
    });
  }

  private RuntimeWiring prepareWiring(final ServiceStubs serviceStubs) {
    final WishFetcher wishFetcher = new WishFetcher(serviceStubs.wishServiceStub());
    final GraphQLTypes wishTypes = new WishTypes(wishFetcher);
    final WiringProvider wiringProvider = new WiringProvider(wishTypes);

    return wiringProvider.runtimeWiring();
  }

  private Router prepareRouter(final Vertx vertx, final GraphQL graphQL, final GraphiQLHandlerOptions options, final JsonObject config) {
    final Router router = Router.router(vertx);
    final String env = config.getString("env");

    if (env.equals("dev")) {
      router.route(GRAPHIQL_ROUTE).handler(GraphiQLHandler.create(options));
    }

    router.route(GRAPHQL_ROUTE).handler(GraphQLHandler.create(graphQL));

    return router;
  }
}
