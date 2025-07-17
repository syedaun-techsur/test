import React, { ReactNode } from 'react';
import { Redirect } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

interface ProtectedRouteProps {
  children: ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }): JSX.Element => {
  const { user, isLoading } = useAuth();

  // Show loading spinner while auth state is loading
  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center" data-testid="loading-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4" />
          <p className="text-gray-600">Loading...</p>
        </div>
      </div>
    );
  }

  // Redirect unauthenticated users to login page
  if (!user) {
    return <Redirect to="/login" />;
  }

  // Render protected content for authenticated users
  return <>{children}</>;
};

export default ProtectedRoute;