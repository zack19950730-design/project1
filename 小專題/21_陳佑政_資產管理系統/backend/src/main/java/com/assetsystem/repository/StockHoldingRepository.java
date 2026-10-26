package com.assetsystem.repository;

import com.assetsystem.entity.Market;
import com.assetsystem.entity.StockHolding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockHoldingRepository extends JpaRepository<StockHolding, Long> {

    List<StockHolding> findByMarketOrderByIdAsc(Market market);

    Optional<StockHolding> findByMarketAndCode(Market market, String code);
}
