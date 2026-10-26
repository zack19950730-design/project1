-- ==========================================
-- 範例資料 (取自原本 frontpage.html / record.html 的寫死資料)
-- 全部歸屬 demo 帳號，登入帳號 demo / 密碼 demo1234
-- 執行方式: mysql -u root asset_system < data.sql
-- ==========================================
USE asset_system;

INSERT IGNORE INTO member (username, password_hash)
VALUES ('demo', '$2a$10$yQ32vKgn.rjSIf9jfOGZqOHtUeC6CLDTu7kFmbtMtiABC92BVPGsO');

SET @demo_id = (SELECT id FROM member WHERE username = 'demo');

INSERT INTO asset_item (member_id, category, name, amount, currency) VALUES
    (@demo_id, 'CASH',      '現金(台幣)', 152401,   'TWD'),
    (@demo_id, 'CASH',      '現金(美金)', 2532,     'USD'),
    (@demo_id, 'FIXED',     '40坪廁所',   15000000, 'TWD'),
    (@demo_id, 'LIABILITY', '廁所貸款',   15000000, 'TWD')
ON DUPLICATE KEY UPDATE amount = VALUES(amount), currency = VALUES(currency);

INSERT INTO stock_holding (member_id, market, code, shares) VALUES
    (@demo_id, 'TW', '2330', 200),
    (@demo_id, 'TW', '3037', 200),
    (@demo_id, 'TW', '2308', 200),
    (@demo_id, 'US', 'INTC', 50),
    (@demo_id, 'US', 'NBIS', 50),
    (@demo_id, 'US', 'TXN',  50),
    (@demo_id, 'US', 'PLTR', 50)
ON DUPLICATE KEY UPDATE shares = VALUES(shares);

-- 交易紀錄沒有唯一鍵，只在 demo 沒有任何紀錄時寫入，避免重複執行產生重複資料
INSERT INTO stock_trade (member_id, market, trade_date, trade_type, code, shares, price)
SELECT @demo_id, seed.* FROM (
    SELECT 'TW' AS market, DATE '2026-04-12' AS trade_date, 'BUY'  AS trade_type, '2330' AS code, 200 AS shares, 4500 AS price
    UNION ALL SELECT 'TW', DATE '2026-04-12', 'BUY',  '3037', 200, 20
    UNION ALL SELECT 'TW', DATE '2026-04-12', 'SELL', '2308', 200, 31
    UNION ALL SELECT 'US', DATE '2026-04-12', 'BUY',  'INTC', 50,  21
    UNION ALL SELECT 'US', DATE '2026-04-12', 'SELL', 'NBIS', 50,  150
) seed
WHERE NOT EXISTS (SELECT 1 FROM stock_trade WHERE member_id = @demo_id);
