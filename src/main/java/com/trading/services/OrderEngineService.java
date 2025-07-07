package com.trading.services;

import io.grpc.stub.StreamObserver;
import order.OrderProto;
import order.OrderServiceGrpc;

import java.time.Instant;

public class OrderEngineService extends OrderServiceGrpc.OrderServiceImplBase {

    @Override
    public void placeOrder(OrderProto.OrderRequest request, StreamObserver<OrderProto.OrderResponse> responseObserver) {
        System.out.println("order received..");
        String orderId = request.getOrderId();
        String symbol = request.getSymbol();
        double qty = request.getQty();
        OrderProto.OrderRequest.OrderType type = request.getType();

        OrderProto.OrderResponse.Builder responseBuilder = OrderProto.OrderResponse.newBuilder();

        if (qty <= 0) {
            responseBuilder
                    .setStatus("FAILED")
                    .setExecutionTime(Instant.now().toString())
                    .setMessage("Quantity must be greater than zero");
        } else {
            responseBuilder
                    .setStatus("SUCCESS")
                    .setExecutionTime(Instant.now().toString())
                    .setMessage("Order " + orderId + " for " + qty + " " + symbol + " placed as " + type);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}

