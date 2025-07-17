import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import React from 'react';
import App from '../App';

// Mock the auth context
vi.mock('../context/AuthContext', () => ({
  AuthProvider: ({ children }: { children: React.ReactNode }) => <>{children}</>,
  useAuth: vi.fn(() => ({
    user: null,
    token: null,
    login: vi.fn(),
    logout: vi.fn(),
    isLoading: false
  }))
}));

// Mock the components (using actual component names from project context)
vi.mock('../components/LoginForm', () => ({
  __esModule: true,
  default: () => <div data-testid="login-form">Login Form</div>
}));

vi.mock('../components/Dashboard', () => ({
  __esModule: true,
  default: () => <div data-testid="dashboard">Dashboard</div>
}));

describe('App Routing', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('redirects to login page by default (root route "/")', async () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>
    );
    expect(await screen.findByTestId('login-form')).toBeInTheDocument();
  });

  it('shows login page on /login route', async () => {
    render(
      <MemoryRouter initialEntries={['/login']}>
        <App />
      </MemoryRouter>
    );
    expect(await screen.findByTestId('login-form')).toBeInTheDocument();
  });

  it('redirects unknown routes to login', async () => {
    render(
      <MemoryRouter initialEntries={['/unknown']}>
        <App />
      </MemoryRouter>
    );
    expect(await screen.findByTestId('login-form')).toBeInTheDocument();
  });

  it('shows dashboard for authenticated user on /dashboard route', async () => {
    // Override useAuth mock for authenticated user
    const useAuthMock = require('../context/AuthContext').useAuth;
    useAuthMock.mockReturnValue({
      user: { id: 'user1' },
      token: 'token123',
      login: vi.fn(),
      logout: vi.fn(),
      isLoading: false
    });

    render(
      <MemoryRouter initialEntries={['/dashboard']}>
        <App />
      </MemoryRouter>
    );
    expect(await screen.findByTestId('dashboard')).toBeInTheDocument();
  });
});