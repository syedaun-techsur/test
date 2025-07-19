import React, { FC } from 'react';
import { useAuth } from '../context/AuthContext';
import Auth from './Auth';
import Dashboard from '../components/Dashboard';
import ProtectedRoute from '../components/ProtectedRoute';
import { AuthProvider } from '../context/AuthContext';

const AppContent: FC = () => {
  const { user, loading, isAuthenticated } = useAuth();

  // Show loading state while initializing authentication
  if (loading) {
    return (
      <div
        className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900"
        data-testid="app-loading"
        aria-label="Loading application"
      >
        <div
          className="animate-spin rounded-full h-12 w-12 border-b-2 border-white"
          role="status"
          aria-live="polite"
          aria-label="Loading spinner"
        ></div>
      </div>
    );
  }

  // Show dashboard if authenticated, else show auth form
  return (
    <>
      {isAuthenticated && user ? (
        <ProtectedRoute>
          <Dashboard />
        </ProtectedRoute>
      ) : (
        <Auth />
      )}
    </>
  );
};

const Index: FC = () => {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
};

export default Index;