package com.assetsystem.dto;

import com.assetsystem.entity.AssetCategory;
import com.assetsystem.entity.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/** 新增 / 修改 資產項目。currency 不填時，名稱含「美金」或「美幣」視為 USD，其餘 TWD */
public record AssetItemRequest(
        @NotNull AssetCategory category,
        @NotBlank @Size(max = 100) String name,
        @NotNull @PositiveOrZero BigDecimal amount,
        Currency currency
) {
}
