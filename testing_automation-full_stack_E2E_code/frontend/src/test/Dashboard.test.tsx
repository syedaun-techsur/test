import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import Dashboard from '../components/Dashboard';
import { AuthProvider, UserProvider, useAuth } from '../context/AuthContext';

// Mock the useAuth hook to return user and logout correctly
vi.mock('../context/AuthContext', async () => {
  const actual = await vi.importActual('../context/AuthContext');
  return {
    ...actual,
    useAuth: () => ({
      user: {
        id: 1,
        email: 'admin@example.com',
        firstName: 'John',
        lastName: 'Doe'
      },
      logout: vi.fn(),
    })
  };
});

describe('Dashboard', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const renderDashboard = () => {
    return render(
      <BrowserRouter>
        <AuthProvider>
          <UserProvider>
            <Dashboard />
          </UserProvider>
        </AuthProvider>
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

  it('calls logout function when logout button is clicked', async () => {
    const user = userEvent.setup();

    const mockLogout = vi.fn();
    // Spy on useAuth to override logout for this test
    const useAuthModule = await import('../context/AuthContext');
    vi.spyOn(useAuthModule, 'useAuth').mockReturnValue({
      user: {
        id: 1,
        email: 'admin@example.com',
        firstName: 'John',
        lastName: 'Doe'
      },
      logout: mockLogout,
    });

    render(
      <BrowserRouter>
        <AuthProvider>
          <UserProvider>
            <Dashboard />
          </UserProvider>
        </AuthProvider>
      </BrowserRouter>
    );

    const logoutButton = screen.getByTestId('logout-button');
    await user.click(logoutButton);

    expect(mockLogout).toHaveBeenCalled();
  });
});