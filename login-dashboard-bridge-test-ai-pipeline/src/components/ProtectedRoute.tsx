import React, { ReactNode } from 'react';
import { useAuth } from '../context/AuthContext';

interface ProtectedRouteProps {
  children: ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const { user, loading, isAuthenticated } = useAuth();

  // Show loading state while checking authentication
  if (loading) {
    return (
      <div
        className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900"
        data-testid="loading-screen"
        aria-busy="true"
        aria-live="polite"
      >
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-white" />
      </div>
    );
  }

  // If not authenticated, don't render protected content.
  // Parent component is responsible for showing auth forms or redirect.
  if (!isAuthenticated) {
    if (process.env.NODE_ENV !== 'production') {
      console.log('ProtectedRoute: Access denied - user not authenticated');
    }
    return null;
  }

  if (process.env.NODE_ENV !== 'production') {
    console.log('ProtectedRoute: Access granted for user:', user?.email);
  }

  return <>{children}</>;
};

export default ProtectedRoute;