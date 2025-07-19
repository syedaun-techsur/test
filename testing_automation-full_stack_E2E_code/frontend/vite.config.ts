import { defineConfig, UserConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.ts'], // setupFiles expects an array
  },
  optimizeDeps: {
    exclude: ['lucide-react'], // Exclude lucide-react from dependency optimization to avoid conflicts or issues
  },
} as UserConfig);