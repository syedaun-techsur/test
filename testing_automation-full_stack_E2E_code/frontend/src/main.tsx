import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './index.css';

// Get the root element from the DOM
const rootElement = document.getElementById('root');
if (!rootElement) {
  throw new Error('Root element not found. Make sure there is a div with id="root" in your index.html.');
}

// Create a root and render the application wrapped in StrictMode
createRoot(rootElement).render(
  <StrictMode>
    <App />
  </StrictMode>
);