import { defineConfig, UserConfig } from 'vite';
import react from '@vitejs/plugin-react';

const config: UserConfig = {
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFilesAfterEnv: ['./src/test/setup.ts'],
  },
  optimizeDeps: {
    exclude: ['lucide-react'],
  },
};

export default defineConfig(config);