import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';

const App: React.FC = () => {
  const { data, error, isLoading } = useQuery({
    queryKey: ['hello'],
    queryFn: fetchHello,
  });

  return (
    <div style={{ padding: 32 }}>
      <h1>Full Stack Demo (Vite + Spring Boot)</h1>
      <p>
        Backend says:{' '}
        {isLoading
          ? 'Loading...'
          : error instanceof Error
          ? `Error: ${error.message}`
          : data?.message}
      </p>
    </div>
  );
};

export default App;