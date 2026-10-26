import { NavLink, Navigate, Route, Routes } from 'react-router-dom'
import { useAuth } from './auth.jsx'
import FrontPage from './pages/FrontPage.jsx'
import RecordPage from './pages/RecordPage.jsx'
import LoginPage from './pages/LoginPage.jsx'

export default function App() {
  const { member, logout } = useAuth()

  return (
    <>
      <nav className="navbar navbar-expand navbar-dark bg-dark">
        <div className="container">
          <span className="navbar-brand">給我錢資產管理系統</span>
          {member && (
            <>
              <div className="navbar-nav me-auto">
                <NavLink className="nav-link" to="/" end>總資產</NavLink>
                <NavLink className="nav-link" to="/record">股票交易紀錄</NavLink>
              </div>
              <div className="d-flex align-items-center gap-2">
                <span className="navbar-text">{member.username}</span>
                <button className="btn btn-sm btn-outline-light" onClick={logout}>登出</button>
              </div>
            </>
          )}
        </div>
      </nav>

      {member === undefined && <div className="container py-5 text-center">連線中...</div>}
      {member === null && <LoginPage />}
      {member && (
        // key 讓切換帳號時整個頁面重新掛載，不會殘留上一個帳號的資料
        <Routes key={member.id}>
          <Route path="/" element={<FrontPage />} />
          <Route path="/record" element={<RecordPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      )}
    </>
  )
}
