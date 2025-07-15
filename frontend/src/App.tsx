import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';

function App(): JSX.Element {
  const { data, error, isLoading } = useQuery({
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
        ) : error ? (
          `Error: ${(error as Error).message}`
        ) : (
          data?.message
        )}
      </p>
    </div>
  );
}

export default App;