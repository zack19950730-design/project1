// 呼叫 Spring Boot 後端，路徑 /api 由 vite.config.js 轉發到 8080

async function request(method, url, body) {
  const response = await fetch(url, {
    method,
    headers: body ? { 'Content-Type': 'application/json' } : undefined,
    body: body ? JSON.stringify(body) : undefined,
  })
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || `HTTP ${response.status}`)
  }
  return response.status === 204 ? null : response.json()
}

export const api = {
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
