package com.trading.servers;

import com.trading.TraderRestAPI;
import io.vertx.core.Vertx;

public class ServerLauncher {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new OrderEngineServiceVerticle(), res -> {
            if (res.succeeded()) {
                System.out.println("OrderEngineServiceVerticle deployed successfully.");
            } else {
                System.err.println("Failed to deploy OrderEngineServiceVerticle: " + res.cause());
            }
        });

        vertx.deployVerticle(new PricingServiceVerticle(), res -> {
            if (res.succeeded()) {
                System.out.println("PricingServiceVerticle deployed successfully.");
            } else {
                System.err.println("Failed to deploy PricingServiceVerticle: " + res.cause());
            }
        });

        vertx.deployVerticle(new TraderRestAPI());
    }
}