import { defineConfig, UserConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    // Use absolute path for setup files to ensure proper resolution
    setupFiles: new URL('./src/test/setup.ts', import.meta.url).pathname,
  },
  optimizeDeps: {
    // Exclude lucide-react from pre-bundling optimization
    exclude: ['lucide-react'],
  },
} as UserConfig);