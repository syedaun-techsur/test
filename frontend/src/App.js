import React, { useEffect, useState } from 'react';
import './App.css';

function App() {
  const [message, setMessage] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const controller = new AbortController();
    const signal = controller.signal;

    async function fetchMessage() {
      try {
        const response = await fetch('http://localhost:8080/api/hello', { signal });
        if (!response.ok) {
          throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`);
        }
        const data = await response.json();
        if (data && data.message) {
          setMessage(data.message);
        } else {
          throw new Error('Invalid response structure');
        }
      } catch (err) {
        if (err.name !== 'AbortError') {
          setError(err.message || 'Unknown error occurred');
        }
      } finally {
        setLoading(false);
      }
    }

    fetchMessage();

    return () => {
      controller.abort();
    };
  }, []);

  return (
    <main className="App-header">
      <h1>Full Stack Demo</h1>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>Error: {error}</p>}
      {!loading && !error && <p>Backend says: {message}</p>}
    </main>
  );
}

export default App;