import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter, useNavigate } from 'react-router-dom';
import Dashboard from '../components/Dashboard';
import { useAuth } from '../context/AuthContext';
import { User, LogOut } from 'lucide-react';

// Create a reusable mockLogout function
const mockLogout = vi.fn();

// Mock the useAuth hook with a reusable logout mock
vi.mock('../context/AuthContext', async () => {
  const actual = await vi.importActual('../context/AuthContext');
  return {
    ...actual,
    useAuth: () => ({
      user: {
        id: 1,
        email: 'admin@example.com',
        firstName: 'John',
        lastName: 'Doe',
      },
      logout: mockLogout,
      token: 'mock-token',
      isLoading: false,
    }),
  };
});

// Mock useNavigate to spy on navigation calls
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => vi.fn(),
  };
});

describe('Dashboard', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderDashboard = () =>
    render(
      <BrowserRouter>
        <Dashboard />
      </BrowserRouter>
    );

  it('renders dashboard with user information', () => {
    renderDashboard();

    expect(screen.getByTestId('dashboard')).toBeInTheDocument();
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByTestId('welcome-message')).toHaveTextContent('Welcome back, John');
    expect(screen.getByText('Good morning, John!')).toBeInTheDocument();
  });

  it('displays user profile information', () => {
    renderDashboard();

    const userInfoCard = screen.getByTestId('user-info-card');
    expect(userInfoCard).toBeInTheDocument();

    expect(screen.getByTestId('first-name')).toHaveTextContent('John');
    expect(screen.getByTestId('last-name')).toHaveTextContent('Doe');
    expect(screen.getByTestId('email')).toHaveTextContent('admin@example.com');
  });

  it('displays statistics cards', () => {
    renderDashboard();
    expect(screen.getByTestId('stat-card-0')).toBeInTheDocument();
    expect(screen.getByTestId('stat-card-1')).toBeInTheDocument();
    expect(screen.getByTestId('stat-card-2')).toBeInTheDocument();
    expect(screen.getByText('Total Projects')).toBeInTheDocument();
    expect(screen.getByText('Active Tasks')).toBeInTheDocument();
    expect(screen.getAllByText('Notifications').length).toBeGreaterThan(0);
  });

  it('displays quick action buttons', () => {
    renderDashboard();

    expect(screen.getByTestId('update-profile-btn')).toBeInTheDocument();
    expect(screen.getByTestId('security-settings-btn')).toBeInTheDocument();
    expect(screen.getByTestId('notifications-btn')).toBeInTheDocument();
  });

  it('calls logout function and navigates when logout button is clicked', async () => {
    // We need to mock useNavigate to track calls
    const mockNavigate = vi.fn();
    (useNavigate as unknown as () => typeof mockNavigate).mockReturnValue(mockNavigate);

    const user = userEvent.setup();

    renderDashboard();

    const logoutButton = screen.getByTestId('logout-button');
    await user.click(logoutButton);

    expect(mockLogout).toHaveBeenCalledTimes(1);
    expect(mockNavigate).toHaveBeenCalledWith('/login', { replace: true });
  });
});