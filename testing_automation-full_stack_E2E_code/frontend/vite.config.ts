import { defineConfig, UserConfig } from 'vite';
import react from '@vitejs/plugin-react';

interface ViteConfig extends UserConfig {
  test?: {
    globals?: boolean;
    environment?: string;
    setupFiles?: string | string[];
  };
}

// https://vitejs.dev/config/
export default defineConfig<ViteConfig>({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    // Use URL for consistent path resolution
    setupFiles: new URL('./src/test/setup.ts', import.meta.url).pathname,
  },
  optimizeDeps: {
    exclude: ['lucide-react'],
  },
});