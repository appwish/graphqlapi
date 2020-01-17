package io.appwish.graphqlapi.graphql.schema;

import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for parsing GraphQL schema from resource files
 */
public class SchemaDefinitionProvider {

  private static final String SCHEMA_DIR_PATH = "schemaDirPath";

  private final Vertx vertx;
  private final JsonObject config;

  public SchemaDefinitionProvider(final Vertx vertx, final JsonObject config) {
    this.vertx = vertx;
    this.config = config;
  }

  /**
   * Parses resource files to build GraphQL schema registry
   */
  public TypeDefinitionRegistry createSchemaDefinition() {
    final String schemaDirPath = config.getString(SCHEMA_DIR_PATH);
    final List<String> schemaFilePaths = vertx.fileSystem().readDirBlocking(schemaDirPath);
    final TypeDefinitionRegistry registry = new TypeDefinitionRegistry();
    final SchemaParser schemaParser = new SchemaParser();

    final List<TypeDefinitionRegistry> typeDefinitionRegistries = schemaFilePaths.stream()
      .map(File::new)
      .map(schemaParser::parse)
      .collect(Collectors.toList());

    typeDefinitionRegistries.forEach(registry::merge);

    return registry;
  }
}
