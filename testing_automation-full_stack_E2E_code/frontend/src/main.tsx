import { StrictMode } from 'react';
import { createRoot, Root } from 'react-dom/client';
import App from './App';
import './index.css';

const container = document.getElementById('root');
if (!container) {
  throw new Error('Root container missing in HTML.');
}

const root: Root = createRoot(container);
root.render(
  <StrictMode>
    <App />
  </StrictMode>
);