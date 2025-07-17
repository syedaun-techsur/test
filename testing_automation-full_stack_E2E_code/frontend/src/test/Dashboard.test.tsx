import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import Dashboard from '../components/Dashboard';
import { useAuth } from '../context/AuthContext';

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

describe('Dashboard Component', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const wrapper: React.FC<{ children: React.ReactNode }> = ({ children }) => (
    <BrowserRouter>{children}</BrowserRouter>
  );

  it('renders dashboard with user greeting and header', () => {
    render(<Dashboard />, { wrapper });

    expect(screen.getByTestId('dashboard')).toBeInTheDocument();
    expect(screen.getByText('Dashboard')).toBeInTheDocument();
    expect(screen.getByTestId('welcome-message')).toHaveTextContent('Welcome back, John');
    expect(screen.getByText('Good morning, John!')).toBeInTheDocument();
  });

  it('displays correct user profile information', () => {
    render(<Dashboard />, { wrapper });

    const userInfoCard = screen.getByTestId('user-info-card');
    expect(userInfoCard).toBeInTheDocument();

    expect(screen.getByTestId('first-name')).toHaveTextContent('John');
    expect(screen.getByTestId('last-name')).toHaveTextContent('Doe');
    expect(screen.getByTestId('email')).toHaveTextContent('admin@example.com');
  });

  it('displays all statistics cards properly', () => {
    render(<Dashboard />, { wrapper });

    expect(screen.getByTestId('stat-card-total-projects')).toBeInTheDocument();
    expect(screen.getByTestId('stat-card-active-tasks')).toBeInTheDocument();
    expect(screen.getByTestId('stat-card-notifications')).toBeInTheDocument();

    expect(screen.getByText('Total Projects')).toBeInTheDocument();
    expect(screen.getByText('Active Tasks')).toBeInTheDocument();
    // Use getAllByText for Notifications text to ensure presence
    expect(screen.getAllByText('Notifications').length).toBeGreaterThan(0);
  });

  it('renders all quick action buttons', () => {
    render(<Dashboard />, { wrapper });

    expect(screen.getByTestId('update-profile-btn')).toBeInTheDocument();
    expect(screen.getByTestId('security-settings-btn')).toBeInTheDocument();
    expect(screen.getByTestId('notifications-btn')).toBeInTheDocument();
  });

  it('calls logout function when logout button is clicked', async () => {
    const user = userEvent.setup();

    // Access the mocked useAuth to get logout mock reference
    const { useAuth: mockedUseAuth } = await vi.importMock('../context/AuthContext');
    const logoutMock = mockedUseAuth().logout as jest.Mock | ReturnType<typeof vi.fn>;

    render(<Dashboard />, { wrapper });

    const logoutButton = screen.getByTestId('logout-button');
    await user.click(logoutButton);

    expect(logoutMock).toHaveBeenCalled();
  });
});