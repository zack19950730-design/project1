package com.assetsystem.repository;

import com.assetsystem.entity.Market;
import com.assetsystem.entity.StockTrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** 所有查詢都帶 memberId，確保只能存取自己的交易紀錄 */
public interface StockTradeRepository extends JpaRepository<StockTrade, Long> {

    List<StockTrade> findByMemberIdAndMarketOrderByTradeDateAscIdAsc(Long memberId, Market market);

    Optional<StockTrade> findByIdAndMemberId(Long id, Long memberId);
}
