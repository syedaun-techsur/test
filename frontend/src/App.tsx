import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';

interface HelloResponse {
  message: string;
}

function App() {
  const { data, error, isLoading } = useQuery<HelloResponse, Error>({
    queryKey: ['hello'],
    queryFn: fetchHello,
  });

  let content;
  if (isLoading) {
    content = 'Loading...';
  } else if (error) {
    content = `Error: ${error.message}`;
  } else {
    content = data?.message ?? 'No message received';
  }

  return (
    <div style={{ padding: 32 }}>
      <h1>Full Stack Demo (Vite + Spring Boot)</h1>
      <p>Backend says: {content}</p>
    </div>
  );
}

export default App;