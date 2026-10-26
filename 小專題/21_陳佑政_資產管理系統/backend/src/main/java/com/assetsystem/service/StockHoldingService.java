package com.assetsystem.service;

import com.assetsystem.dto.StockHoldingRequest;
import com.assetsystem.dto.StockHoldingResponse;
import com.assetsystem.entity.Currency;
import com.assetsystem.entity.Market;
import com.assetsystem.entity.StockHolding;
import com.assetsystem.exception.NotFoundException;
import com.assetsystem.repository.StockHoldingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class StockHoldingService {

    private final StockHoldingRepository repository;
    private final StockPriceService stockPriceService;
    private final ExchangeRateService exchangeRateService;

    public StockHoldingService(StockHoldingRepository repository,
                               StockPriceService stockPriceService,
                               ExchangeRateService exchangeRateService) {
        this.repository = repository;
        this.stockPriceService = stockPriceService;
        this.exchangeRateService = exchangeRateService;
    }

    /** 列出某市場持股並附上即時股價 */
    public List<StockHoldingResponse> findByMarketWithPrice(Long memberId, Market market) {
        return repository.findByMemberIdAndMarketOrderByIdAsc(memberId, market).stream()
                .map(this::withPrice)
                .toList();
    }

    /** 同市場同代號已存在就更新股數，否則新增 (與原本 addStock 的行為相同) */
    @Transactional
    public StockHoldingResponse upsert(Long memberId, StockHoldingRequest request) {
        String code = request.code().trim().toUpperCase();
        StockHolding holding = repository.findByMemberIdAndMarketAndCode(memberId, request.market(), code)
                .orElseGet(() -> {
                    StockHolding created = new StockHolding();
                    created.setMemberId(memberId);
                    created.setMarket(request.market());
                    created.setCode(code);
                    return created;
                });
        holding.setShares(request.shares());
        return withPrice(repository.save(holding));
    }

    @Transactional
    public void delete(Long memberId, Long id) {
        StockHolding holding = repository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new NotFoundException("找不到持股 id=" + id));
        repository.delete(holding);
    }

    /** 投資總額 (台幣)，抓不到股價的持股不計入 */
    public BigDecimal totalTwd(Long memberId) {
        return repository.findByMemberId(memberId).stream()
                .map(this::withPrice)
                .map(StockHoldingResponse::amountTwd)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private StockHoldingResponse withPrice(StockHolding holding) {
        BigDecimal price = stockPriceService.getPrice(holding.getMarket(), holding.getCode());
        BigDecimal amount = price == null ? null : holding.getShares().multiply(price);
        BigDecimal amountTwd = amount == null ? null
                : exchangeRateService.toTwd(amount, holding.getMarket() == Market.US ? Currency.USD : Currency.TWD);
        return new StockHoldingResponse(holding.getId(), holding.getMarket(), holding.getCode(),
                holding.getShares(), price, amount, amountTwd);
    }
}
