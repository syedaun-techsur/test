import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Vite configuration for React project
export default defineConfig({
  plugins: [react()],

  // Testing configuration
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/test/setup.ts',
  },

  // Dependency optimization configuration
  optimizeDeps: {
    exclude: ['lucide-react'],
  },
});