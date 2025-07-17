import { defineConfig, UserConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

// https://vitejs.dev/config/
export default defineConfig({
  // Vite plugins
  plugins: [react()],

  // Dependencies to exclude from optimization, e.g., to avoid conflicts or improve build times
  optimizeDeps: {
    exclude: ['lucide-react'],
  },

  // Test configuration for Vitest
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: path.resolve(__dirname, './src/test/setup.ts'),
  },
} as UserConfig);