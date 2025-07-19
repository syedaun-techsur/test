import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './index.css';

const rootElement = document.getElementById('root');

// Ensure root element exists before rendering
if (!rootElement) {
  console.error("Root element with id 'root' not found. Cannot mount React app.");
} else {
  createRoot(rootElement).render(
    <StrictMode>
      <App />
    </StrictMode>
  );
}