import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.ts'], // Use array for setupFiles for better type safety
  },
  optimizeDeps: {
    exclude: ['lucide-react'],
  },
});