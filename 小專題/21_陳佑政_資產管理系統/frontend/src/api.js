// 呼叫 Spring Boot 後端，路徑 /api 由 vite.config.js 轉發到 8080
// 登入狀態存在後端 Session，瀏覽器會自動帶上 Cookie

let unauthorizedHandler = () => {}

// 由 AuthProvider 註冊：任何 API 回 401 (Session 過期) 時回到登入畫面
export function onUnauthorized(handler) {
  unauthorizedHandler = handler
}

async function request(method, url, body) {
  const response = await fetch(url, {
    method,
    credentials: 'include',
    headers: body ? { 'Content-Type': 'application/json' } : undefined,
    body: body ? JSON.stringify(body) : undefined,
  })
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    if (response.status === 401 && !url.startsWith('/api/auth/')) {
      unauthorizedHandler()
    }
    throw new Error(error.message || `HTTP ${response.status}`)
  }
  return response.status === 204 ? null : response.json()
}

export const api = {
  // 會員
  me: () => request('GET', '/api/auth/me'),
  login: (username, password) => request('POST', '/api/auth/login', { username, password }),
  register: (username, password) => request('POST', '/api/auth/register', { username, password }),
  logout: () => request('POST', '/api/auth/logout'),

  getSummary: () => request('GET', '/api/summary'),

  // category: CASH | FIXED | LIABILITY
  getAssets: (category) => request('GET', `/api/assets?category=${category}`),
  saveAsset: (asset) => request('POST', '/api/assets', asset),
  deleteAsset: (id) => request('DELETE', `/api/assets/${id}`),

  // market: TW | US
  getHoldings: (market) => request('GET', `/api/holdings?market=${market}`),
  saveHolding: (holding) => request('POST', '/api/holdings', holding),
  deleteHolding: (id) => request('DELETE', `/api/holdings/${id}`),

  getTrades: (market) => request('GET', `/api/trades?market=${market}`),
  createTrade: (trade) => request('POST', '/api/trades', trade),
  deleteTrade: (id) => request('DELETE', `/api/trades/${id}`),
}
