import { defineConfig, UserConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
const config: UserConfig = {
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFilesAfterEnv: './src/test/setup.ts',
    include: ['**/*.{test,spec}.{js,ts,jsx,tsx}'],
  },
  optimizeDeps: {
    exclude: ['lucide-react'],
  },
};

export default defineConfig(config);