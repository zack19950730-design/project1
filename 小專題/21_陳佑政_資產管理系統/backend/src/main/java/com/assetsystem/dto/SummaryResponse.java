package com.assetsystem.dto;

import java.math.BigDecimal;

/** 首頁總資產看板，金額皆為台幣。淨資產 = 流動資金 + 投資 + 固定資產 - 負債 */
public record SummaryResponse(
        BigDecimal cash,
        BigDecimal investment,
        BigDecimal fixedAsset,
        BigDecimal liability,
        BigDecimal netWorth,
        BigDecimal usdRate
) {
}
