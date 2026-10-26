package com.assetsystem.dto;

import com.assetsystem.entity.Market;
import com.assetsystem.entity.TradeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockTradeRequest(
        @NotNull Market market,
        @NotNull LocalDate tradeDate,
        @NotNull TradeType tradeType,
        @NotBlank @Size(max = 20) String code,
        @NotNull @Positive BigDecimal shares,
        @NotNull @PositiveOrZero BigDecimal price
) {
}
