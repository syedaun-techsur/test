import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    // Use array for setupFiles to support multiple setup files if needed in future
    setupFiles: ['./src/test/setup.ts'],
  },
  optimizeDeps: {
    exclude: ['lucide-react'],
  },
});