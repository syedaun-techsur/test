import React from 'react';
import { useAuth } from '../context/AuthContext';
import Auth from './Auth';
import Dashboard from '../components/Dashboard';
import ProtectedRoute from '../components/ProtectedRoute';
import { AuthProvider } from '../context/AuthContext';

const AppContent = () => {
  const { user, loading, isAuthenticated } = useAuth();

  // Show loading state while initializing authentication
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900" data-testid="app-loading">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-white"></div>
      </div>
    );
  }

  // If user is authenticated, show protected dashboard
  if (isAuthenticated && user) {
    return (
      <ProtectedRoute>
        <Dashboard />
      </ProtectedRoute>
    );
  }

  // If not authenticated, show auth form
  return <Auth />;
};

const Index = () => {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
};

export default Index;