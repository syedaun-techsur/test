import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';

interface HelloResponse {
  message: string;
}

const App: React.FC = () => {
  const { data, error, isLoading } = useQuery<HelloResponse, Error>({
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
          : error
          ? `Error: ${ (error as unknown as Error).message }`
          : data?.message ?? 'No message received'}
      </p>
    </div>
  );
};

export default App;