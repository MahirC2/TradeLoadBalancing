package com.trading.servers;

import com.trading.services.PricingService;
import io.grpc.Server;
import io.vertx.core.AbstractVerticle;
import io.vertx.grpc.VertxServerBuilder;

import java.io.IOException;

public class PricingServiceVerticle extends AbstractVerticle {
    private static final int PORT = 50052;

    @Override
    public void start() throws IOException {
        Server server = VertxServerBuilder
                .forAddress(vertx, "localhost", PORT)
                .addService(new PricingService(vertx))
                .build();

        server.start();
        System.out.println("Pricing gRPC server started.");
    }
}
