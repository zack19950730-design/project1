-- ==========================================
-- 升級舊資料庫：新增 member 會員表，並讓每筆資產歸屬於某個會員
-- 舊資料會全部歸給 demo 帳號 (密碼 demo1234)，登入後請自行修改或另建帳號
-- 執行方式: mysql -u root < migration_member.sql  (可重複執行)
-- ==========================================
USE asset_system;

CREATE TABLE IF NOT EXISTS member (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(50)  NOT NULL COMMENT '登入帳號',
    password_hash  VARCHAR(100) NOT NULL COMMENT 'BCrypt 雜湊後的密碼',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_member_username (username)
) ENGINE = InnoDB;

-- demo / demo1234
INSERT IGNORE INTO member (username, password_hash)
VALUES ('demo', '$2a$10$yQ32vKgn.rjSIf9jfOGZqOHtUeC6CLDTu7kFmbtMtiABC92BVPGsO');

SET @demo_id = (SELECT id FROM member WHERE username = 'demo');

-- ---------- asset_item ----------
ALTER TABLE asset_item ADD COLUMN IF NOT EXISTS member_id BIGINT NULL COMMENT '擁有者 member.id' AFTER id;
UPDATE asset_item SET member_id = @demo_id WHERE member_id IS NULL;
ALTER TABLE asset_item MODIFY member_id BIGINT NOT NULL COMMENT '擁有者 member.id';
ALTER TABLE asset_item DROP INDEX IF EXISTS uk_asset_category_name;
ALTER TABLE asset_item ADD UNIQUE KEY IF NOT EXISTS uk_asset_member_category_name (member_id, category, name);
ALTER TABLE asset_item DROP FOREIGN KEY IF EXISTS fk_asset_item_member;
ALTER TABLE asset_item ADD CONSTRAINT fk_asset_item_member
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE;

-- ---------- stock_holding ----------
ALTER TABLE stock_holding ADD COLUMN IF NOT EXISTS member_id BIGINT NULL COMMENT '擁有者 member.id' AFTER id;
UPDATE stock_holding SET member_id = @demo_id WHERE member_id IS NULL;
ALTER TABLE stock_holding MODIFY member_id BIGINT NOT NULL COMMENT '擁有者 member.id';
ALTER TABLE stock_holding DROP INDEX IF EXISTS uk_holding_market_code;
ALTER TABLE stock_holding ADD UNIQUE KEY IF NOT EXISTS uk_holding_member_market_code (member_id, market, code);
ALTER TABLE stock_holding DROP FOREIGN KEY IF EXISTS fk_stock_holding_member;
ALTER TABLE stock_holding ADD CONSTRAINT fk_stock_holding_member
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE;

-- ---------- stock_trade ----------
ALTER TABLE stock_trade ADD COLUMN IF NOT EXISTS member_id BIGINT NULL COMMENT '擁有者 member.id' AFTER id;
UPDATE stock_trade SET member_id = @demo_id WHERE member_id IS NULL;
ALTER TABLE stock_trade MODIFY member_id BIGINT NOT NULL COMMENT '擁有者 member.id';
ALTER TABLE stock_trade DROP INDEX IF EXISTS idx_trade_market_date;
ALTER TABLE stock_trade ADD KEY IF NOT EXISTS idx_trade_member_market_date (member_id, market, trade_date);
ALTER TABLE stock_trade DROP FOREIGN KEY IF EXISTS fk_stock_trade_member;
ALTER TABLE stock_trade ADD CONSTRAINT fk_stock_trade_member
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE;
