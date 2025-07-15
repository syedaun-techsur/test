import { defineConfig, UserConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
// Using UserConfig type improves IntelliSense and type safety
const config: UserConfig = {
  plugins: [react()],

  // Example of additional configuration that can be uncommented and customized if needed:
  /*
  server: {
    // Proxy API requests to backend during development
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
  */
}

export default defineConfig(config)