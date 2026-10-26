import { formatNumber } from '../format.js'

const ROWS = [
  { key: 'cash', label: '流動資金:', hint: '(台幣、美金)' },
  { key: 'investment', label: '投資:', hint: '(台股、美股)' },
  { key: 'fixedAsset', label: '固定資產:', hint: '(房產、汽車)' },
  { key: 'liability', label: '負債:', hint: '(貸款、信用卡)', danger: true },
]

// 區塊 1：總資產看板 (淨資產 = 現金 + 投資 + 固定資產 - 負債)
export default function SummaryBoard({ summary }) {
  const netWorth = summary?.netWorth ?? 0

  return (
    <div className="container section-container">
      <h1 className="display-5 fw-bold mb-4">
        <div className="row align-items-center">
          <div className="col-3">淨資產:</div>
          <div className="col-3"></div>
          <div className={`col-5 ${netWorth < 0 ? 'text-danger' : 'text-success'}`}>
            {summary ? formatNumber(netWorth, 0) : '連線中...'}
          </div>
        </div>
      </h1>

      <hr className="my-4" />

      {ROWS.map((row) => (
        <div key={row.key} className="row fs-4 my-3 align-items-center">
          <div className={`col-3 fw-semibold ${row.danger ? 'text-danger' : 'text-primary'}`}>{row.label}</div>
          <div className="col-3 text-muted fs-6">{row.hint}</div>
          <div className={`col-3 fw-bold text-end ${row.danger ? 'text-danger' : ''}`}>
            {formatNumber(summary?.[row.key] ?? 0, 0)}
          </div>
          <div className="col-3 text-muted fs-6">(台幣)</div>
        </div>
      ))}
    </div>
  )
}
