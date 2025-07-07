package com.trading.services;

import com.trading.FXRateProvider;
import com.trading.TickerPriceProvider;
import io.grpc.stub.StreamObserver;
import io.vertx.core.Vertx;
import pricing.PricingServiceGrpc;
import pricing.PricingProto;

public class PricingService extends PricingServiceGrpc.PricingServiceImplBase {

    private final TickerPriceProvider tickerPriceProvider;
    private final FXRateProvider fxRateProvider;

    public PricingService(Vertx vertx)
    {
        this.tickerPriceProvider = new TickerPriceProvider(vertx, "LAUG8VIAY2ZD2B82");
        this.fxRateProvider = new FXRateProvider(vertx, "5ca0333687dff1020225aab7");
        vertx.setPeriodic(60_000, id -> fxRateProvider.fetchRates());

    }

    @Override
    public void getPrice(PricingProto.PriceRequest priceRequest, StreamObserver<PricingProto.PriceResponse> priceResponseObserver)
    {
        String tickerSymbol = priceRequest.getTickerSymbol();
        double price = tickerPriceProvider.getPrice(tickerSymbol);
        String currency = priceRequest.getCurrency();
        PricingProto.PriceResponse.Builder responseBuilder = PricingProto.PriceResponse.newBuilder();

        if(price == 0.0)
        {
            responseBuilder.setStatus("FAILED TO PRICE: Ticker not found!");
        }
        else
        {
            responseBuilder.setTickerSymbol(tickerSymbol);
            responseBuilder.setPrice(price * fxRateProvider.getRate(currency));
            responseBuilder.setCurrency(currency);
            responseBuilder.setAsOf(java.time.Instant.now().toString());
            responseBuilder.setStatus("SUCCESS");
        }

        priceResponseObserver.onNext(responseBuilder.build());
        priceResponseObserver.onCompleted();
    }
}
