package io.appwish.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.20.0)",
    comments = "Source: appwish.proto")
public final class AppWishServiceGrpc {

  private AppWishServiceGrpc() {}

  private static <T> io.grpc.stub.StreamObserver<T> toObserver(final io.vertx.core.Handler<io.vertx.core.AsyncResult<T>> handler) {
    return new io.grpc.stub.StreamObserver<T>() {
      private volatile boolean resolved = false;
      @Override
      public void onNext(T value) {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.succeededFuture(value));
        }
      }

      @Override
      public void onError(Throwable t) {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.failedFuture(t));
        }
      }

      @Override
      public void onCompleted() {
        if (!resolved) {
          resolved = true;
          handler.handle(io.vertx.core.Future.succeededFuture());
        }
      }
    };
  }

  public static final String SERVICE_NAME = "wishservice.AppWishService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<io.appwish.grpc.AllAppWishQuery,
      io.appwish.grpc.AllAppWishReply> getGetAllAppWishMethod;

  public static io.grpc.MethodDescriptor<io.appwish.grpc.AllAppWishQuery,
      io.appwish.grpc.AllAppWishReply> getGetAllAppWishMethod() {
    io.grpc.MethodDescriptor<io.appwish.grpc.AllAppWishQuery, io.appwish.grpc.AllAppWishReply> getGetAllAppWishMethod;
    if ((getGetAllAppWishMethod = AppWishServiceGrpc.getGetAllAppWishMethod) == null) {
      synchronized (AppWishServiceGrpc.class) {
        if ((getGetAllAppWishMethod = AppWishServiceGrpc.getGetAllAppWishMethod) == null) {
          AppWishServiceGrpc.getGetAllAppWishMethod = getGetAllAppWishMethod = 
              io.grpc.MethodDescriptor.<io.appwish.grpc.AllAppWishQuery, io.appwish.grpc.AllAppWishReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "wishservice.AppWishService", "GetAllAppWish"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.appwish.grpc.AllAppWishQuery.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.appwish.grpc.AllAppWishReply.getDefaultInstance()))
                  .setSchemaDescriptor(new AppWishServiceMethodDescriptorSupplier("GetAllAppWish"))
                  .build();
          }
        }
     }
     return getGetAllAppWishMethod;
  }

  private static volatile io.grpc.MethodDescriptor<io.appwish.grpc.AppWishQuery,
      io.appwish.grpc.AppWishReply> getGetAppWishMethod;

  public static io.grpc.MethodDescriptor<io.appwish.grpc.AppWishQuery,
      io.appwish.grpc.AppWishReply> getGetAppWishMethod() {
    io.grpc.MethodDescriptor<io.appwish.grpc.AppWishQuery, io.appwish.grpc.AppWishReply> getGetAppWishMethod;
    if ((getGetAppWishMethod = AppWishServiceGrpc.getGetAppWishMethod) == null) {
      synchronized (AppWishServiceGrpc.class) {
        if ((getGetAppWishMethod = AppWishServiceGrpc.getGetAppWishMethod) == null) {
          AppWishServiceGrpc.getGetAppWishMethod = getGetAppWishMethod = 
              io.grpc.MethodDescriptor.<io.appwish.grpc.AppWishQuery, io.appwish.grpc.AppWishReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "wishservice.AppWishService", "GetAppWish"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.appwish.grpc.AppWishQuery.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.appwish.grpc.AppWishReply.getDefaultInstance()))
                  .setSchemaDescriptor(new AppWishServiceMethodDescriptorSupplier("GetAppWish"))
                  .build();
          }
        }
     }
     return getGetAppWishMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AppWishServiceStub newStub(io.grpc.Channel channel) {
    return new AppWishServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AppWishServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new AppWishServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AppWishServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new AppWishServiceFutureStub(channel);
  }

  /**
   * Creates a new vertx stub that supports all call types for the service
   */
  public static AppWishServiceVertxStub newVertxStub(io.grpc.Channel channel) {
    return new AppWishServiceVertxStub(channel);
  }

  /**
   */
  public static abstract class AppWishServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getAllAppWish(io.appwish.grpc.AllAppWishQuery request,
        io.grpc.stub.StreamObserver<io.appwish.grpc.AllAppWishReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAllAppWishMethod(), responseObserver);
    }

    /**
     */
    public void getAppWish(io.appwish.grpc.AppWishQuery request,
        io.grpc.stub.StreamObserver<io.appwish.grpc.AppWishReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAppWishMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetAllAppWishMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                io.appwish.grpc.AllAppWishQuery,
                io.appwish.grpc.AllAppWishReply>(
                  this, METHODID_GET_ALL_APP_WISH)))
          .addMethod(
            getGetAppWishMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                io.appwish.grpc.AppWishQuery,
                io.appwish.grpc.AppWishReply>(
                  this, METHODID_GET_APP_WISH)))
          .build();
    }
  }

  /**
   */
  public static final class AppWishServiceStub extends io.grpc.stub.AbstractStub<AppWishServiceStub> {
    public AppWishServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    public AppWishServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AppWishServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AppWishServiceStub(channel, callOptions);
    }

    /**
     */
    public void getAllAppWish(io.appwish.grpc.AllAppWishQuery request,
        io.grpc.stub.StreamObserver<io.appwish.grpc.AllAppWishReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAllAppWishMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAppWish(io.appwish.grpc.AppWishQuery request,
        io.grpc.stub.StreamObserver<io.appwish.grpc.AppWishReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAppWishMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class AppWishServiceBlockingStub extends io.grpc.stub.AbstractStub<AppWishServiceBlockingStub> {
    public AppWishServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    public AppWishServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AppWishServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AppWishServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public io.appwish.grpc.AllAppWishReply getAllAppWish(io.appwish.grpc.AllAppWishQuery request) {
      return blockingUnaryCall(
          getChannel(), getGetAllAppWishMethod(), getCallOptions(), request);
    }

    /**
     */
    public io.appwish.grpc.AppWishReply getAppWish(io.appwish.grpc.AppWishQuery request) {
      return blockingUnaryCall(
          getChannel(), getGetAppWishMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class AppWishServiceFutureStub extends io.grpc.stub.AbstractStub<AppWishServiceFutureStub> {
    public AppWishServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    public AppWishServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AppWishServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AppWishServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.appwish.grpc.AllAppWishReply> getAllAppWish(
        io.appwish.grpc.AllAppWishQuery request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAllAppWishMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.appwish.grpc.AppWishReply> getAppWish(
        io.appwish.grpc.AppWishQuery request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAppWishMethod(), getCallOptions()), request);
    }
  }

  /**
   */
  public static abstract class AppWishServiceVertxImplBase implements io.grpc.BindableService {

    /**
     */
    public void getAllAppWish(io.appwish.grpc.AllAppWishQuery request,
        io.vertx.core.Promise<io.appwish.grpc.AllAppWishReply> response) {
      asyncUnimplementedUnaryCall(getGetAllAppWishMethod(), AppWishServiceGrpc.toObserver(response));
    }

    /**
     */
    public void getAppWish(io.appwish.grpc.AppWishQuery request,
        io.vertx.core.Promise<io.appwish.grpc.AppWishReply> response) {
      asyncUnimplementedUnaryCall(getGetAppWishMethod(), AppWishServiceGrpc.toObserver(response));
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetAllAppWishMethod(),
            asyncUnaryCall(
              new VertxMethodHandlers<
                io.appwish.grpc.AllAppWishQuery,
                io.appwish.grpc.AllAppWishReply>(
                  this, METHODID_GET_ALL_APP_WISH)))
          .addMethod(
            getGetAppWishMethod(),
            asyncUnaryCall(
              new VertxMethodHandlers<
                io.appwish.grpc.AppWishQuery,
                io.appwish.grpc.AppWishReply>(
                  this, METHODID_GET_APP_WISH)))
          .build();
    }
  }

  /**
   */
  public static final class AppWishServiceVertxStub extends io.grpc.stub.AbstractStub<AppWishServiceVertxStub> {
    public AppWishServiceVertxStub(io.grpc.Channel channel) {
      super(channel);
    }

    public AppWishServiceVertxStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AppWishServiceVertxStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AppWishServiceVertxStub(channel, callOptions);
    }

    /**
     */
    public void getAllAppWish(io.appwish.grpc.AllAppWishQuery request,
        io.vertx.core.Handler<io.vertx.core.AsyncResult<io.appwish.grpc.AllAppWishReply>> response) {
      asyncUnaryCall(
          getChannel().newCall(getGetAllAppWishMethod(), getCallOptions()), request, AppWishServiceGrpc.toObserver(response));
    }

    /**
     */
    public void getAppWish(io.appwish.grpc.AppWishQuery request,
        io.vertx.core.Handler<io.vertx.core.AsyncResult<io.appwish.grpc.AppWishReply>> response) {
      asyncUnaryCall(
          getChannel().newCall(getGetAppWishMethod(), getCallOptions()), request, AppWishServiceGrpc.toObserver(response));
    }
  }

  private static final int METHODID_GET_ALL_APP_WISH = 0;
  private static final int METHODID_GET_APP_WISH = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AppWishServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AppWishServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ALL_APP_WISH:
          serviceImpl.getAllAppWish((io.appwish.grpc.AllAppWishQuery) request,
              (io.grpc.stub.StreamObserver<io.appwish.grpc.AllAppWishReply>) responseObserver);
          break;
        case METHODID_GET_APP_WISH:
          serviceImpl.getAppWish((io.appwish.grpc.AppWishQuery) request,
              (io.grpc.stub.StreamObserver<io.appwish.grpc.AppWishReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class VertxMethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AppWishServiceVertxImplBase serviceImpl;
    private final int methodId;

    VertxMethodHandlers(AppWishServiceVertxImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ALL_APP_WISH:
          serviceImpl.getAllAppWish((io.appwish.grpc.AllAppWishQuery) request,
              (io.vertx.core.Promise<io.appwish.grpc.AllAppWishReply>) io.vertx.core.Promise.<io.appwish.grpc.AllAppWishReply>promise().future().setHandler(ar -> {
                if (ar.succeeded()) {
                  ((io.grpc.stub.StreamObserver<io.appwish.grpc.AllAppWishReply>) responseObserver).onNext(ar.result());
                  responseObserver.onCompleted();
                } else {
                  responseObserver.onError(ar.cause());
                }
              }));
          break;
        case METHODID_GET_APP_WISH:
          serviceImpl.getAppWish((io.appwish.grpc.AppWishQuery) request,
              (io.vertx.core.Promise<io.appwish.grpc.AppWishReply>) io.vertx.core.Promise.<io.appwish.grpc.AppWishReply>promise().future().setHandler(ar -> {
                if (ar.succeeded()) {
                  ((io.grpc.stub.StreamObserver<io.appwish.grpc.AppWishReply>) responseObserver).onNext(ar.result());
                  responseObserver.onCompleted();
                } else {
                  responseObserver.onError(ar.cause());
                }
              }));
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class AppWishServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AppWishServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.appwish.grpc.GRPC.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AppWishService");
    }
  }

  private static final class AppWishServiceFileDescriptorSupplier
      extends AppWishServiceBaseDescriptorSupplier {
    AppWishServiceFileDescriptorSupplier() {}
  }

  private static final class AppWishServiceMethodDescriptorSupplier
      extends AppWishServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AppWishServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AppWishServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AppWishServiceFileDescriptorSupplier())
              .addMethod(getGetAllAppWishMethod())
              .addMethod(getGetAppWishMethod())
              .build();
        }
      }
    }
    return result;
  }
}
