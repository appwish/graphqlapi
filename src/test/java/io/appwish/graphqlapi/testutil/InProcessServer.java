package io.appwish.graphqlapi.testutil;

import io.grpc.Server;
import io.grpc.inprocess.InProcessServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class InProcessServer<T extends io.grpc.BindableService> {

  private static final Logger LOG = Logger.getLogger(InProcessServer.class.getName());

  private final Class<T> clazz;
  private Server server;

  public InProcessServer(final Class<T> clazz) {
    this.clazz = clazz;
  }

  public void start(final String forName) throws IOException, InstantiationException, IllegalAccessException {
    server = InProcessServerBuilder
      .forName(forName)
      .directExecutor()
      .addService(clazz.newInstance())
      .build()
      .start();

    LOG.info("InProcessServer started");

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      // Using stderr here since the LOG may have been reset by its JVM shutdown hook
      System.err.println("Shutting down gRPC server since JVM is shutting down");
      InProcessServer.this.stop();
      System.err.println("Server shut down");
    }));
  }

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }
}
