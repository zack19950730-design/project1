package com.assetsystem.dto;

import com.assetsystem.entity.Market;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record StockHoldingRequest(
        @NotNull Market market,
        @NotBlank @Size(max = 20) String code,
        @NotNull @PositiveOrZero BigDecimal shares
) {
}
