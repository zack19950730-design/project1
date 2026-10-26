package com.assetsystem.service;

import com.assetsystem.entity.Market;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 從 Yahoo Finance 抓即時股價。
 * 原本前端直接 fetch Yahoo 會被瀏覽器 CORS 擋下，改由後端代抓並快取。
 */
@Service
public class StockPriceService {

    private static final Logger log = LoggerFactory.getLogger(StockPriceService.class);

    private record CachedPrice(BigDecimal price, Instant fetchedAt) {
    }

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://query1.finance.yahoo.com")
            .defaultHeader("User-Agent", "Mozilla/5.0")
            .build();

    private final Map<String, CachedPrice> cache = new ConcurrentHashMap<>();
    private final Duration cacheTtl;

    public StockPriceService(@Value("${app.price-cache-seconds}") long cacheSeconds) {
        this.cacheTtl = Duration.ofSeconds(cacheSeconds);
    }

    /** 回傳最新股價，抓取失敗回傳 null */
    public BigDecimal getPrice(Market market, String code) {
        String symbol = market == Market.TW ? code + ".TW" : code;

        CachedPrice cached = cache.get(symbol);
        if (cached != null && cached.fetchedAt().plus(cacheTtl).isAfter(Instant.now())) {
            return cached.price();
        }

        try {
            JsonNode body = restClient.get()
                    .uri("/v8/finance/chart/{symbol}?interval=1m&range=1d", symbol)
                    .retrieve()
                    .body(JsonNode.class);
            JsonNode priceNode = body.path("chart").path("result").path(0).path("meta").path("regularMarketPrice");
            if (priceNode.isMissingNode() || priceNode.isNull()) {
                return null;
            }
            BigDecimal price = priceNode.decimalValue();
            cache.put(symbol, new CachedPrice(price, Instant.now()));
            return price;
        } catch (Exception e) {
            log.warn("抓取 {} 股價失敗: {}", symbol, e.getMessage());
            return cached != null ? cached.price() : null;
        }
    }
}
