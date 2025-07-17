import React from 'react';
import { BrowserRouter, Switch, Route, Redirect } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import LoginForm from './components/LoginForm';
import Dashboard from './components/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';

const App: React.FC = () => {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="min-h-screen bg-gray-50">
          <Switch>
            {/* Default route redirects to login */}
            <Route exact path="/">
              <Redirect to="/login" />
            </Route>

            {/* Login route */}
            <Route path="/login" component={LoginForm} />

            {/* Protected dashboard route */}
            <ProtectedRoute path="/dashboard">
              <Dashboard />
            </ProtectedRoute>

            {/* Catch all route redirects to login */}
            <Route path="*">
              <Redirect to="/login" />
            </Route>
          </Switch>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
};

export default App;