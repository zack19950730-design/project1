package com.assetsystem.service;

import com.assetsystem.dto.SummaryResponse;
import com.assetsystem.entity.AssetCategory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/** 總資產看板：淨資產 = 流動資金 + 投資 + 固定資產 - 負債 */
@Service
public class SummaryService {

    private final AssetItemService assetItemService;
    private final StockHoldingService stockHoldingService;
    private final ExchangeRateService exchangeRateService;

    public SummaryService(AssetItemService assetItemService,
                          StockHoldingService stockHoldingService,
                          ExchangeRateService exchangeRateService) {
        this.assetItemService = assetItemService;
        this.stockHoldingService = stockHoldingService;
        this.exchangeRateService = exchangeRateService;
    }

    public SummaryResponse getSummary(Long memberId) {
        BigDecimal cash = assetItemService.totalTwd(memberId, AssetCategory.CASH);
        BigDecimal fixed = assetItemService.totalTwd(memberId, AssetCategory.FIXED);
        BigDecimal liability = assetItemService.totalTwd(memberId, AssetCategory.LIABILITY);
        BigDecimal investment = stockHoldingService.totalTwd(memberId);
        BigDecimal netWorth = cash.add(investment).add(fixed).subtract(liability);
        return new SummaryResponse(cash, investment, fixed, liability, netWorth, exchangeRateService.getUsdRate());
    }
}
