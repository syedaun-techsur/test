import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';

const App: React.FC = () => {
  const { data, error, isLoading } = useQuery<{ message: string }, Error>({
    queryKey: ['hello'],
    queryFn: fetchHello,
  });

  let content;

  if (isLoading) {
    content = <span aria-live="polite">Loading...</span>;
  } else if (error) {
    content = <span aria-live="polite">{`Error: ${error.message}`}</span>;
  } else {
    content = data?.message ?? 'No message received';
  }

  return (
    <div style={{ padding: 32 }}>
      <h1>Full Stack Demo (Vite + Spring Boot)</h1>
      <p>Backend says: {content}</p>
    </div>
  );
};

export default App;