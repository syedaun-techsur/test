
import React from 'react';
import { useAuth } from '../context/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const { user, loading, isAuthenticated } = useAuth();

  // Show loading state while checking authentication
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900" data-testid="loading-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-white"></div>
      </div>
    );
  }

  // If not authenticated, don't render protected content
  // Parent component will handle showing auth form
  if (!isAuthenticated || !user) {
    console.log('ProtectedRoute: Access denied - user not authenticated');
    return null;
  }

  console.log('ProtectedRoute: Access granted for user:', user.email);
  return <>{children}</>;
};

export default ProtectedRoute;

