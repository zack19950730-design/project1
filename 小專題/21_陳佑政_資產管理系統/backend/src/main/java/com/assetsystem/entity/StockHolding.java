package com.assetsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 對應 stock_holding：首頁投資看板的持股 */
@Entity
@Table(name = "stock_holding")
public class StockHolding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Market market;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal shares = BigDecimal.ZERO;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Market getMarket() { return market; }
    public void setMarket(Market market) { this.market = market; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public BigDecimal getShares() { return shares; }
    public void setShares(BigDecimal shares) { this.shares = shares; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
