import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { AuthProvider, useAuth } from '../context/AuthContext';

// Mock fetch with correct typing
const globalAny: any = global;
globalAny.fetch = vi.fn() as jest.MockedFunction<typeof fetch>;

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
          // Await login to ensure any async state updates propagate
          await login('test@example.com', 'password123');
        }}
      >
        Login
      </button>
      <button
        data-testid="logout-btn"
        onClick={async () => {
          // Await logout if it ever becomes async or state intensive
          logout();
        }}
      >
        Logout
      </button>
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

    await waitFor(() => {
      expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading');
    });
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

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('John Doe');
      expect(screen.getByTestId('token')).toHaveTextContent('mock-token');
    });
  });

  it('handles successful login', async () => {
    const userEvt = userEvent.setup();
    const mockResponse = {
      token: 'new-token',
      user: { id: 1, email: 'test@example.com', firstName: 'Jane', lastName: 'Smith' },
      message: 'Login successful',
    };

    (globalAny.fetch as jest.MockedFunction<typeof fetch>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    } as Response);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    const loginButton = screen.getByTestId('login-btn');
    await userEvt.click(loginButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('Jane Smith');
      expect(screen.getByTestId('token')).toHaveTextContent('new-token');
    });

    expect(localStorage.getItem('authToken')).toBe('new-token');
    expect(JSON.parse(localStorage.getItem('user') || '{}')).toEqual(mockResponse.user);
  });

  it('handles login failure', async () => {
    const userEvt = userEvent.setup();
    const mockErrorResponse = {
      message: 'Invalid credentials',
    };

    (globalAny.fetch as jest.MockedFunction<typeof fetch>).mockResolvedValueOnce({
      ok: false,
      json: async () => mockErrorResponse,
    } as Response);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    const loginButton = screen.getByTestId('login-btn');
    await userEvt.click(loginButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('No User');
      expect(screen.getByTestId('token')).toHaveTextContent('No Token');
    });
  });

  it('handles logout correctly', async () => {
    const userEvt = userEvent.setup();
    const mockUser = { id: 1, email: 'test@example.com', firstName: 'John', lastName: 'Doe' };
    const mockToken = 'mock-token';

    localStorage.setItem('user', JSON.stringify(mockUser));
    localStorage.setItem('authToken', mockToken);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    // Verify user is logged in
    expect(screen.getByTestId('user')).toHaveTextContent('John Doe');

    const logoutButton = screen.getByTestId('logout-btn');
    await userEvt.click(logoutButton);

    expect(screen.getByTestId('user')).toHaveTextContent('No User');
    expect(screen.getByTestId('token')).toHaveTextContent('No Token');
    expect(localStorage.getItem('authToken')).toBeNull();
    expect(localStorage.getItem('user')).toBeNull();
  });
});