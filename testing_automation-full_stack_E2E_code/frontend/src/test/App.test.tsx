import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import App from '../App';

// Mock the auth context
vi.mock('../context/AuthContext', () => ({
  AuthProvider: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
  useAuth: () => ({
    user: null,
    token: null,
    login: vi.fn(() => {}),
    logout: vi.fn(() => {}),
    isLoading: false
  })
}));

// Mock the LoginForm component used in App
vi.mock('../components/LoginForm', () => ({
  default: () => <div data-testid="login-page">Login Page</div>
}));

// Mock the Dashboard component used in App
vi.mock('../components/Dashboard', () => ({
  default: () => <div data-testid="dashboard-page">Dashboard Page</div>
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
      <MemoryRouter initialEntries={['/some/unknown/path']}>
        <App />
      </MemoryRouter>
    );
    expect(screen.getByTestId('login-page')).toBeInTheDocument();
  });
});