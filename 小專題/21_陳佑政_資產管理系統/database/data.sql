-- ==========================================
-- 範例資料 (取自原本 frontpage.html / record.html 的寫死資料)
-- 執行方式: mysql -u root asset_system < data.sql
-- ==========================================
USE asset_system;

INSERT INTO asset_item (category, name, amount, currency) VALUES
    ('CASH',      '現金(台幣)', 152401,   'TWD'),
    ('CASH',      '現金(美金)', 2532,     'USD'),
    ('FIXED',     '40坪廁所',   15000000, 'TWD'),
    ('LIABILITY', '廁所貸款',   15000000, 'TWD')
ON DUPLICATE KEY UPDATE amount = VALUES(amount), currency = VALUES(currency);

INSERT INTO stock_holding (market, code, shares) VALUES
    ('TW', '2330', 200),
    ('TW', '3037', 200),
    ('TW', '2308', 200),
    ('US', 'INTC', 50),
    ('US', 'NBIS', 50),
    ('US', 'TXN',  50),
    ('US', 'PLTR', 50)
ON DUPLICATE KEY UPDATE shares = VALUES(shares);

-- 交易紀錄沒有唯一鍵，只在空表時寫入，避免重複執行產生重複資料
INSERT INTO stock_trade (market, trade_date, trade_type, code, shares, price)
SELECT * FROM (
    SELECT 'TW' AS market, DATE '2026-04-12' AS trade_date, 'BUY'  AS trade_type, '2330' AS code, 200 AS shares, 4500 AS price
    UNION ALL SELECT 'TW', DATE '2026-04-12', 'BUY',  '3037', 200, 20
    UNION ALL SELECT 'TW', DATE '2026-04-12', 'SELL', '2308', 200, 31
    UNION ALL SELECT 'US', DATE '2026-04-12', 'BUY',  'INTC', 50,  21
    UNION ALL SELECT 'US', DATE '2026-04-12', 'SELL', 'NBIS', 50,  150
) seed
WHERE NOT EXISTS (SELECT 1 FROM stock_trade);
