import { useQuery } from '@tanstack/react-query';
import { fetchHello } from './api';

function App(): JSX.Element {
  const { data, error, isLoading } = useQuery<{ message: string }, Error>({
    queryKey: ['hello'],
    queryFn: fetchHello,
  });

  return (
    <>
      <div style={{ padding: 32 }}>
        <h1>Full Stack Demo (Vite + Spring Boot)</h1>
        <p>
          Backend says:{' '}
          {isLoading
            ? 'Loading...'
            : error?.message
            ? `Error: ${error.message}`
            : data?.message}
        </p>
      </div>
    </>
  );
}

export default App;