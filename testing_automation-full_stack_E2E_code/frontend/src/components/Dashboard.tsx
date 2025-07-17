import React, { FC } from 'react';
import { useHistory } from 'react-router-dom';
import { User, Settings, LogOut, Activity, Bell, Calendar } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

interface Stat {
  label: string;
  value: string | number;
  icon: FC<React.SVGProps<SVGSVGElement>>;
  color: string;
}

const Dashboard: FC = () => {
  const { user, logout } = useAuth();
  const history = useHistory();

  const handleLogout = (): void => {
    logout();
    history.replace('/login');
  };

  const stats: Stat[] = [
    { label: 'Total Projects', value: 12, icon: Activity, color: 'bg-blue-500' },
    { label: 'Active Tasks', value: 8, icon: Calendar, color: 'bg-green-500' },
    { label: 'Notifications', value: 3, icon: Bell, color: 'bg-yellow-500' },
  ];

  return (
    <div className="min-h-screen bg-gray-50" data-testid="dashboard">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <div className="bg-blue-600 w-8 h-8 rounded-lg flex items-center justify-center">
                <User className="w-5 h-5 text-white" aria-hidden="true" />
              </div>
              <h1 className="ml-3 text-xl font-semibold text-gray-900">Dashboard</h1>
            </div>

            <div className="flex items-center space-x-4">
              <div className="text-sm text-gray-600" data-testid="welcome-message">
                Welcome back, <span className="font-medium">{user?.firstName ?? 'User'}</span>
              </div>
              <button
                type="button"
                onClick={handleLogout}
                className="flex items-center px-3 py-2 text-sm text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg transition-colors"
                data-testid="logout-button"
                aria-label="Logout"
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
          {stats.map((stat, index) => (
            <div
              key={index}
              className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow"
              data-testid={`stat-card-${index}`}
              role="region"
              aria-label={`${stat.label} statistic`}
            >
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">{stat.label}</p>
                  <p className="text-3xl font-bold text-gray-900 mt-2">{stat.value}</p>
                </div>
                <div className={`${stat.color} p-3 rounded-lg`}>
                  <stat.icon className="w-6 h-6 text-white" aria-hidden="true" />
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* User Info Card */}
        <section
          className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-8"
          data-testid="user-info-card"
          aria-labelledby="profile-info-heading"
        >
          <h3
            id="profile-info-heading"
            className="text-lg font-semibold text-gray-900 mb-4 flex items-center"
          >
            <User className="w-5 h-5 mr-2" aria-hidden="true" />
            Profile Information
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label htmlFor="first-name-display" className="block text-sm font-medium text-gray-700 mb-1">
                First Name
              </label>
              <p
                id="first-name-display"
                className="text-gray-900 bg-gray-50 px-3 py-2 rounded-lg"
                data-testid="first-name"
              >
                {user?.firstName ?? '-'}
              </p>
            </div>
            <div>
              <label htmlFor="last-name-display" className="block text-sm font-medium text-gray-700 mb-1">
                Last Name
              </label>
              <p
                id="last-name-display"
                className="text-gray-900 bg-gray-50 px-3 py-2 rounded-lg"
                data-testid="last-name"
              >
                {user?.lastName ?? '-'}
              </p>
            </div>
            <div className="md:col-span-2">
              <label htmlFor="email-display" className="block text-sm font-medium text-gray-700 mb-1">
                Email Address
              </label>
              <p
                id="email-display"
                className="text-gray-900 bg-gray-50 px-3 py-2 rounded-lg"
                data-testid="email"
              >
                {user?.email ?? '-'}
              </p>
            </div>
          </div>
        </section>

        {/* Quick Actions */}
        <section
          className="bg-white rounded-xl shadow-sm border border-gray-200 p-6"
          aria-labelledby="quick-actions-heading"
        >
          <h3
            id="quick-actions-heading"
            className="text-lg font-semibold text-gray-900 mb-4 flex items-center"
          >
            <Settings className="w-5 h-5 mr-2" aria-hidden="true" />
            Quick Actions
          </h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            <button
              type="button"
              className="p-4 text-left border border-gray-200 rounded-lg hover:border-blue-300 hover:bg-blue-50 transition-colors"
              data-testid="update-profile-btn"
            >
              <h4 className="font-medium text-gray-900">Update Profile</h4>
              <p className="text-sm text-gray-600 mt-1">Change your personal information</p>
            </button>
            <button
              type="button"
              className="p-4 text-left border border-gray-200 rounded-lg hover:border-blue-300 hover:bg-blue-50 transition-colors"
              data-testid="security-settings-btn"
            >
              <h4 className="font-medium text-gray-900">Security Settings</h4>
              <p className="text-sm text-gray-600 mt-1">Manage your password and security</p>
            </button>
            <button
              type="button"
              className="p-4 text-left border border-gray-200 rounded-lg hover:border-blue-300 hover:bg-blue-50 transition-colors"
              data-testid="notifications-btn"
            >
              <h4 className="font-medium text-gray-900">Notifications</h4>
              <p className="text-sm text-gray-600 mt-1">Configure your notification preferences</p>
            </button>
          </div>
        </section>
      </main>
    </div>
  );
};

export default Dashboard;