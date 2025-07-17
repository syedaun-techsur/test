import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';

interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (email: string, password: string) => Promise<{ success: boolean; message?: string }>;
  logout: () => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: React.ReactNode;
}

const API_BASE_URL = 'http://localhost:8080/api';

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check for existing token and user on app startup
    const savedToken = localStorage.getItem('authToken');
    const savedUserString = localStorage.getItem('user');

    if (!savedToken || !savedUserString) {
      setIsLoading(false);
      return;
    }

    try {
      const savedUser: User = JSON.parse(savedUserString);
      setToken(savedToken);
      setUser(savedUser);
    } catch {
      // If stored user data is invalid, clear storage to avoid persistent errors
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      setToken(null);
      setUser(null);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const login = useCallback(
    async (email: string, password: string): Promise<{ success: boolean; message?: string }> => {
      setIsLoading(true);
      try {
        const response = await fetch(`${API_BASE_URL}/login`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ email, password }),
        });

        // Using unknown type to safely handle response data
        const data: unknown = await response.json();

        if (response.ok) {
          // Basic runtime validation of expected properties
          if (
            typeof data === 'object' &&
            data !== null &&
            'token' in data &&
            'user' in data &&
            typeof (data as any).token === 'string' &&
            typeof (data as any).user === 'object'
          ) {
            const { token, user } = data as { token: string; user: User };
            setToken(token);
            setUser(user);
            localStorage.setItem('authToken', token);
            localStorage.setItem('user', JSON.stringify(user));
            return { success: true };
          }
          return { success: false, message: 'Invalid response from server' };
        } else {
          // Attempt safe extraction of error message
          const message =
            typeof data === 'object' && data !== null && 'message' in data && typeof (data as any).message === 'string'
              ? (data as any).message
              : 'Login failed';
          return { success: false, message };
        }
      } catch {
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

  const value: AuthContextType = {
    user,
    token,
    login,
    logout,
    isLoading,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};