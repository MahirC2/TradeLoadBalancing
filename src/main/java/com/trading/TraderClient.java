package com.trading;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import order.OrderProto;
import order.OrderServiceGrpc;
import pricing.PricingProto;
import pricing.PricingServiceGrpc;

public class TraderClient {

    public static void main(String[] args) {
        requestPrice("AAPL", "USD");
        requestPrice("AAPL", "GBP");
        requestPrice("AAPL", "JPY");
        requestPrice("AAPL", "EUR");

        requestTrade("AAPL", 10);
    }

    static void requestPrice(String tickerSymbol, String currency) {
        // Implement a discovery here so you can discover available services.
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        PricingServiceGrpc.PricingServiceBlockingStub blockingStub = PricingServiceGrpc.newBlockingStub(channel);

        PricingProto.PriceRequest request = PricingProto.PriceRequest.newBuilder()
                .setTickerSymbol(tickerSymbol)
                .setCurrency(currency)
                .setTimestamp(java.time.Instant.now().toString())
                .build();

        PricingProto.PriceResponse response = blockingStub.getPrice(request);
        System.out.println(response.getPrice() + currency);
        System.out.println(response.getStatus());
    }


    static void requestTrade(String tickerSymbol, double qty) {
        // Implement a discovery here so you can discover available services.
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        OrderServiceGrpc.OrderServiceBlockingStub blockingStub = OrderServiceGrpc.newBlockingStub(channel);

        OrderProto.OrderRequest request = OrderProto.OrderRequest.newBuilder()
                .setOrderId("ORD001")
                .setSymbol(tickerSymbol)
                .setQty(qty)
                .setType(OrderProto.OrderRequest.OrderType.MARKET)
                .build();

        OrderProto.OrderResponse response = blockingStub.placeOrder(request);
        System.out.println(response.getStatus());
    }
}

