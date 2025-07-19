import { createRoot } from 'react-dom/client'
import App from './App'
import './index.css'

// Find root element and ensure non-null before rendering
const rootElement = document.getElementById("root");
if (!rootElement) {
  throw new Error('Root element not found');
}

createRoot(rootElement).render(<App />);