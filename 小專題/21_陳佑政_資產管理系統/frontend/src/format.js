// 千分位格式，null / undefined 顯示為 "-"
export function formatNumber(value, maxDigits = 2) {
  if (value === null || value === undefined) return '-'
  return Number(value).toLocaleString('zh-TW', { maximumFractionDigits: maxDigits })
}

export function today() {
  return new Date().toISOString().split('T')[0]
}
