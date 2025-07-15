import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Vite configuration for React project
// Reference: https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
})