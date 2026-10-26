import { NavLink, Route, Routes } from 'react-router-dom'
import FrontPage from './pages/FrontPage.jsx'
import RecordPage from './pages/RecordPage.jsx'

export default function App() {
  return (
    <>
      <nav className="navbar navbar-expand navbar-dark bg-dark">
        <div className="container">
          <span className="navbar-brand">給我錢資產管理系統</span>
          <div className="navbar-nav">
            <NavLink className="nav-link" to="/" end>總資產</NavLink>
            <NavLink className="nav-link" to="/record">股票交易紀錄</NavLink>
          </div>
        </div>
      </nav>
      <Routes>
        <Route path="/" element={<FrontPage />} />
        <Route path="/record" element={<RecordPage />} />
      </Routes>
    </>
  )
}
