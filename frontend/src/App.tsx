import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';

const containerStyle: React.CSSProperties = {
  padding: 32,
};

const isError = (error: unknown): error is Error => {
  return error instanceof Error;
};

const App: React.FC = () => {
  const { data, error, isLoading } = useQuery({
    queryKey: ['hello'],
    queryFn: fetchHello,
  });

  return (
    <>
      <div style={containerStyle}>
        <h1>Full Stack Demo (Vite + Spring Boot)</h1>
        <p>
          Backend says:{' '}
          {isLoading
            ? 'Loading...'
            : isError(error)
            ? `Error: ${error.message}`
            : data?.message}
        </p>
      </div>
    </>
  );
};

export default App;