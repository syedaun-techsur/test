import React, { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './index.css';

// Get the root DOM element
const rootElement = document.getElementById('root');

if (!rootElement) {
  throw new Error("Root element with id 'root' not found in the DOM.");
}

// Create root and render the app wrapped in StrictMode for highlighting potential issues
createRoot(rootElement).render(
  <StrictMode>
    <App />
  </StrictMode>
);