package com.assetsystem.dto;

import com.assetsystem.entity.Market;

import java.math.BigDecimal;

/**
 * 持股 + 即時股價。price 抓不到時為 null。
 * amount 以該市場幣別計 (台股=台幣、美股=美金)，amountTwd 一律換算成台幣。
 */
public record StockHoldingResponse(
        Long id,
        Market market,
        String code,
        BigDecimal shares,
        BigDecimal price,
        BigDecimal amount,
        BigDecimal amountTwd
) {
}
