import React from 'react';
import ReactDOM from 'react-dom/client';

import './index.css';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import App from './App';

// Initialize React Query client instance
const queryClient = new QueryClient();

// Get root element safely, return early if not found to prevent runtime error
const rootElement = document.getElementById('root');
if (!rootElement) {
  throw new Error('Root element not found');
}

// Create root and render the app wrapped with React Query provider and strict mode
ReactDOM.createRoot(rootElement).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </React.StrictMode>
);