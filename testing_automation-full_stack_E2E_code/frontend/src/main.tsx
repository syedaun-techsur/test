import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './index.css';

// Get the root element from the DOM
const rootElement = document.getElementById('root');
if (!rootElement) {
  throw new Error('Failed to find the root element');
}

// Create a React root and render the App wrapped with StrictMode
const root = createRoot(rootElement);
root.render(
  <StrictMode>
    <App />
  </StrictMode>
);