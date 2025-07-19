import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { AuthProvider, useAuth } from '../context/AuthContext';

// Mock fetch with correct typing
const fetchMock = vi.fn();
global.fetch = fetchMock;

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
    
    // Initially, isLoading is true due to useEffect initial load, 
    // then becomes false, so waitFor ensures it is settled.
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
    const user = userEvent.setup();
    const mockResponse = {
      token: 'new-token',
      user: { id: 1, email: 'test@example.com', firstName: 'Jane', lastName: 'Smith' },
      message: 'Login successful'
    };
    
    fetchMock.mockResolvedValueOnce({
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
    const user = userEvent.setup();
    const mockErrorResponse = {
      message: 'Invalid credentials'
    };
    
    fetchMock.mockResolvedValueOnce({
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
    
    // Verify user is logged in
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