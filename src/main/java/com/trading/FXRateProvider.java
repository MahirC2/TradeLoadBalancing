package com.trading;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FXRateProvider {
    private final WebClient client;
    private final String apiKey;
    private final Map<String, Double> fxRates = new ConcurrentHashMap<>();

    public FXRateProvider(Vertx vertx, String apiKey) {
        this.client = WebClient.create(vertx);
        this.apiKey = apiKey;
    }

    public void fetchRates() {
        client.get(443, "v6.exchangerate-api.com", "/v6/" + apiKey + "/latest/USD")
                .ssl(true)
                .send(ar -> {
                    if (ar.succeeded()) {
                        JsonObject rates = ar.result().bodyAsJsonObject().getJsonObject("conversion_rates");
                        for (String currency : rates.fieldNames()) {
                            fxRates.put(currency, rates.getDouble(currency));
                        }
                        System.out.println("FX Rates Updated");
                    } else {
                        ar.cause().printStackTrace();
                    }
                });
    }

    public double getRate(String currency) {
        return fxRates.getOrDefault(currency, 0.0);
    }
}
