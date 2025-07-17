import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { AuthProvider, useAuth } from '../context/AuthContext';

// Mock fetch
global.fetch = vi.fn() as unknown as typeof fetch;

// Helper to set cookie
const setUserCookie = (user: object | null) => {
  if (user) {
    const serialized = encodeURIComponent(JSON.stringify(user));
    document.cookie = `user=${serialized}; path=/;`;
  } else {
    document.cookie = 'user=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;';
  }
};

// Helper to clear user cookie
const clearUserCookie = () => {
  document.cookie = 'user=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;';
};

// Test component that uses the auth context
const TestComponent = () => {
  const { user, login, logout, isLoading } = useAuth();

  return (
    <div>
      <div data-testid="loading">{isLoading ? 'Loading' : 'Not Loading'}</div>
      <div data-testid="user">{user ? `${user.firstName} ${user.lastName}` : 'No User'}</div>
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
  beforeEach(() => {
    vi.clearAllMocks();
    clearUserCookie();
  });

  afterEach(() => {
    clearUserCookie();
  });

  it('provides initial state correctly', async () => {
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    // Await loading to complete
    await waitFor(() =>
      expect(screen.getByTestId('loading')).toHaveTextContent('Not Loading')
    );

    expect(screen.getByTestId('user')).toHaveTextContent('No User');
  });

  it('loads user from cookie on initialization', async () => {
    const mockUser = { id: 1, email: 'test@example.com', firstName: 'John', lastName: 'Doe' };

    setUserCookie(mockUser);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() =>
      expect(screen.getByTestId('user')).toHaveTextContent('John Doe')
    );
  });

  it('handles successful login', async () => {
    const user = userEvent.setup();
    const mockResponse = {
      user: { id: 1, email: 'test@example.com', firstName: 'Jane', lastName: 'Smith' },
      message: 'Login successful'
    };

    (global.fetch as unknown as jest.Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    const loginButton = screen.getByTestId('login-btn');
    await user.click(loginButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('Jane Smith');
    });

    // Confirm cookie is set with the user info
    const cookieUser = document.cookie
      .split('; ')
      .find((row) => row.startsWith('user='))
      ?.split('=')[1];
    expect(cookieUser).toBeDefined();

    if (cookieUser) {
      expect(JSON.parse(decodeURIComponent(cookieUser))).toEqual(mockResponse.user);
    }
  });

  it('handles login failure', async () => {
    const user = userEvent.setup();
    const mockErrorResponse = {
      message: 'Invalid credentials'
    };

    (global.fetch as unknown as jest.Mock).mockResolvedValueOnce({
      ok: false,
      json: async () => mockErrorResponse,
    });

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    const loginButton = screen.getByTestId('login-btn');
    await user.click(loginButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('No User');
    });
  });

  it('handles logout correctly', async () => {
    const user = userEvent.setup();
    const mockUser = { id: 1, email: 'test@example.com', firstName: 'John', lastName: 'Doe' };

    setUserCookie(mockUser);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() =>
      expect(screen.getByTestId('user')).toHaveTextContent('John Doe')
    );

    const logoutButton = screen.getByTestId('logout-btn');
    await user.click(logoutButton);

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('No User');
    });

    // Confirm cookie is cleared
    const cookieUser = document.cookie
      .split('; ')
      .find((row) => row.startsWith('user='))
      ?.split('=')[1];
    expect(cookieUser).toBe('');
  });
});