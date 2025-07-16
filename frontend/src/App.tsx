import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';

const containerStyle: React.CSSProperties = {
  padding: 32,
};

function App(): JSX.Element {
  const { data, error, isLoading } = useQuery({
    queryKey: ['hello'],
    queryFn: fetchHello,
  });

  return (
    <main style={containerStyle}>
      <h1>Full Stack Demo (Vite + Spring Boot)</h1>
      <p>
        Backend says:{' '}
        {isLoading
          ? 'Loading...'
          : error instanceof Error
          ? `Error: ${error.message}`
          : data?.message ?? 'No message received'}
      </p>
    </main>
  );
}

export default App;