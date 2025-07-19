import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import { AuthProvider } from '../context/AuthContext';

// Mock fetch with appropriate typing
const mockFetch = vi.fn() as unknown as typeof fetch;
global.fetch = mockFetch;

const MockedLoginForm = () => (
  <BrowserRouter>
    <AuthProvider>
      <LoginForm />
    </AuthProvider>
  </BrowserRouter>
);

describe('LoginForm', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  afterEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  it('renders login form with all required elements', (): void => {
    render(<MockedLoginForm />);
    
    expect(screen.getByText('Welcome Back')).toBeInTheDocument();
    expect(screen.getByText('Login to your account')).toBeInTheDocument();
    expect(screen.getByTestId('email-input')).toBeInTheDocument();
    expect(screen.getByTestId('password-input')).toBeInTheDocument();
    expect(screen.getByTestId('login-button')).toBeInTheDocument();
    expect(screen.getByText('Demo credentials: admin@example.com / password123')).toBeInTheDocument();
  });

  it('shows validation errors for empty fields', async (): Promise<void> => {
    const user = userEvent.setup();
    render(<MockedLoginForm />);
    
    const loginButton = screen.getByTestId('login-button');
    await user.click(loginButton);
    
    expect(screen.getByTestId('email-error')).toHaveTextContent('Email is required');
    expect(screen.getByTestId('password-error')).toHaveTextContent('Password is required');
  });

  it('shows validation error for invalid email format', async (): Promise<void> => {
    const user = userEvent.setup();
    render(<MockedLoginForm />);
    
    const emailInput = screen.getByTestId('email-input');
    const passwordInput = screen.getByTestId('password-input');
    
    await user.clear(emailInput);
    await user.type(emailInput, 'invalid-email');
    await user.type(passwordInput, 'password123');
    
    // Submit form via userEvent to better simulate user action
    const loginForm = screen.getByTestId('login-form');
    await user.click(screen.getByTestId('login-button'));

    const emailError = await screen.findByTestId('email-error');
    expect(emailError).toHaveTextContent('Please enter a valid email address');
  });

  it('shows validation error for short password', async (): Promise<void> => {
    const user = userEvent.setup();
    render(<MockedLoginForm />);
    
    const passwordInput = screen.getByTestId('password-input');
    const loginButton = screen.getByTestId('login-button');
    
    await user.type(passwordInput, '123');
    await user.click(loginButton);
    
    expect(screen.getByTestId('password-error')).toHaveTextContent('Password must be at least 6 characters');
  });

  it('toggles password visibility', async (): Promise<void> => {
    const user = userEvent.setup();
    render(<MockedLoginForm />);
    
    const passwordInput = screen.getByTestId('password-input') as HTMLInputElement;
    const toggleButton = screen.getByTestId('toggle-password');
    
    expect(passwordInput.type).toBe('password');
    
    await user.click(toggleButton);
    expect(passwordInput.type).toBe('text');
    
    await user.click(toggleButton);
    expect(passwordInput.type).toBe('password');
  });

  it('submits form with valid credentials', async (): Promise<void> => {
    const user = userEvent.setup();
    const mockResponse = {
      token: 'mock-token',
      user: { id: 1, email: 'admin@example.com', firstName: 'John', lastName: 'Doe' },
      message: 'Login successful'
    };
    
    mockFetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    } as Response);
    
    render(<MockedLoginForm />);
    
    const emailInput = screen.getByTestId('email-input');
    const passwordInput = screen.getByTestId('password-input');
    const loginButton = screen.getByTestId('login-button');
    
    await user.type(emailInput, 'admin@example.com');
    await user.type(passwordInput, 'password123');
    await user.click(loginButton);
    
    expect(global.fetch).toHaveBeenCalledWith('http://localhost:8080/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email: 'admin@example.com', password: 'password123' }),
    });
  });

  it('shows error message on login failure', async (): Promise<void> => {
    const user = userEvent.setup();
    const mockErrorResponse = {
      message: 'Invalid email or password'
    };
    
    mockFetch.mockResolvedValueOnce({
      ok: false,
      json: async () => mockErrorResponse,
    } as Response);
    
    render(<MockedLoginForm />);
    
    const emailInput = screen.getByTestId('email-input');
    const passwordInput = screen.getByTestId('password-input');
    const loginButton = screen.getByTestId('login-button');
    
    await user.type(emailInput, 'admin@example.com');
    await user.type(passwordInput, 'wrongpassword');
    await user.click(loginButton);
    
    await waitFor(() => {
      expect(screen.getByTestId('error-message')).toHaveTextContent('Invalid email or password');
    });
  });
});