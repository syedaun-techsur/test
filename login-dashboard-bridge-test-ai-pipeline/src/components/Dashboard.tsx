import React, { useEffect, useState } from 'react';
import { LogOut, User, Calendar, Activity, Settings } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { useAuth } from '../context/AuthContext';
import { toast } from '@/hooks/use-toast';
import { supabase } from '@/integrations/supabase/client';

interface UserProfile {
  id: string;
  email: string;
  name: string;
  created_at: string;
}

const Dashboard = () => {
  const { user, signOut, isAuthenticated } = useAuth();
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [profileLoading, setProfileLoading] = useState(true);

  // Redirect to login if not authenticated (handled by parent component)
  if (!isAuthenticated || !user) {
    return null;
  }

  useEffect(() => {
    const fetchProfile = async () => {
      if (!user) return;
      
      try {
        // Use any type to bypass TypeScript error until types are regenerated
        const { data, error } = await (supabase as any)
          .from('profiles')
          .select('*')
          .eq('id', user.id)
          .single();

        if (error) {
          console.error('Error fetching profile:', error);
          // If profile doesn't exist, use user data as fallback
          setProfile({
            id: user.id,
            email: user.email || '',
            name: user.user_metadata?.name || user.email || '',
            created_at: user.created_at || new Date().toISOString()
          });
        } else {
          setProfile(data);
        }
      } catch (error) {
        console.error('Error fetching profile:', error);
        // Fallback to user data
        setProfile({
          id: user.id,
          email: user.email || '',
          name: user.user_metadata?.name || user.email || '',
          created_at: user.created_at || new Date().toISOString()
        });
      } finally {
        setProfileLoading(false);
      }
    };

    fetchProfile();
  }, [user]);

  const handleLogout = async () => {
    console.log('Dashboard: Logout button clicked');
    
    try {
      await signOut();
      toast({
        title: "Logged Out",
        description: "You have been successfully logged out.",
      });
      console.log('Dashboard: Logout successful');
    } catch (error) {
      console.error('Dashboard: Logout failed:', error);
      toast({
        title: "Logout Error",
        description: "There was an issue logging out. Please try again.",
        variant: "destructive",
      });
    }
  };

  const stats = [
    {
      title: 'Total Projects',
      value: '12',
      description: 'Active projects',
      icon: Activity,
      color: 'from-blue-500 to-cyan-500'
    },
    {
      title: 'Tasks Completed',
      value: '48',
      description: 'This month',
      icon: Calendar,
      color: 'from-green-500 to-emerald-500'
    },
    {
      title: 'Team Members',
      value: '6',
      description: 'Collaborators',
      icon: User,
      color: 'from-purple-500 to-pink-500'
    },
    {
      title: 'Notifications',
      value: '3',
      description: 'Unread messages',
      icon: Bell,
      color: 'from-orange-500 to-red-500'
    }
  ];

  if (profileLoading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-white"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-purple-900 to-slate-900" data-testid="dashboard">
      {/* Header */}
      <header className="bg-white/10 backdrop-blur-lg border-b border-white/20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center space-x-4">
              <div className="h-10 w-10 rounded-full bg-gradient-to-r from-purple-400 to-pink-400 flex items-center justify-center">
                <User className="h-6 w-6 text-white" />
              </div>
              <div>
                <h1 className="text-xl font-bold text-white" data-testid="dashboard-title">Dashboard</h1>
                <p className="text-gray-300 text-sm" data-testid="welcome-message">
                  Welcome back, {profile?.name || user.email}!
                </p>
              </div>
            </div>
            
            <div className="flex items-center space-x-4">
              <Button
                variant="ghost"
                size="sm"
                className="text-white hover:bg-white/10"
                data-testid="settings-button"
              >
                <Settings className="h-4 w-4 mr-2" />
                Settings
              </Button>
              <Button
                onClick={handleLogout}
                variant="ghost"
                size="sm"
                className="text-white hover:bg-white/10 hover:text-red-300"
                data-testid="logout-button"
              >
                <LogOut className="h-4 w-4 mr-2" />
                Logout
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* User Info Card */}
        <Card className="mb-8 bg-white/10 backdrop-blur-lg border-white/20 text-white" data-testid="user-profile-card">
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <User className="h-5 w-5" />
              <span>User Profile</span>
            </CardTitle>
            <CardDescription className="text-gray-300">
              Your account information and preferences
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <div className="flex items-center space-x-2">
                  <Mail className="h-4 w-4 text-purple-400" />
                  <span className="text-sm text-gray-300">Email:</span>
                  <span className="text-sm font-medium" data-testid="user-email">{profile?.email || user.email}</span>
                </div>
                <div className="flex items-center space-x-2">
                  <User className="h-4 w-4 text-purple-400" />
                  <span className="text-sm text-gray-300">Name:</span>
                  <span className="text-sm font-medium" data-testid="user-name">{profile?.name || 'N/A'}</span>
                </div>
              </div>
              <div className="space-y-2">
                <div className="flex items-center space-x-2">
                  <Calendar className="h-4 w-4 text-purple-400" />
                  <span className="text-sm text-gray-300">Member Since:</span>
                  <span className="text-sm font-medium" data-testid="user-created-at">
                    {profile?.created_at ? new Date(profile.created_at).toLocaleDateString() : 'N/A'}
                  </span>
                </div>
                <div className="flex items-center space-x-2">
                  <Activity className="h-4 w-4 text-purple-400" />
                  <span className="text-sm text-gray-300">Status:</span>
                  <span className="text-sm font-medium text-green-400" data-testid="user-status">Active</span>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8" data-testid="stats-grid">
          {stats.map((stat, index) => (
            <Card key={index} className="bg-white/10 backdrop-blur-lg border-white/20 text-white hover:bg-white/15 transition-all duration-200 transform hover:scale-105">
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium text-gray-300">
                  {stat.title}
                </CardTitle>
                <div className={`h-8 w-8 rounded-full bg-gradient-to-r ${stat.color} flex items-center justify-center`}>
                  <stat.icon className="h-4 w-4 text-white" />
                </div>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{stat.value}</div>
                <p className="text-xs text-gray-400">{stat.description}</p>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* Recent Activity */}
        <Card className="bg-white/10 backdrop-blur-lg border-white/20 text-white" data-testid="recent-activity">
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <Activity className="h-5 w-5" />
              <span>Recent Activity</span>
            </CardTitle>
            <CardDescription className="text-gray-300">
              Your latest actions and updates
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {[
                { action: 'Completed project review', time: '2 hours ago', icon: Calendar },
                { action: 'Updated user profile', time: '1 day ago', icon: User },
                { action: 'Sent team notification', time: '2 days ago', icon: Bell },
                { action: 'Created new project', time: '3 days ago', icon: Activity }
              ].map((activity, index) => (
                <div key={index} className="flex items-center space-x-3 p-3 rounded-lg bg-white/5 hover:bg-white/10 transition-colors">
                  <div className="h-8 w-8 rounded-full bg-gradient-to-r from-purple-400 to-pink-400 flex items-center justify-center">
                    <activity.icon className="h-4 w-4 text-white" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm font-medium">{activity.action}</p>
                    <p className="text-xs text-gray-400">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </main>
    </div>
  );
};

export default Dashboard;