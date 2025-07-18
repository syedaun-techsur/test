import React, { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './index.css';

// Get the root DOM element and validate its presence
const rootElement = document.getElementById('root');
if (!rootElement) {
  throw new Error('Failed to find the root element.');
}

// Create React root and render the application in StrictMode
const root = createRoot(rootElement);
root.render(
  <StrictMode>
    <App />
  </StrictMode>
);