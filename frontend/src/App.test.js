import { render, screen } from '@testing-library/react';
import App from './App';

test('renders backend message after loading', async () => {
  render(<App />);
  // Wait for the loading to finish and the backend message to appear
  const messageElement = await screen.findByText(/backend says:/i, {}, { timeout: 5000 });
  expect(messageElement).toBeInTheDocument();
});