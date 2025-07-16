
import React, { createContext, useContext, useEffect, useState } from 'react';
import { User, Session } from '@supabase/supabase-js';
import { supabase } from '@/integrations/supabase/client';

interface AuthContextType {
  user: User | null;
  session: Session | null;
  signUp: (email: string, password: string, name: string) => Promise<{ error: any }>;
  signIn: (email: string, password: string) => Promise<{ error: any }>;
  signOut: () => Promise<void>;
  loading: boolean;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [session, setSession] = useState<Session | null>(null);
  const [loading, setLoading] = useState(true);

  const isAuthenticated = !!session && !!user;

  useEffect(() => {
    console.log('AuthContext: Setting up Supabase auth listener');

    // Set up auth state listener
    const { data: { subscription } } = supabase.auth.onAuthStateChange(
      async (event, session) => {
        console.log('AuthContext: Auth state changed:', event, session?.user?.email);
        setSession(session);
        setUser(session?.user ?? null);
        setLoading(false);
      }
    );

    // Get initial session
    supabase.auth.getSession().then(({ data: { session } }) => {
      console.log('AuthContext: Initial session:', session?.user?.email);
      setSession(session);
      setUser(session?.user ?? null);
      setLoading(false);
    });

    return () => {
      console.log('AuthContext: Cleaning up auth listener');
      subscription.unsubscribe();
    };
  }, []);

  const signUp = async (email: string, password: string, name: string) => {
    console.log('AuthContext: Sign up attempt for:', email);
    
    const { error } = await supabase.auth.signUp({
      email,
      password,
      options: {
        emailRedirectTo: `${window.location.origin}/`,
        data: {
          name: name
        }
      }
    });

    if (error) {
      console.error('AuthContext: Sign up failed:', error);
    } else {
      console.log('AuthContext: Sign up successful');
    }

    return { error };
  };

  const signIn = async (email: string, password: string) => {
    console.log('AuthContext: Sign in attempt for:', email);
    
    const { error } = await supabase.auth.signInWithPassword({
      email,
      password,
    });

    if (error) {
      console.error('AuthContext: Sign in failed:', error);
    } else {
      console.log('AuthContext: Sign in successful');
    }

    return { error };
  };

  const signOut = async () => {
    console.log('AuthContext: Sign out initiated');
    
    const { error } = await supabase.auth.signOut();
    
    if (error) {
      console.error('AuthContext: Sign out failed:', error);
    } else {
      console.log('AuthContext: Sign out successful');
    }
  };

  const value = {
    user,
    session,
    signUp,
    signIn,
    signOut,
    loading,
    isAuthenticated
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

