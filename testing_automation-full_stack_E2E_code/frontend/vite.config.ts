import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { resolve } from 'path';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    // Use absolute path for setupFiles to avoid path resolution issues
    setupFiles: resolve(__dirname, 'src/test/setup.ts'),
  },
  optimizeDeps: {
    // Exclude packages that should not be pre-bundled by Vite for compatibility
    exclude: ['lucide-react'],
  },
});