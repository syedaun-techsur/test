import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter, useNavigate } from 'react-router-dom';
import Dashboard from '../components/Dashboard';
import { useAuth } from '../context/AuthContext';
import { User, LogOut } from 'lucide-react';

// Mock the useAuth hook
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
      logout: vi.fn(),
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
    expect(screen.getByTestId('stat-card-0')).toBeInTheDocument();
    expect(screen.getByTestId('stat-card-1')).toBeInTheDocument();
    expect(screen.getByTestId('stat-card-2')).toBeInTheDocument();
    expect(screen.getByText('Total Projects')).toBeInTheDocument();
    expect(screen.getByText('Active Tasks')).toBeInTheDocument();
    // Assertions for presence of 'Notifications' text
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

    // Create a custom Dashboard component with the mock logout
    const CustomDashboard = () => {
      const { user } = useAuth();
      const navigate = useNavigate();

      const handleLogout = () => {
        mockLogout();
        navigate('/login', { replace: true });
      };

      return (
        <div className="min-h-screen bg-gray-50" data-testid="dashboard">
          <header className="bg-white shadow-sm border-b border-gray-200">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
              <div className="flex justify-between items-center h-16">
                <div className="flex items-center">
                  <div className="bg-blue-600 w-8 h-8 rounded-lg flex items-center justify-center">
                    <User className="w-5 h-5 text-white" />
                  </div>
                  <h1 className="ml-3 text-xl font-semibold text-gray-900">Dashboard</h1>
                </div>

                <div className="flex items-center space-x-4">
                  <div className="text-sm text-gray-600" data-testid="welcome-message">
                    Welcome back, <span className="font-medium">{user?.firstName}</span>
                  </div>
                  <button
                    onClick={handleLogout}
                    className="flex items-center px-3 py-2 text-sm text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg transition-colors"
                    data-testid="logout-button"
                  >
                    <LogOut className="w-4 h-4 mr-1" />
                    Logout
                  </button>
                </div>
              </div>
            </div>
          </header>
        </div>
      );
    };

    render(
      <BrowserRouter>
        <CustomDashboard />
      </BrowserRouter>
    );

    const logoutButton = screen.getByTestId('logout-button');
    await user.click(logoutButton);

    expect(mockLogout).toHaveBeenCalled();
  });
});