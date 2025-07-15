import React, { useEffect, useState } from 'react';
import './App.css';

function App() {
  const [message, setMessage] = useState('');
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch('http://localhost:8080/api/hello')
      .then((res) => {
        if (!res.ok) throw new Error('Network response was not ok');
        return res.json();
      })
      .then((data) => setMessage(data.message))
      .catch((err) => setError(err.message));
  }, []);

  return (
    <div style={{ padding: 32 }}>
      <h1>Full Stack Demo</h1>
      <p>Backend says: {message || (error ? `Error: ${error}` : 'Loading...')}</p>
    </div>
  );
}

export default App;
