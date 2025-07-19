import { defineConfig, UserConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
const config: UserConfig = defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    // Use setupFilesAfterEnv for global setup scripts in Vitest
    setupFilesAfterEnv: ['./src/test/setup.ts'],
  },
  optimizeDeps: {
    exclude: ['lucide-react'],
  },
});

export default config;