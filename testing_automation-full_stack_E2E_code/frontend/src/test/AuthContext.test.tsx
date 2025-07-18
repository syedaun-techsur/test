import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { AuthProvider, useAuth } from '../context/AuthContext';

// Mock fetch with proper typing
global.fetch = vi.fn() as unknown as typeof fetch;

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
        onClick={() => {
          void login('test@example.com', 'password123');
        }}
      >
        Login
      </button>
      <button data-testid="logout-btn" onClick={logout}>
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

  it('provides initial state correctly', () => {
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading');
    expect(screen.getByTestId('user')).toHaveTextContent('No User');
    expect(screen.getByTestId('token')).toHaveTextContent('No Token');
  });

  it('loads user and token from localStorage on initialization', () => {
    const mockUser = { id: 1, email: 'test@example.com', firstName: 'John', lastName: 'Doe' };
    const mockToken = 'mock-token';

    localStorage.setItem('user', JSON.stringify(mockUser));
    localStorage.setItem('authToken', mockToken);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    expect(screen.getByTestId('user')).toHaveTextContent('John Doe');
    expect(screen.getByTestId('token')).toHaveTextContent('mock-token');
  });

  it('handles successful login correctly', async () => {
    const user = userEvent.setup();
    const mockResponse = {
      token: 'new-token',
      user: { id: 1, email: 'test@example.com', firstName: 'Jane', lastName: 'Smith' },
      message: 'Login successful',
    };

    (global.fetch as vi.Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    } as Response);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    const loginButton = screen.getByTestId('login-btn');
    await user.click(loginButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('Jane Smith');
      expect(screen.getByTestId('token')).toHaveTextContent('new-token');
    });

    expect(localStorage.getItem('authToken')).toBe('new-token');
    expect(JSON.parse(localStorage.getItem('user') || '{}')).toEqual(mockResponse.user);
  });

  it('handles login failure gracefully', async () => {
    const user = userEvent.setup();
    const mockErrorResponse = {
      message: 'Invalid credentials',
    };

    (global.fetch as vi.Mock).mockResolvedValueOnce({
      ok: false,
      json: async () => mockErrorResponse,
    } as Response);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    const loginButton = screen.getByTestId('login-btn');
    await user.click(loginButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('No User');
      expect(screen.getByTestId('token')).toHaveTextContent('No Token');
    });
  });

  it('handles logout correctly and clears state', async () => {
    const user = userEvent.setup();
    const mockUser = { id: 1, email: 'test@example.com', firstName: 'John', lastName: 'Doe' };
    const mockToken = 'mock-token';

    localStorage.setItem('user', JSON.stringify(mockUser));
    localStorage.setItem('authToken', mockToken);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    // Verify user is logged in initially
    expect(screen.getByTestId('user')).toHaveTextContent('John Doe');

    const logoutButton = screen.getByTestId('logout-btn');
    await user.click(logoutButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('No User');
      expect(screen.getByTestId('token')).toHaveTextContent('No Token');
    });

    expect(localStorage.getItem('authToken')).toBeNull();
    expect(localStorage.getItem('user')).toBeNull();
  });
});