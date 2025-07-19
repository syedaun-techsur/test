import React, { ReactElement } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }): ReactElement => {
  const { user, isLoading } = useAuth();

  // Show loading spinner while auth state is being determined
  if (isLoading) {
    return (
      <main
        className="min-h-screen bg-gray-50 flex items-center justify-center"
        role="alert"
        aria-busy="true"
        aria-live="polite"
        data-testid="loading-screen"
      >
        <div className="text-center">
          <div
            className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"
            aria-label="Loading"
          />
          <p className="text-gray-600">Loading...</p>
        </div>
      </main>
    );
  }

  // Redirect to login if no authenticated user
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // Render protected content if user is authenticated
  return <>{children}</>;
};

export default ProtectedRoute;