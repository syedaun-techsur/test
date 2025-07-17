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

// Mock the components used in App routing
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

  describe('Default route "/"', () => {
    it('redirects to login page', () => {
      render(
        <MemoryRouter initialEntries={['/']}>
          <App />
        </MemoryRouter>
      );
      expect(screen.getByTestId('login-form')).toBeInTheDocument();
    });
  });

  describe('/login route', () => {
    it('shows login page', () => {
      render(
        <MemoryRouter initialEntries={['/login']}>
          <App />
        </MemoryRouter>
      );
      expect(screen.getByTestId('login-form')).toBeInTheDocument();
    });
  });

  describe('Unknown routes', () => {
    it('redirects unknown routes to login page', () => {
      render(
        <MemoryRouter initialEntries={['/some/unknown/path']}>
          <App />
        </MemoryRouter>
      );
      expect(screen.getByTestId('login-form')).toBeInTheDocument();
    });
  });
});