package com.assetsystem.service;

import com.assetsystem.dto.StockTradeRequest;
import com.assetsystem.entity.Market;
import com.assetsystem.entity.StockTrade;
import com.assetsystem.exception.NotFoundException;
import com.assetsystem.repository.StockTradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockTradeService {

    private final StockTradeRepository repository;

    public StockTradeService(StockTradeRepository repository) {
        this.repository = repository;
    }

    public List<StockTrade> findByMarket(Long memberId, Market market) {
        return repository.findByMemberIdAndMarketOrderByTradeDateAscIdAsc(memberId, market);
    }

    @Transactional
    public StockTrade create(Long memberId, StockTradeRequest request) {
        StockTrade trade = new StockTrade();
        trade.setMemberId(memberId);
        trade.setMarket(request.market());
        trade.setTradeDate(request.tradeDate());
        trade.setTradeType(request.tradeType());
        trade.setCode(request.code().trim().toUpperCase());
        trade.setShares(request.shares());
        trade.setPrice(request.price());
        return repository.save(trade);
    }

    @Transactional
    public void delete(Long memberId, Long id) {
        StockTrade trade = repository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new NotFoundException("找不到交易紀錄 id=" + id));
        repository.delete(trade);
    }
}
