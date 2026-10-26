import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api.js'
import { formatNumber } from '../format.js'
import Modal from './Modal.jsx'

const MARKET_NAME = { TW: '台股', US: '美股' }

function HoldingTable({ holdings, currencyLabel, onEdit, onDelete }) {
  return (
    <table className="table table-hover mt-2">
      <thead className="table-dark">
        <tr><th>代號</th><th>股數</th><th>現價</th><th>總金額 ({currencyLabel})</th><th></th></tr>
      </thead>
      <tbody>
        {holdings === null && (
          <tr><td colSpan="5">讀取中...</td></tr>
        )}
        {holdings?.map((h) => (
          <tr key={h.id}>
            <td>{h.code}</td>
            <td>{formatNumber(h.shares, 4)}</td>
            <td>{h.price === null ? '讀取失敗' : formatNumber(h.price)}</td>
            <td>{formatNumber(h.amount)}</td>
            <td className="text-end">
              <button className="btn btn-sm btn-link" onClick={() => onEdit(h)}>修改</button>
              <button className="btn btn-sm btn-link text-danger" onClick={() => onDelete(h)}>刪除</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}

// 區塊 3：投資看板 (股價由後端向 Yahoo Finance 代抓)
export default function InvestmentSection({ total, usdRate, onChange }) {
  const [tw, setTw] = useState(null)
  const [us, setUs] = useState(null)
  const [editing, setEditing] = useState(null) // { market, code, shares, fixedCode }

  const load = () => {
    api.getHoldings('TW').then(setTw).catch(() => setTw([]))
    api.getHoldings('US').then(setUs).catch(() => setUs([]))
  }

  useEffect(load, [])

  const save = async () => {
    try {
      await api.saveHolding({
        market: editing.market,
        code: editing.code.trim().toUpperCase(),
        shares: Number(editing.shares) || 0,
      })
      setEditing(null)
      load()
      onChange()
    } catch (e) {
      alert(`儲存失敗：${e.message}`)
    }
  }

  const remove = async (h) => {
    if (!confirm(`確定刪除持股「${h.code}」？`)) return
    await api.deleteHolding(h.id)
    load()
    onChange()
  }

  const edit = (h) => setEditing({ market: h.market, code: h.code, shares: h.shares, fixedCode: true })

  return (
    <div className="container section-container mb-5">
      <div className="d-flex justify-content-between align-items-center flex-wrap gap-2">
        <h2 className="mb-0">
          投資總額 (台幣): <span className="text-primary fw-bold">
            {total === undefined ? '連線中...' : `$ ${formatNumber(total, 0)}`}
          </span>
        </h2>
        <Link className="btn btn-outline-secondary" to="/record">+股票交易紀錄</Link>
      </div>

      <div className="row mt-4 mb-3">
        <div className="col-12">
          <button className="btn btn-success me-2" onClick={() => setEditing({ market: 'TW', code: '', shares: '' })}>
            + 新增台股
          </button>
          <button className="btn btn-primary" onClick={() => setEditing({ market: 'US', code: '', shares: '' })}>
            + 新增美股
          </button>
        </div>
      </div>

      <h4 className="mt-4">台股資產</h4>
      <HoldingTable holdings={tw} currencyLabel="台幣" onEdit={edit} onDelete={remove} />

      <h4 className="mt-4">美股資產 (匯率: 1 USD = {usdRate} TWD)</h4>
      <HoldingTable holdings={us} currencyLabel="美金" onEdit={edit} onDelete={remove} />

      {editing && (
        <Modal title={`${editing.fixedCode ? '修改' : '新增'}${MARKET_NAME[editing.market]}`}
               onClose={() => setEditing(null)} onSubmit={save}>
          <div className="mb-3">
            <label className="form-label">股票代號</label>
            <input className="form-control" required disabled={editing.fixedCode} autoFocus={!editing.fixedCode}
                   placeholder={editing.market === 'TW' ? '例如: 2454' : '例如: AAPL'} value={editing.code}
                   onChange={(e) => setEditing({ ...editing, code: e.target.value })} />
          </div>
          <div className="mb-3">
            <label className="form-label">持股股數</label>
            <input type="number" className="form-control" required min="0" step="any"
                   autoFocus={editing.fixedCode} value={editing.shares}
                   onChange={(e) => setEditing({ ...editing, shares: e.target.value })} />
          </div>
        </Modal>
      )}
    </div>
  )
}
