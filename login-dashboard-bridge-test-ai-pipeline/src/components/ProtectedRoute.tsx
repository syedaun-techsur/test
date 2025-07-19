import React, { useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const { user, loading, isAuthenticated } = useAuth();

  useEffect(() => {
    if (!loading) {
      if (!isAuthenticated || !user) {
        console.log('ProtectedRoute: Access denied - user not authenticated');
      } else {
        console.log('ProtectedRoute: Access granted for user:', user.email ?? 'unknown email');
      }
    }
  }, [loading, isAuthenticated, user]);

  if (loading) {
    return (
      <div
        className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900"
        data-testid="loading-screen"
      >
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-white"></div>
      </div>
    );
  }

  if (!isAuthenticated || !user) {
    return <div data-testid="access-denied" />;
  }

  return children;
};

export default ProtectedRoute;