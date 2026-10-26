import { useEffect, useState } from 'react'
import { api } from '../api.js'
import { formatNumber, today } from '../format.js'
import Modal from '../components/Modal.jsx'

const TRADE_TYPE_TEXT = { BUY: '買', SELL: '賣' }

function TradeTable({ trades, currencyLabel, onDelete }) {
  return (
    <table className="table table-hover mt-2">
      <thead className="table-dark">
        <tr><th>日期</th><th>買/賣</th><th>代號</th><th>股數</th><th>現價</th><th>總金額 ({currencyLabel})</th><th></th></tr>
      </thead>
      <tbody>
        {trades.map((t) => (
          // 買=紅底、賣=綠底 (與原本 applyRowColor 相同)
          <tr key={t.id} className={t.tradeType === 'BUY' ? 'table-danger' : 'table-success'}>
            <td>{t.tradeDate.replace(/-/g, '/')}</td>
            <td>{TRADE_TYPE_TEXT[t.tradeType]}</td>
            <td>{t.code}</td>
            <td>{formatNumber(t.shares, 4)}</td>
            <td>{formatNumber(t.price, 4)}</td>
            <td>{formatNumber(t.amount)}</td>
            <td className="text-end">
              <button className="btn btn-sm btn-link text-danger" onClick={() => onDelete(t)}>刪除</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}

// 對應原本的 record.html
export default function RecordPage() {
  const [tw, setTw] = useState([])
  const [us, setUs] = useState([])
  const [form, setForm] = useState(null)

  const load = () => {
    api.getTrades('TW').then(setTw).catch((e) => alert(`讀取失敗：${e.message}`))
    api.getTrades('US').then(setUs).catch((e) => alert(`讀取失敗：${e.message}`))
  }

  useEffect(load, [])

  const openAddModal = (market) =>
    setForm({ market, tradeDate: today(), tradeType: 'BUY', code: '', shares: '', price: '' })

  const save = async () => {
    try {
      await api.createTrade({
        ...form,
        code: form.code.trim().toUpperCase(),
        shares: Number(form.shares),
        price: Number(form.price),
      })
      setForm(null)
      load()
    } catch (e) {
      alert(`儲存失敗：${e.message}`)
    }
  }

  const remove = async (t) => {
    if (!confirm(`確定刪除 ${t.tradeDate} ${t.code} 這筆紀錄？`)) return
    await api.deleteTrade(t.id)
    load()
  }

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value })
  const estimate = form ? (Number(form.shares) || 0) * (Number(form.price) || 0) : 0

  return (
    <div className="page" style={{ backgroundImage: 'url(/image/images.png)' }}>
      <div className="container section-container mb-5">
        <h2>股票交易紀錄</h2>

        <div className="row mt-4 mb-3">
          <div className="col-12">
            <button className="btn btn-success me-2" onClick={() => openAddModal('TW')}>+ 新增台股交易</button>
            <button className="btn btn-primary" onClick={() => openAddModal('US')}>+ 新增美股交易</button>
          </div>
        </div>

        <h4 className="mt-4">台股交易紀錄</h4>
        <TradeTable trades={tw} currencyLabel="台幣" onDelete={remove} />

        <h4 className="mt-4">美股交易紀錄</h4>
        <TradeTable trades={us} currencyLabel="美金" onDelete={remove} />
      </div>

      {form && (
        <Modal title={form.market === 'TW' ? '新增台股交易紀錄' : '新增美股交易紀錄'}
               onClose={() => setForm(null)} onSubmit={save}>
          <div className="mb-3">
            <label className="form-label">日期</label>
            <input type="date" className="form-control" required value={form.tradeDate} onChange={update('tradeDate')} />
          </div>
          <div className="mb-3">
            <label className="form-label">買/賣</label>
            <select className="form-select" value={form.tradeType} onChange={update('tradeType')}>
              <option value="BUY">買</option>
              <option value="SELL">賣</option>
            </select>
          </div>
          <div className="mb-3">
            <label className="form-label">股票代號</label>
            <input className="form-control" required placeholder="例如: 2330 或 AAPL"
                   value={form.code} onChange={update('code')} />
          </div>
          <div className="mb-3">
            <label className="form-label">股數</label>
            <input type="number" className="form-control" required min="1" step="any" placeholder="100"
                   value={form.shares} onChange={update('shares')} />
          </div>
          <div className="mb-3">
            <label className="form-label">現價 (單價)</label>
            <input type="number" className="form-control" required min="0" step="0.01" placeholder="500"
                   value={form.price} onChange={update('price')} />
          </div>
          <div className="mb-3">
            <label className="form-label">預估總金額</label>
            <input className="form-control" readOnly placeholder="自動計算"
                   value={estimate > 0 ? formatNumber(estimate) : ''} />
          </div>
        </Modal>
      )}
    </div>
  )
}
