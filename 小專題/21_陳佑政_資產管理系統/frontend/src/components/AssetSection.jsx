import { useEffect, useState } from 'react'
import { api } from '../api.js'
import { formatNumber } from '../format.js'
import Modal from './Modal.jsx'

// 區塊 2 / 4 / 5：流動資金、固定資產、負債 共用元件
// presets: 下拉選單的預設項目；danger: 負債用紅字
export default function AssetSection({ category, title, addText, presets, danger, usdRate, onChange }) {
  const [items, setItems] = useState([])
  const [editing, setEditing] = useState(null) // { name, amount, custom }

  const load = () => api.getAssets(category).then(setItems).catch((e) => alert(`讀取失敗：${e.message}`))

  useEffect(() => {
    load()
  }, [category])

  const subtotal = items.reduce(
    (sum, item) => sum + Number(item.amount) * (item.currency === 'USD' ? usdRate : 1),
    0,
  )

  const openEditor = (name, custom = false) => {
    const existing = items.find((item) => item.name === name)
    setEditing({ name, amount: existing ? existing.amount : '', custom })
  }

  const save = async () => {
    const name = editing.name.trim()
    if (!name) return
    try {
      await api.saveAsset({ category, name, amount: Number(editing.amount) || 0 })
      setEditing(null)
      await load()
      onChange()
    } catch (e) {
      alert(`儲存失敗：${e.message}`)
    }
  }

  const remove = async (item) => {
    if (!confirm(`確定刪除「${item.name}」？`)) return
    await api.deleteAsset(item.id)
    await load()
    onChange()
  }

  const textClass = danger ? 'text-danger' : ''

  return (
    <div className="container section-container">
      <h2 className="mb-4">
        {title}: <span className={danger ? 'text-danger' : 'text-primary'}>{formatNumber(subtotal, 0)}</span>
      </h2>

      <div className="dropdown mb-4">
        <button className={`btn ${danger ? 'btn-outline-danger' : 'btn-outline-primary'} dropdown-toggle`}
                type="button" data-bs-toggle="dropdown">
          {addText}
        </button>
        <ul className="dropdown-menu">
          {presets.map((name) => (
            <li key={name}>
              <button className="dropdown-item" type="button" onClick={() => openEditor(name)}>{name}</button>
            </li>
          ))}
          <li>
            <button className="dropdown-item text-danger" type="button" onClick={() => openEditor('', true)}>
              + 自訂項目
            </button>
          </li>
        </ul>
      </div>

      {items.map((item) => (
        <div key={item.id} className="asset-item">
          <div className="row">
            <div className={`col-4 ${textClass}`}>{item.name}:</div>
            <div className="col-4 text-end">
              <button className="btn btn-sm btn-link" onClick={() => openEditor(item.name)}>修改</button>
              <button className="btn btn-sm btn-link text-danger" onClick={() => remove(item)}>刪除</button>
            </div>
            <div className={`col-4 ${textClass}`}>{formatNumber(item.amount)}</div>
          </div>
          <div className="row text-muted mb-3" style={{ fontSize: '0.9rem' }}>
            <div className="col-8"></div>
            <div className="col-4">{item.currency === 'USD' ? '(美金)' : '(台幣)'}</div>
          </div>
        </div>
      ))}

      {editing && (
        <Modal title={editing.custom ? '新增自訂項目' : `請輸入「${editing.name}」的金額`}
               onClose={() => setEditing(null)} onSubmit={save}>
          {editing.custom && (
            <div className="mb-3">
              <label className="form-label">項目名稱</label>
              <input className="form-control" required autoFocus value={editing.name}
                     placeholder="名稱含「美金」會以美金計算"
                     onChange={(e) => setEditing({ ...editing, name: e.target.value })} />
            </div>
          )}
          <div className="mb-3">
            <label className="form-label">金額</label>
            <input type="number" className="form-control" required min="0" step="any"
                   autoFocus={!editing.custom} value={editing.amount}
                   onChange={(e) => setEditing({ ...editing, amount: e.target.value })} />
          </div>
        </Modal>
      )}
    </div>
  )
}
