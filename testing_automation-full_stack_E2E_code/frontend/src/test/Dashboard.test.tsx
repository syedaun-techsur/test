import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import Dashboard from '../components/Dashboard';
import { useAuth as useAuthOriginal } from '../context/AuthContext';
import { User, LogOut } from 'lucide-react';

// Mock the useAuth hook
const mockLogout = vi.fn();
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

describe('Dashboard', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderDashboard = () => {
    return render(
      <BrowserRouter>
        <Dashboard />
      </BrowserRouter>
    );
  };

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

    expect(screen.getByTestId('stat-card-Total Projects')).toBeInTheDocument();
    expect(screen.getByTestId('stat-card-Active Tasks')).toBeInTheDocument();
    expect(screen.getByTestId('stat-card-Notifications')).toBeInTheDocument();

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

  it('calls logout function when logout button is clicked', async () => {
    renderDashboard();
    const user = userEvent.setup();

    const logoutButton = screen.getByTestId('logout-button');
    await user.click(logoutButton);

    expect(mockLogout).toHaveBeenCalledTimes(1);
  });
});