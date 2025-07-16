import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';
import React, { Fragment } from 'react';

function App() {
  const {
    data,
    error,
    isLoading,
  }: {
    data?: { message: string };
    error: unknown;
    isLoading: boolean;
  } = useQuery({
    queryKey: ['hello'],
    queryFn: fetchHello,
  });

  // Type guard to handle error of unknown type
  const getErrorMessage = (error: unknown): string => {
    if (error instanceof Error) return error.message;
    return String(error);
  };

  return (
    <div style={{ padding: 32 }}>
      <h1>Full Stack Demo (Vite + Spring Boot)</h1>
      <p>
        Backend says:{' '}
        {isLoading ? (
          'Loading...'
        ) : error ? (
          `Error: ${getErrorMessage(error)}`
        ) : (
          data?.message
        )}
      </p>
    </div>
  );
}

export default App;