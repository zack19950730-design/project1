# 給我錢資產管理系統 (React + Spring Boot + MySQL)

```
21_陳佑政_資產管理系統/
├─ 21_陳佑政_資產管理系統/   原本的 Bootstrap 靜態頁 (保留不動)
├─ database/                 schema.sql (建庫建表)、data.sql (範例資料)
├─ backend/                  Spring Boot 3 + JPA (port 8080)
│   └─ src/main/java/com/assetsystem/
│       ├─ entity/           AssetItem, StockHolding, StockTrade + 列舉
│       ├─ repository/       Spring Data JPA 介面
│       ├─ service/          商業邏輯 (小計、淨資產、匯率、Yahoo 股價代抓)
│       ├─ controller/       REST API
│       ├─ dto/  config/  exception/
└─ frontend/                 React 19 + Vite (port 5173)
    └─ src/  pages/ (FrontPage, RecordPage)  components/  api.js
```

## 啟動

1. 開 XAMPP 的 MySQL
2. 建資料庫 (只需第一次)：
   ```
   C:\xampp\mysql\bin\mysql.exe -u root --default-character-set=utf8mb4 < database\schema.sql
   C:\xampp\mysql\bin\mysql.exe -u root --default-character-set=utf8mb4 < database\data.sql
   ```
3. 後端：`cd backend` → `mvn spring-boot:run`
4. 前端：`cd frontend` → `npm install` → `npm run dev` → 開 http://localhost:5173

資料庫帳密在 `backend/src/main/resources/application.properties` (預設 root、無密碼)，美金匯率 `app.usd-rate` 也在這裡改。

## API

| 方法 | 路徑 | 說明 |
|---|---|---|
| GET | /api/summary | 總覽：流動資金、投資、固定資產、負債、淨資產 (台幣) |
| GET | /api/assets?category=CASH\|FIXED\|LIABILITY | 資產項目列表 |
| POST | /api/assets | 新增；同類別同名稱則更新金額 |
| PUT / DELETE | /api/assets/{id} | 修改 / 刪除 |
| GET | /api/holdings?market=TW\|US | 持股 + 即時股價 |
| POST | /api/holdings | 新增；同市場同代號則更新股數 |
| DELETE | /api/holdings/{id} | 刪除持股 |
| GET | /api/trades?market=TW\|US | 交易紀錄 |
| POST | /api/trades | 新增交易 |
| DELETE | /api/trades/{id} | 刪除交易 |
