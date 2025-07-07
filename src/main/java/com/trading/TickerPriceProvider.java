package com.trading;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class TickerPriceProvider {
    private final WebClient client;
    private final String apiKey;
    private final Map<String, Double> prices = new ConcurrentHashMap<>();

    public TickerPriceProvider(Vertx vertx, String apiKey) {
        this.client = WebClient.create(vertx);
        this.apiKey = apiKey;
    }

    public void fetchPrice(String symbol) {
        client.get(443, "www.alphavantage.co", "/query")
                .ssl(true)
                .addQueryParam("function", "GLOBAL_QUOTE")
                .addQueryParam("symbol", symbol)
                .addQueryParam("apikey", apiKey)
                .send(ar -> {
                    if (ar.succeeded()) {
                        JsonObject quote = ar.result().bodyAsJsonObject().getJsonObject("Global Quote");
                        if (quote != null && quote.containsKey("05. price")) {
                            double price = Double.parseDouble(quote.getString("05. price"));
                            prices.put(symbol, price);
                            System.out.println("Updated price for " + symbol + ": " + price);
                        }
                    } else {
                        ar.cause().printStackTrace();
                    }
                });
    }

    public double getPrice(String symbol) {
        fetchPrice(symbol);
        return prices.getOrDefault(symbol, 0.0);
    }
}
