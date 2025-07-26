import React from 'react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import App from '../App';

// Mock the auth context
vi.mock('../context/AuthContext', () => ({
  AuthProvider: ({ children }: { children: React.ReactNode }): JSX.Element => <>{children}</>,
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
  default: () => <div data-testid="login-page">Login Page</div>,
}));

vi.mock('../pages/DashboardPage', () => ({
  default: () => <div data-testid="dashboard-page">Dashboard Page</div>,
}));

describe('App Routing', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('redirects to login page by default', () => {
    render(<App />);
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });

  it('shows login page on /login route', () => {
    render(<App />);
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });

  it('redirects unknown routes to login', () => {
    render(<App />);
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });
});