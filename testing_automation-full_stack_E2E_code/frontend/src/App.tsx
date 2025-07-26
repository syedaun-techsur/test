import React, { FC } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginForm from './components/LoginForm';
import Dashboard from './components/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';

const App: FC = () => {
  return (
    <div className="min-h-screen bg-gray-50">
      <AuthProvider>
        <Router>
          <Routes>
            {/* Redirect '/' to '/login' */}
            <Route path="/" element={<Navigate to="/login" replace />} />

            {/* Public login route */}
            <Route path="/login" element={<LoginForm />} />

            {/* Protected dashboard route */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <Dashboard />
                </ProtectedRoute>
              }
            />

            {/* Fallback redirect to login */}
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </Router>
      </AuthProvider>
    </div>
  );
};

export default App;