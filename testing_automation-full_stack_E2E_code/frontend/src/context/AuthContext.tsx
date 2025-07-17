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

  const fetchUserData = useCallback(async () => {
    try {
      const response = await fetch('http://localhost:8080/api/me', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      const data = await response.json();
      if (response.ok) {
        setUser(data.user);
      } else {
        setUser(null);
        setToken(null);
        sessionStorage.removeItem('authToken');
        sessionStorage.removeItem('user');
      }
    } catch (error) {
      setUser(null);
      setToken(null);
      sessionStorage.removeItem('authToken');
      sessionStorage.removeItem('user');
    } finally {
      setIsLoading(false);
    }
  }, [token]);

  useEffect(() => {
    const savedToken = sessionStorage.getItem('authToken');
    const savedUser = sessionStorage.getItem('user');

    if (savedToken && savedUser) {
      setToken(savedToken);
      setUser(JSON.parse(savedUser));
      fetchUserData();
    } else {
      setIsLoading(false);
    }
  }, [fetchUserData]);

  const login = async (email: string, password: string): Promise<{ success: boolean; message?: string }> => {
    try {
      setIsLoading(true);
      const response = await fetch('http://localhost:8080/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (response.ok) {
        setToken(data.token);
        setUser(data.user);
        sessionStorage.setItem('authToken', data.token);
        sessionStorage.setItem('user', JSON.stringify(data.user));
        fetchUserData();
        return { success: true };
      } else {
        return { success: false, message: data.message || 'Login failed' };
      }
    } catch (error) {
      return { success: false, message: 'Network error. Please try again.' };
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    sessionStorage.removeItem('authToken');
    sessionStorage.removeItem('user');
  };

  const value = {
    user,
    token,
    login,
    logout,
    isLoading,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};