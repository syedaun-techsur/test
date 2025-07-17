import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';

interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
}

interface LoginResponse {
  token: string;
  user: User;
  message?: string;
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (email: string, password: string) => Promise<{ success: boolean; message?: string }>;
  logout: () => void;
  isLoading: boolean;
}

const defaultAuthContext: AuthContextType = {
  user: null,
  token: null,
  login: async () => ({ success: false, message: 'Not initialized' }),
  logout: () => {},
  isLoading: false,
};

const AuthContext = createContext<AuthContextType>(defaultAuthContext);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
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

  useEffect(() => {
    // On component mount, attempt to load auth state from localStorage
    const savedToken = localStorage.getItem('authToken');
    const savedUserString = localStorage.getItem('user');

    if (savedToken && savedUserString) {
      try {
        const savedUser: User = JSON.parse(savedUserString);
        setToken(savedToken);
        setUser(savedUser);
      } catch {
        // If parsing user fails, clear saved data to prevent runtime errors
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        setToken(null);
        setUser(null);
      }
    }
    setIsLoading(false);
  }, []);

  const login = useCallback(
    async (email: string, password: string): Promise<{ success: boolean; message?: string }> => {
      setIsLoading(true);
      try {
        const response = await fetch('http://localhost:8080/api/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ email, password }),
        });

        const data: LoginResponse = await response.json();

        if (response.ok) {
          setToken(data.token);
          setUser(data.user);
          localStorage.setItem('authToken', data.token);
          localStorage.setItem('user', JSON.stringify(data.user));
          return { success: true };
        } else {
          return { success: false, message: data.message || 'Login failed' };
        }
      } catch (error) {
        // More specific catch - can enhance with error instanceof checks if needed
        return { success: false, message: 'Network error. Please try again.' };
      } finally {
        setIsLoading(false);
      }
    },
    []
  );

  const logout = useCallback(() => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  }, []);

  const value = {
    user,
    token,
    login,
    logout,
    isLoading,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};