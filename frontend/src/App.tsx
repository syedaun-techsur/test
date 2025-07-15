import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';

function App() {
  const { data, error, isLoading, isError } = useQuery({
    queryKey: ['hello'],
    queryFn: fetchHello,
  });

  return (
    <div style={{ padding: 32 }}>
      <h1>Full Stack Demo (Vite + Spring Boot)</h1>
      <p>
        Backend says:{' '}
        {isLoading ? (
          'Loading...'
        ) : isError && error instanceof Error ? (
          <span role="alert">Error: {error.message}</span>
        ) : (
          data?.message ?? 'No message available'
        )}
      </p>
    </div>
  );
}

export default App;