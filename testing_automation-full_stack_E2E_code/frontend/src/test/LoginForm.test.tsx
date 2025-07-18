import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import { AuthProvider } from '../context/AuthContext';

const globalAny = global as any;

// Mock fetch
globalAny.fetch = vi.fn();

const MockedLoginForm: React.FC = () => (
  <BrowserRouter>
    <AuthProvider>
      <LoginForm />
    </AuthProvider>
  </BrowserRouter>
);

describe('LoginForm Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  it('renders login form with all required elements', () => {
    render(<MockedLoginForm />);
    
    expect(screen.getByText('Welcome Back')).toBeInTheDocument();
    expect(screen.getByText('Login to your account')).toBeInTheDocument();
    expect(screen.getByTestId('email-input')).toBeInTheDocument();
    expect(screen.getByTestId('password-input')).toBeInTheDocument();
    expect(screen.getByTestId('login-button')).toBeInTheDocument();
    expect(screen.getByText('Demo credentials: admin@example.com / password123')).toBeInTheDocument();
  });

  it('shows validation errors when fields are empty and form is submitted', async () => {
    const user = userEvent.setup();
    render(<MockedLoginForm />);
    
    const loginButton = screen.getByTestId('login-button');
    await user.click(loginButton);
    
    expect(screen.getByTestId('email-error')).toHaveTextContent('Email is required');
    expect(screen.getByTestId('password-error')).toHaveTextContent('Password is required');
  });

  it('shows validation error for invalid email format', async () => {
    const user = userEvent.setup();
    render(<MockedLoginForm />);
    
    const emailInput = screen.getByTestId('email-input');
    const passwordInput = screen.getByTestId('password-input');
    const loginForm = screen.getByTestId('login-form');
    
    await user.clear(emailInput);
    await user.type(emailInput, 'invalid-email');
    await user.type(passwordInput, 'password123');
    
    fireEvent.submit(loginForm);
    
    const emailError = await screen.findByTestId('email-error');
    expect(emailError).toHaveTextContent('Please enter a valid email address');
  });

  it('shows validation error when password length is less than 6', async () => {
    const user = userEvent.setup();
    render(<MockedLoginForm />);
    
    const passwordInput = screen.getByTestId('password-input');
    const loginButton = screen.getByTestId('login-button');
    
    await user.type(passwordInput, '123');
    await user.click(loginButton);
    
    expect(screen.getByTestId('password-error')).toHaveTextContent('Password must be at least 6 characters');
  });

  it('toggles password visibility when toggle button is clicked', async () => {
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

  it('submits form successfully with valid credentials', async () => {
    const user = userEvent.setup();
    const successResponse = {
      token: 'mock-token',
      user: { id: 1, email: 'admin@example.com', firstName: 'John', lastName: 'Doe' },
      message: 'Login successful'
    };
    
    globalAny.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => successResponse,
    });
    
    render(<MockedLoginForm />);
    
    const emailInput = screen.getByTestId('email-input');
    const passwordInput = screen.getByTestId('password-input');
    const loginButton = screen.getByTestId('login-button');
    
    await user.type(emailInput, 'admin@example.com');
    await user.type(passwordInput, 'password123');
    await user.click(loginButton);
    
    expect(globalAny.fetch).toHaveBeenCalledWith('http://localhost:8080/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email: 'admin@example.com', password: 'password123' }),
    });
  });

  it('displays server error message on login failure', async () => {
    const user = userEvent.setup();
    const failureResponse = {
      message: 'Invalid email or password'
    };
    
    globalAny.fetch.mockResolvedValueOnce({
      ok: false,
      json: async () => failureResponse,
    });
    
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