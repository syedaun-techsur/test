import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import './index.css';

// Entry point for the React application: render App component into the root element
const container = document.getElementById('root');
if (!container) {
  throw new Error('Root container missing in HTML.');
}

const root = createRoot(container);

root.render(
  <StrictMode>
    <App />
  </StrictMode>
);