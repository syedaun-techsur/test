import { defineConfig, UserConfig } from 'vite';
import react from '@vitejs/plugin-react';

const config: UserConfig = defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.ts'],
  },
  optimizeDeps: {
    exclude: ['lucide-react'],
  },
});

export default config;