package com.assetsystem.service;

import com.assetsystem.entity.Currency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/** 匯率換算，匯率設定於 application.properties 的 app.usd-rate */
@Service
public class ExchangeRateService {

    private final BigDecimal usdRate;

    public ExchangeRateService(@Value("${app.usd-rate}") BigDecimal usdRate) {
        this.usdRate = usdRate;
    }

    public BigDecimal getUsdRate() {
        return usdRate;
    }

    public BigDecimal toTwd(BigDecimal amount, Currency currency) {
        return currency == Currency.USD ? amount.multiply(usdRate) : amount;
    }
}
