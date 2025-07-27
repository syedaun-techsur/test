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
vi.mock('../pages/LoginPage', () => ({
  __esModule: true,
  default: () => <div data-testid="login-page">Login Page</div>,
}));

vi.mock('../pages/DashboardPage', () => ({
  __esModule: true,
  default: () => <div data-testid="dashboard-page">Dashboard Page</div>,
}));

describe('App Routing', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('redirects to login page by default', () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>
    );
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });

  it('shows login page on /login route', () => {
    render(
      <MemoryRouter initialEntries={['/login']}>
        <App />
      </MemoryRouter>
    );
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });

  it('redirects unknown routes to login page', () => {
    render(
      <MemoryRouter initialEntries={['/unknown-route']}>
        <App />
      </MemoryRouter>
    );
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });
});