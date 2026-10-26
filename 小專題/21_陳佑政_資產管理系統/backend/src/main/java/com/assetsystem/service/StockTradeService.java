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

    public List<StockTrade> findByMarket(Market market) {
        return repository.findByMarketOrderByTradeDateAscIdAsc(market);
    }

    @Transactional
    public StockTrade create(StockTradeRequest request) {
        StockTrade trade = new StockTrade();
        trade.setMarket(request.market());
        trade.setTradeDate(request.tradeDate());
        trade.setTradeType(request.tradeType());
        trade.setCode(request.code().trim().toUpperCase());
        trade.setShares(request.shares());
        trade.setPrice(request.price());
        return repository.save(trade);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("找不到交易紀錄 id=" + id);
        }
        repository.deleteById(id);
    }
}
