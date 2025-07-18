import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { AuthProvider, useAuth } from '../context/AuthContext';

// Mock fetch with proper typing
const mockedFetch = vi.fn() as jest.MockedFunction<typeof fetch>;
global.fetch = mockedFetch;

// Test component that uses the auth context
const TestComponent: React.FC = () => {
  const { user, token, login, logout, isLoading } = useAuth();

  return (
    <div>
      <div data-testid="loading">{isLoading ? 'Loading' : 'Not Loading'}</div>
      <div data-testid="user">{user ? `${user.firstName} ${user.lastName}` : 'No User'}</div>
      <div data-testid="token">{token || 'No Token'}</div>
      <button
        data-testid="login-btn"
        onClick={() => login('test@example.com', 'password123')}
      >
        Login
      </button>
      <button data-testid="logout-btn" onClick={logout}>Logout</button>
    </div>
  );
};

describe('AuthContext', () => {
  const user = userEvent.setup();

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

    // Initially, isLoading is true, so expect 'Loading'
    expect(screen.getByTestId('loading')).toHaveTextContent('Loading');

    // Wait for loading to end to assert user and token
    await waitFor(() => {
      expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading');
      expect(screen.getByTestId('user')).toHaveTextContent('No User');
      expect(screen.getByTestId('token')).toHaveTextContent('No Token');
    });
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
      expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading');
    });
  });

  it('handles successful login', async () => {
    const mockResponse = {
      token: 'new-token',
      user: { id: 1, email: 'test@example.com', firstName: 'Jane', lastName: 'Smith' },
      message: 'Login successful',
    };

    mockedFetch.mockResolvedValueOnce({
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

  it('handles login failure', async () => {
    const mockErrorResponse = {
      message: 'Invalid credentials',
    };

    mockedFetch.mockResolvedValueOnce({
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

    // Verify user is logged in initially
    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('John Doe');
    });

    const logoutButton = screen.getByTestId('logout-btn');
    await user.click(logoutButton);

    expect(screen.getByTestId('user')).toHaveTextContent('No User');
    expect(screen.getByTestId('token')).toHaveTextContent('No Token');
    expect(localStorage.getItem('authToken')).toBeNull();
    expect(localStorage.getItem('user')).toBeNull();
  });
});