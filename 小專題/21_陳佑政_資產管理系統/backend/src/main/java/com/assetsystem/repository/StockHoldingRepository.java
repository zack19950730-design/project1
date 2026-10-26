package com.assetsystem.repository;

import com.assetsystem.entity.Market;
import com.assetsystem.entity.StockHolding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** 所有查詢都帶 memberId，確保只能存取自己的持股 */
public interface StockHoldingRepository extends JpaRepository<StockHolding, Long> {

    List<StockHolding> findByMemberId(Long memberId);

    List<StockHolding> findByMemberIdAndMarketOrderByIdAsc(Long memberId, Market market);

    Optional<StockHolding> findByMemberIdAndMarketAndCode(Long memberId, Market market, String code);

    Optional<StockHolding> findByIdAndMemberId(Long id, Long memberId);
}
