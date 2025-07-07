package com.trading;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import order.OrderProto;
import order.OrderServiceGrpc;
import pricing.PricingProto;
import pricing.PricingServiceGrpc;

import java.time.Instant;

public class TraderRestAPI extends AbstractVerticle {

    private PricingServiceGrpc.PricingServiceBlockingStub pricingStub;
    private OrderServiceGrpc.OrderServiceBlockingStub orderStub;

    @Override
    public void start() {
        ManagedChannel pricingChannel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext().build();
        ManagedChannel orderChannel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

        pricingStub = PricingServiceGrpc.newBlockingStub(pricingChannel);
        orderStub = OrderServiceGrpc.newBlockingStub(orderChannel);

        Router router = Router.router(vertx);

        router.get("/price").handler(this::handleGetPrice);
        router.post("/order").handler(this::handlePlaceOrder);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080)
                .onSuccess(server -> System.out.println("HTTP API listening on port 8080"));
    }

    private void handleGetPrice(RoutingContext ctx) {
        String symbol = ctx.request().getParam("symbol");
        String currency = ctx.request().getParam("currency");

        PricingProto.PriceRequest req = PricingProto.PriceRequest.newBuilder()
                .setTickerSymbol(symbol)
                .setCurrency(currency)
                .setTimestamp(Instant.now().toString())
                .build();

        PricingProto.PriceResponse res = pricingStub.getPrice(req);

        JsonObject responseJson = new JsonObject()
                .put("symbol", res.getTickerSymbol())
                .put("currency", res.getCurrency())
                .put("price", res.getPrice())
                .put("asOf", res.getAsOf())
                .put("status", res.getStatus());

        ctx.json(responseJson);
    }

    private void handlePlaceOrder(RoutingContext ctx) {
        ctx.request().body().onSuccess(body -> {
            JsonObject json = body.toJsonObject();
            OrderProto.OrderRequest req = OrderProto.OrderRequest.newBuilder()
                    .setOrderId("ORD" + System.currentTimeMillis())
                    .setSymbol(json.getString("symbol"))
                    .setQty(json.getDouble("qty"))
                    .setType(OrderProto.OrderRequest.OrderType.MARKET)
                    .build();

            OrderProto.OrderResponse res = orderStub.placeOrder(req);
            JsonObject responseJson = new JsonObject()
                    .put("status", res.getStatus())
                    .put("executionTime", res.getExecutionTime())
                    .put("message", res.getMessage());
            ctx.json(responseJson);
        });
    }
}

