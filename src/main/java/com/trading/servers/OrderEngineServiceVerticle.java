package com.trading.servers;

import com.trading.services.OrderEngineService;
import io.grpc.Server;
import io.vertx.core.AbstractVerticle;
import io.vertx.grpc.VertxServerBuilder;

import java.io.IOException;

public class OrderEngineServiceVerticle extends AbstractVerticle {
    private static final int PORT = 50051;

    @Override
    public void start() throws IOException {
        Server server = VertxServerBuilder
                .forAddress(vertx, "localhost", PORT)
                .addService(new OrderEngineService())
                .build();

        server.start();
        System.out.println("OrderEngine gRPC server started.");
    }
}
