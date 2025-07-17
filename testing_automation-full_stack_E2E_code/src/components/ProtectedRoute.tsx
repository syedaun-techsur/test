import React, { ReactNode, useMemo } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

interface ProtectedRouteProps {
  children: ReactNode;
}

const LoadingSpinner: React.FC = () => (
  <div className="text-center">
    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4" />
    <p className="text-gray-600">Loading...</p>
  </div>
);

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }): JSX.Element => {
  const { user, isLoading } = useAuth();

  // Memoize loading spinner for performance
  const loadingContent = useMemo(
    () => (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center" data-testid="loading-screen">
        <LoadingSpinner />
      </div>
    ),
    []
  );

  if (isLoading) {
    return loadingContent;
  }

  if (!user) {
    // Redirect unauthenticated users to login page
    return <Navigate to="/login" replace />;
  }

  // Render protected content for authenticated users
  return <>{children}</>;
};

export default ProtectedRoute;