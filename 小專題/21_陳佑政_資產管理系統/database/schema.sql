-- ==========================================
-- 給我錢資產管理系統 - 資料庫結構
-- 執行方式: mysql -u root < schema.sql
-- ==========================================
CREATE DATABASE IF NOT EXISTS asset_system
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE asset_system;

-- 流動資金 / 固定資產 / 負債 共用一張表，以 category 區分
CREATE TABLE IF NOT EXISTS asset_item (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    category    ENUM('CASH', 'FIXED', 'LIABILITY') NOT NULL COMMENT 'CASH=流動資金, FIXED=固定資產, LIABILITY=負債',
    name        VARCHAR(100)   NOT NULL COMMENT '項目名稱，例如 現金(台幣)、房子、房屋貸款',
    amount      DECIMAL(18, 2) NOT NULL DEFAULT 0,
    currency    ENUM('TWD', 'USD') NOT NULL DEFAULT 'TWD',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_asset_category_name (category, name)
) ENGINE = InnoDB;

-- 目前持股 (首頁投資看板)
CREATE TABLE IF NOT EXISTS stock_holding (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    market      ENUM('TW', 'US') NOT NULL COMMENT 'TW=台股, US=美股',
    code        VARCHAR(20) NOT NULL COMMENT '股票代號，例如 2330、AAPL',
    shares      DECIMAL(18, 4) NOT NULL DEFAULT 0,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_holding_market_code (market, code)
) ENGINE = InnoDB;

-- 股票交易紀錄 (record 頁面)
CREATE TABLE IF NOT EXISTS stock_trade (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    market      ENUM('TW', 'US') NOT NULL,
    trade_date  DATE NOT NULL,
    trade_type  ENUM('BUY', 'SELL') NOT NULL COMMENT 'BUY=買, SELL=賣',
    code        VARCHAR(20) NOT NULL,
    shares      DECIMAL(18, 4) NOT NULL,
    price       DECIMAL(18, 4) NOT NULL COMMENT '成交單價',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_trade_market_date (market, trade_date)
) ENGINE = InnoDB;
