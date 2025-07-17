import React, { useCallback, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { User, Settings, LogOut, Activity, Bell, Calendar } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const Dashboard: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = useCallback(() => {
    logout();
    navigate('/login', { replace: true });
  }, [logout, navigate]);

  const stats = useMemo(
    () => [
      { label: 'Total Projects', value: '12', icon: Activity, color: 'bg-blue-500' },
      { label: 'Active Tasks', value: '8', icon: Calendar, color: 'bg-green-500' },
      { label: 'Notifications', value: '3', icon: Bell, color: 'bg-yellow-500' },
    ],
    []
  );

  return (
    <div className="min-h-screen bg-gray-50" data-testid="dashboard">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <div className="bg-blue-600 w-8 h-8 rounded-lg flex items-center justify-center" aria-hidden="true">
                <User className="w-5 h-5 text-white" />
              </div>
              <h1 className="ml-3 text-xl font-semibold text-gray-900">Dashboard</h1>
            </div>

            <div className="flex items-center space-x-4">
              <div className="text-sm text-gray-600" data-testid="welcome-message">
                Welcome back, <span className="font-medium">{user?.firstName ?? 'User'}</span>
              </div>
              <button
                onClick={handleLogout}
                aria-label="Logout"
                className="flex items-center px-3 py-2 text-sm text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg transition-colors"
                data-testid="logout-button"
              >
                <LogOut className="w-4 h-4 mr-1" aria-hidden="true" />
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Welcome Section */}
        <div className="mb-8">
          <h2 className="text-2xl font-bold text-gray-900 mb-2">
            Good morning, {user?.firstName ?? 'User'}!
          </h2>
          <p className="text-gray-600">Here's what's happening with your account today.</p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          {stats.map(({ label, value, icon: Icon, color }) => (
            <div
              key={label}
              className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow"
              data-testid={`stat-card-${label.replace(/\s+/g, '-').toLowerCase()}`}
            >
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">{label}</p>
                  <p className="text-3xl font-bold text-gray-900 mt-2">{value}</p>
                </div>
                <div className={`${color} p-3 rounded-lg`} aria-hidden="true">
                  <Icon className="w-6 h-6 text-white" />
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* User Info Card */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-8" data-testid="user-info-card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
            <User className="w-5 h-5 mr-2" aria-hidden="true" />
            Profile Information
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1" htmlFor="first-name">
                First Name
              </label>
              <p
                id="first-name"
                className="text-gray-900 bg-gray-50 px-3 py-2 rounded-lg"
                data-testid="first-name"
              >
                {user?.firstName ?? 'N/A'}
              </p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1" htmlFor="last-name">
                Last Name
              </label>
              <p
                id="last-name"
                className="text-gray-900 bg-gray-50 px-3 py-2 rounded-lg"
                data-testid="last-name"
              >
                {user?.lastName ?? 'N/A'}
              </p>
            </div>
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-1" htmlFor="email">
                Email Address
              </label>
              <p
                id="email"
                className="text-gray-900 bg-gray-50 px-3 py-2 rounded-lg"
                data-testid="email"
              >
                {user?.email ?? 'N/A'}
              </p>
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
            <Settings className="w-5 h-5 mr-2" aria-hidden="true" />
            Quick Actions
          </h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            <button
              className="p-4 text-left border border-gray-200 rounded-lg hover:border-blue-300 hover:bg-blue-50 transition-colors"
              data-testid="update-profile-btn"
              type="button"
            >
              <h4 className="font-medium text-gray-900">Update Profile</h4>
              <p className="text-sm text-gray-600 mt-1">Change your personal information</p>
            </button>
            <button
              className="p-4 text-left border border-gray-200 rounded-lg hover:border-blue-300 hover:bg-blue-50 transition-colors"
              data-testid="security-settings-btn"
              type="button"
            >
              <h4 className="font-medium text-gray-900">Security Settings</h4>
              <p className="text-sm text-gray-600 mt-1">Manage your password and security</p>
            </button>
            <button
              className="p-4 text-left border border-gray-200 rounded-lg hover:border-blue-300 hover:bg-blue-50 transition-colors"
              data-testid="notifications-btn"
              type="button"
            >
              <h4 className="font-medium text-gray-900">Notifications</h4>
              <p className="text-sm text-gray-600 mt-1">Configure your notification preferences</p>
            </button>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;