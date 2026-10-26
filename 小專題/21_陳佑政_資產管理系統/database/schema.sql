-- ==========================================
-- 給我錢資產管理系統 - 資料庫結構
-- 執行方式: mysql -u root < schema.sql
-- (舊資料庫升級請改執行 migration_member.sql)
-- ==========================================
CREATE DATABASE IF NOT EXISTS asset_system
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE asset_system;

-- 會員帳號，密碼只存 BCrypt 雜湊
CREATE TABLE IF NOT EXISTS member (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(50)  NOT NULL COMMENT '登入帳號',
    password_hash  VARCHAR(100) NOT NULL COMMENT 'BCrypt 雜湊後的密碼',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_member_username (username)
) ENGINE = InnoDB;

-- 流動資金 / 固定資產 / 負債 共用一張表，以 category 區分
CREATE TABLE IF NOT EXISTS asset_item (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL COMMENT '擁有者 member.id',
    category    ENUM('CASH', 'FIXED', 'LIABILITY') NOT NULL COMMENT 'CASH=流動資金, FIXED=固定資產, LIABILITY=負債',
    name        VARCHAR(100)   NOT NULL COMMENT '項目名稱，例如 現金(台幣)、房子、房屋貸款',
    amount      DECIMAL(18, 2) NOT NULL DEFAULT 0,
    currency    ENUM('TWD', 'USD') NOT NULL DEFAULT 'TWD',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_asset_member_category_name (member_id, category, name),
    CONSTRAINT fk_asset_item_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- 目前持股 (首頁投資看板)
CREATE TABLE IF NOT EXISTS stock_holding (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL COMMENT '擁有者 member.id',
    market      ENUM('TW', 'US') NOT NULL COMMENT 'TW=台股, US=美股',
    code        VARCHAR(20) NOT NULL COMMENT '股票代號，例如 2330、AAPL',
    shares      DECIMAL(18, 4) NOT NULL DEFAULT 0,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_holding_member_market_code (member_id, market, code),
    CONSTRAINT fk_stock_holding_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- 股票交易紀錄 (record 頁面)
CREATE TABLE IF NOT EXISTS stock_trade (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL COMMENT '擁有者 member.id',
    market      ENUM('TW', 'US') NOT NULL,
    trade_date  DATE NOT NULL,
    trade_type  ENUM('BUY', 'SELL') NOT NULL COMMENT 'BUY=買, SELL=賣',
    code        VARCHAR(20) NOT NULL,
    shares      DECIMAL(18, 4) NOT NULL,
    price       DECIMAL(18, 4) NOT NULL COMMENT '成交單價',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_trade_member_market_date (member_id, market, trade_date),
    CONSTRAINT fk_stock_trade_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE
) ENGINE = InnoDB;
