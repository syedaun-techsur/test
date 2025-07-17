import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { AuthProvider, useAuth } from '../context/AuthContext';

// Mock fetch
global.fetch = vi.fn() as any;

// Test component that uses the auth context
const TestComponent = () => {
  const { user, token, login, logout, isLoading } = useAuth();

  return (
    <div>
      <div data-testid="loading">{isLoading ? 'Loading' : 'Not Loading'}</div>
      <div data-testid="user">{user ? `${user.firstName} ${user.lastName}` : 'No User'}</div>
      <div data-testid="token">{token || 'No Token'}</div>
      <button
        data-testid="login-btn"
        onClick={async () => {
          // Await to properly handle async state updates in test
          await login('test@example.com', 'password123');
        }}
      >
        Login
      </button>
      <button data-testid="logout-btn" onClick={logout}>Logout</button>
    </div>
  );
};

describe('AuthContext', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  it('provides initial state correctly', async () => {
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    // Initially isLoading is true, but after effect finishes it becomes false
    // Wait for that state change
    await waitFor(() => expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading'));

    expect(screen.getByTestId('user')).toHaveTextContent('No User');
    expect(screen.getByTestId('token')).toHaveTextContent('No Token');
  });

  it('loads user from localStorage on initialization', async () => {
    const mockUser = { id: 1, email: 'test@example.com', firstName: 'John', lastName: 'Doe' };
    const mockToken = 'mock-token';

    localStorage.setItem('user', JSON.stringify(mockUser));
    localStorage.setItem('authToken', mockToken);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    // Wait for loading to finish and user/token set
    await waitFor(() => {
      expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading');
      expect(screen.getByTestId('user')).toHaveTextContent('John Doe');
      expect(screen.getByTestId('token')).toHaveTextContent('mock-token');
    });
  });

  it('handles successful login', async () => {
    const mockResponse = {
      token: 'new-token',
      user: { id: 1, email: 'test@example.com', firstName: 'Jane', lastName: 'Smith' },
      message: 'Login successful'
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    const user = userEvent.setup();
    const loginButton = screen.getByTestId('login-btn');
    await user.click(loginButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('Jane Smith');
      expect(screen.getByTestId('token')).toHaveTextContent('new-token');
    });

    expect(localStorage.getItem('authToken')).toBe('new-token');
    expect(JSON.parse(localStorage.getItem('user') || '{}')).toEqual(mockResponse.user);
  });

  it('handles login failure', async () => {
    const mockErrorResponse = {
      message: 'Invalid credentials'
    };

    (global.fetch as any).mockResolvedValueOnce({
      ok: false,
      json: async () => mockErrorResponse,
    });

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    const user = userEvent.setup();
    const loginButton = screen.getByTestId('login-btn');
    await user.click(loginButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('No User');
      expect(screen.getByTestId('token')).toHaveTextContent('No Token');
    });
  });

  it('handles logout correctly', async () => {
    const mockUser = { id: 1, email: 'test@example.com', firstName: 'John', lastName: 'Doe' };
    const mockToken = 'mock-token';

    localStorage.setItem('user', JSON.stringify(mockUser));
    localStorage.setItem('authToken', mockToken);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    // Wait for loading to finish and user/token set
    await waitFor(() => expect(screen.getByTestId('user')).toHaveTextContent('John Doe'));

    const user = userEvent.setup();
    const logoutButton = screen.getByTestId('logout-btn');
    await user.click(logoutButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('No User');
      expect(screen.getByTestId('token')).toHaveTextContent('No Token');
      expect(localStorage.getItem('authToken')).toBeNull();
      expect(localStorage.getItem('user')).toBeNull();
    });
  });
});