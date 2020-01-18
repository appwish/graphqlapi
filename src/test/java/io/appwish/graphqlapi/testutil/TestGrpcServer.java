package io.appwish.graphqlapi.testutil;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

/**
 * Simple gRPC server that can be used to mock responses from other services for testing purposes.
 */
public class TestGrpcServer<T extends io.grpc.BindableService> {

  private final Server server;

  public TestGrpcServer(final Class<T> clazz, final int port) throws IllegalAccessException, InstantiationException {
    this.server = ServerBuilder
      .forPort(port)
      .directExecutor()
      .addService(clazz.newInstance())
      .build();
  }

  public void start() throws IOException {
    server.start();
    Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
  }

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }
}
