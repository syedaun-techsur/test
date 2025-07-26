import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  useCallback,
  useMemo,
} from 'react';

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

const AUTH_TOKEN_KEY = 'authToken';
const USER_KEY = 'user';

type LoginResponse = {
  token: string;
  user: User;
  message?: string;
};

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    let isMounted = true;

    try {
      const savedToken = localStorage.getItem(AUTH_TOKEN_KEY);
      const savedUser = localStorage.getItem(USER_KEY);

      if (savedToken && savedUser && isMounted) {
        setToken(savedToken);
        setUser(JSON.parse(savedUser));
      }
    } catch {
      // If parsing fails, clear corrupted data
      localStorage.removeItem(AUTH_TOKEN_KEY);
      localStorage.removeItem(USER_KEY);
    } finally {
      if (isMounted) setIsLoading(false);
    }

    return () => {
      isMounted = false;
    };
  }, []);

  const login = useCallback(
    async (email: string, password: string): Promise<{ success: boolean; message?: string }> => {
      setIsLoading(true);

      try {
        const response = await fetch(
          `${process.env.REACT_APP_API_URL ?? 'http://localhost:8080'}/api/login`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
          }
        );

        const data: LoginResponse & { message?: string } = await response.json();

        if (response.ok) {
          setToken(data.token);
          setUser(data.user);
          localStorage.setItem(AUTH_TOKEN_KEY, data.token);
          localStorage.setItem(USER_KEY, JSON.stringify(data.user));
          return { success: true };
        }

        return { success: false, message: data.message || 'Login failed' };
      } catch {
        return { success: false, message: 'Network error. Please try again.' };
      } finally {
        setIsLoading(false);
      }
    },
    []
  );

  const logout = useCallback((): void => {
    setUser(null);
    setToken(null);
    localStorage.removeItem(AUTH_TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }, []);

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