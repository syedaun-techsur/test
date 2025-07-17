import React, { createContext, useContext, useState, useEffect, useCallback, useMemo } from 'react';

interface User {
  readonly id: number;
  readonly email: string;
  readonly firstName: string;
  readonly lastName: string;
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (email: string, password: string) => Promise<{ success: boolean; message?: string }>;
  logout: () => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // On mount, load token and user from localStorage safely
  useEffect(() => {
    try {
      const savedToken = localStorage.getItem('authToken');
      const savedUser = localStorage.getItem('user');

      if (savedToken && savedUser) {
        const parsedUser = JSON.parse(savedUser);
        if (parsedUser) {
          setToken(savedToken);
          setUser(parsedUser);
        }
      }
    } catch (e) {
      console.error('Failed to parse auth data from localStorage:', e);
    } finally {
      setIsLoading(false);
    }
  }, []);

  // Login function wrapped with useCallback to improve performance
  const login = useCallback(
    async (email: string, password: string): Promise<{ success: boolean; message?: string }> => {
      try {
        setIsLoading(true);
        const response = await fetch('http://localhost:8080/api/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ email, password }),
        });

        const data: { token?: string; user?: User; message?: string } = await response.json();

        if (response.ok && data.token && data.user) {
          setToken(data.token);
          setUser(data.user);
          localStorage.setItem('authToken', data.token);
          localStorage.setItem('user', JSON.stringify(data.user));
          return { success: true };
        } else {
          console.error('Login failed:', data.message);
          return { success: false, message: data.message || 'Login failed' };
        }
      } catch (error) {
        console.error('Network error during login:', error);
        return { success: false, message: 'Network error. Please try again.' };
      } finally {
        setIsLoading(false);
      }
    },
    []
  );

  // Logout function wrapped with useCallback
  const logout = useCallback(() => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  }, []);

  // Memoize context value to avoid unnecessary re-renders
  const value = useMemo(
    () => ({
      user,
      token,
      login,
      logout,
      isLoading,
    }),
    [user, token, login, logout, isLoading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};