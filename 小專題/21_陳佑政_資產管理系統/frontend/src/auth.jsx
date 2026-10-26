import { createContext, useContext, useEffect, useState } from 'react'
import { api, onUnauthorized } from './api.js'

const AuthContext = createContext(null)

// 保存目前登入的會員；member 為 undefined 代表還在確認登入狀態，null 代表未登入
export function AuthProvider({ children }) {
  const [member, setMember] = useState(undefined)

  useEffect(() => {
    onUnauthorized(() => setMember(null))
    api.me().then(setMember).catch(() => setMember(null))
  }, [])

  const value = {
    member,
    login: async (username, password) => setMember(await api.login(username, password)),
    register: async (username, password) => setMember(await api.register(username, password)),
    logout: async () => {
      await api.logout().catch(() => {})
      setMember(null)
    },
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  return useContext(AuthContext)
}
