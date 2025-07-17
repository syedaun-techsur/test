import React from 'react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import App from '../App';

// Mock the auth context
vi.mock('../context/AuthContext', () => ({
  AuthProvider: ({ children }: { children: React.ReactNode }) => <>{children}</>,
  useAuth: () => ({
    user: null,
    token: null,
    login: vi.fn(),
    logout: vi.fn(),
    isLoading: false,
  }),
}));

// Mock the components
vi.mock('../components/LoginForm', () => ({
  default: () => <div data-testid="login-page">Login Page</div>,
}));

vi.mock('../components/Dashboard', () => ({
  default: () => <div data-testid="dashboard-page">Dashboard Page</div>,
}));

describe('App Routing', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('redirects to login page by default route "/"', async () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>
    );
    expect(await screen.findByTestId('login-page')).toBeInTheDocument();
  });

  it('shows login page on /login route', async () => {
    render(
      <MemoryRouter initialEntries={['/login']}>
        <App />
      </MemoryRouter>
    );
    expect(await screen.findByTestId('login-page')).toBeInTheDocument();
  });

  it('redirects unknown routes to login page', async () => {
    render(
      <MemoryRouter initialEntries={['/unknown-route']}>
        <App />
      </MemoryRouter>
    );
    expect(await screen.findByTestId('login-page')).toBeInTheDocument();
  });

  it('shows dashboard page on /dashboard route when authenticated', async () => {
    // Override useAuth mock to simulate logged-in user for this test
    vi.mocked(require('../context/AuthContext').useAuth).mockReturnValue({
      user: { id: '123', name: 'Test User' },
      token: 'mock-token',
      login: vi.fn(),
      logout: vi.fn(),
      isLoading: false,
    });

    render(
      <MemoryRouter initialEntries={['/dashboard']}>
        <App />
      </MemoryRouter>
    );

    expect(await screen.findByTestId('dashboard-page')).toBeInTheDocument();
  });
});