import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    // 開發時把 /api 轉給 Spring Boot (8080)
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
