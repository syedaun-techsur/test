import React from 'react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import App from '../App';

// Mock the auth context
vi.mock('../context/AuthContext', () => {
  return {
    AuthProvider: ({ children }: { children: React.ReactNode }) => <>{children}</>,
    useAuth: () => ({
      user: null,
      token: null,
      login: vi.fn(),
      logout: vi.fn(),
      isLoading: false,
    }),
  };
});

// Mock the components used in the app
vi.mock('../components/LoginForm', () => ({
  __esModule: true,
  default: () => <div data-testid="login-form">Login Form</div>,
}));

vi.mock('../components/Dashboard', () => ({
  __esModule: true,
  default: () => <div data-testid="dashboard-page">Dashboard Page</div>,
}));

describe('App Routing', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('redirects to login page by default route "/"', () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>
    );
    expect(screen.getByTestId('login-form')).toBeInTheDocument();
  });

  it('shows login page on /login route', () => {
    render(
      <MemoryRouter initialEntries={['/login']}>
        <App />
      </MemoryRouter>
    );
    expect(screen.getByTestId('login-form')).toBeInTheDocument();
  });

  it('redirects unknown routes to login page', () => {
    render(
      <MemoryRouter initialEntries={['/unknown-route']}>
        <App />
      </MemoryRouter>
    );
    expect(screen.getByTestId('login-form')).toBeInTheDocument();
  });

  it('shows dashboard page on /dashboard route when user is authenticated', () => {
    // Override the useAuth mock to simulate authenticated user
    const useAuth = require('../context/AuthContext').useAuth;
    vi.spyOn(require('../context/AuthContext'), 'useAuth').mockReturnValue({
      user: { id: '1', name: 'Test User' },
      token: 'fake-token',
      login: vi.fn(),
      logout: vi.fn(),
      isLoading: false,
    });

    render(
      <MemoryRouter initialEntries={['/dashboard']}>
        <App />
      </MemoryRouter>
    );
    expect(screen.getByTestId('dashboard-page')).toBeInTheDocument();
  });

  it('redirects to login page on /dashboard if user is not authenticated', () => {
    render(
      <MemoryRouter initialEntries={['/dashboard']}>
        <App />
      </MemoryRouter>
    );
    expect(screen.getByTestId('login-form')).toBeInTheDocument();
  });
});