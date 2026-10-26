package com.assetsystem.repository;

import com.assetsystem.entity.Market;
import com.assetsystem.entity.StockTrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockTradeRepository extends JpaRepository<StockTrade, Long> {

    List<StockTrade> findByMarketOrderByTradeDateAscIdAsc(Market market);
}
