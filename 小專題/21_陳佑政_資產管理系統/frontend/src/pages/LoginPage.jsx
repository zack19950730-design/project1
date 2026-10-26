import { useState } from 'react'
import { useAuth } from '../auth.jsx'

// 登入 / 註冊 共用一個表單，以上方頁籤切換
export default function LoginPage() {
  const { login, register } = useAuth()
  const [mode, setMode] = useState('login')
  const [form, setForm] = useState({ username: '', password: '', confirm: '' })
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const isRegister = mode === 'register'

  const switchMode = (next) => {
    setMode(next)
    setError('')
  }

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value })

  const submit = async (e) => {
    e.preventDefault()
    if (isRegister && form.password !== form.confirm) {
      setError('兩次輸入的密碼不一致')
      return
    }
    setSubmitting(true)
    setError('')
    try {
      await (isRegister ? register : login)(form.username.trim(), form.password)
    } catch (err) {
      setError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="page" style={{ backgroundImage: 'url(/image/cat3.jpg)' }}>
      <div className="container section-container" style={{ maxWidth: 420 }}>
        <h2 className="text-center mb-4">給我錢資產管理系統</h2>

        <ul className="nav nav-tabs mb-4">
          <li className="nav-item">
            <button type="button" className={`nav-link ${isRegister ? '' : 'active'}`}
                    onClick={() => switchMode('login')}>登入</button>
          </li>
          <li className="nav-item">
            <button type="button" className={`nav-link ${isRegister ? 'active' : ''}`}
                    onClick={() => switchMode('register')}>註冊</button>
          </li>
        </ul>

        <form onSubmit={submit}>
          <div className="mb-3">
            <label className="form-label">帳號</label>
            <input className="form-control" required autoFocus autoComplete="username"
                   minLength={isRegister ? 3 : undefined} maxLength="50"
                   pattern={isRegister ? '[A-Za-z0-9_]+' : undefined}
                   title="3~50 字，只能包含英文、數字或底線"
                   value={form.username} onChange={update('username')} />
          </div>
          <div className="mb-3">
            <label className="form-label">密碼</label>
            <input type="password" className="form-control" required
                   autoComplete={isRegister ? 'new-password' : 'current-password'}
                   minLength={isRegister ? 6 : undefined} maxLength="72"
                   value={form.password} onChange={update('password')} />
            {isRegister && <div className="form-text">至少 6 個字元</div>}
          </div>
          {isRegister && (
            <div className="mb-3">
              <label className="form-label">確認密碼</label>
              <input type="password" className="form-control" required autoComplete="new-password"
                     value={form.confirm} onChange={update('confirm')} />
            </div>
          )}

          {error && <div className="alert alert-danger py-2">{error}</div>}

          <button type="submit" className="btn btn-primary w-100" disabled={submitting}>
            {submitting ? '處理中...' : isRegister ? '註冊並登入' : '登入'}
          </button>
        </form>
      </div>
    </div>
  )
}
