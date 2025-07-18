import React, { useState, ChangeEvent, FormEvent } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { Eye, EyeOff, Mail, Lock, LogIn } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

interface FormErrors {
  email?: string;
  password?: string;
  submit?: string;
}

const LoginForm: React.FC = React.memo(() => {
  const { user, login, isLoading } = useAuth();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [errors, setErrors] = useState<FormErrors>({});
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  if (user) {
    return <Navigate to="/dashboard" replace />;
  }

  const validateEmail = (email: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validateForm = (): FormErrors => {
    const newErrors: FormErrors = {};

    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!validateEmail(formData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }

    if (!formData.password.trim()) {
      newErrors.password = 'Password is required';
    } else if (formData.password.length < 6) {
      newErrors.password = 'Password must be at least 6 characters';
    }

    return newErrors;
  };

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>): void => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));

    setErrors(prevErrors => {
      const newErrors = { ...prevErrors };
      if (name === 'email') {
        if (validateEmail(value)) {
          delete newErrors.email;
        }
      }
      if (name === 'password') {
        if (value.length >= 6) {
          delete newErrors.password;
        }
      }
      return newErrors;
    });
  };

  const handleSubmit = async (e: FormEvent): Promise<void> => {
    e.preventDefault();

    const validationErrors = validateForm();
    setErrors(validationErrors);
    if (Object.keys(validationErrors).length > 0) {
      return;
    }

    setIsSubmitting(true);
    setErrors({});

    const result = await login(formData.email, formData.password);

    if (result.success) {
      navigate('/dashboard', { replace: true });
    } else {
      setErrors({ submit: result.message });
    }

    setIsSubmitting(false);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md transform transition-all duration-300 hover:shadow-2xl">
        <div className="text-center mb-8">
          <div className="bg-blue-600 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
            <LogIn className="w-8 h-8 text-white" />
          </div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Welcome Back</h1>
          <p className="text-gray-600">Login to your account</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6" data-testid="login-form" noValidate>
          {errors.submit && (
            <div
              className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm"
              data-testid="error-message"
              role="alert"
              aria-live="assertive"
            >
              {errors.submit}
            </div>
          )}

          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
              Email Address
            </label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Mail className="h-5 w-5 text-gray-400" />
              </div>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                className={`block w-full pl-10 pr-3 py-3 border ${
                  errors.email ? 'border-red-300' : 'border-gray-300'
                } rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors`}
                placeholder="Enter your email"
                data-testid="email-input"
                autoComplete="email"
                required
                aria-invalid={errors.email ? 'true' : 'false'}
                aria-describedby={errors.email ? 'email-error' : undefined}
              />
            </div>
            {errors.email && (
              <p className="text-red-600 text-sm mt-1" data-testid="email-error" id="email-error" role="alert">
                {errors.email}
              </p>
            )}
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
              Password
            </label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Lock className="h-5 w-5 text-gray-400" />
              </div>
              <input
                type={showPassword ? 'text' : 'password'}
                id="password"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                className={`block w-full pl-10 pr-10 py-3 border ${
                  errors.password ? 'border-red-300' : 'border-gray-300'
                } rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors`}
                placeholder="Enter your password"
                data-testid="password-input"
                autoComplete="current-password"
                required
                aria-invalid={errors.password ? 'true' : 'false'}
                aria-describedby={errors.password ? 'password-error' : undefined}
              />
              <button
                type="button"
                className="absolute inset-y-0 right-0 pr-3 flex items-center"
                onClick={() => setShowPassword(prev => !prev)}
                aria-label={showPassword ? 'Hide password' : 'Show password'}
                data-testid="toggle-password"
              >
                {showPassword ? (
                  <EyeOff className="h-5 w-5 text-gray-400 hover:text-gray-600" />
                ) : (
                  <Eye className="h-5 w-5 text-gray-400 hover:text-gray-600" />
                )}
              </button>
            </div>
            {errors.password && (
              <p className="text-red-600 text-sm mt-1" data-testid="password-error" id="password-error" role="alert">
                {errors.password}
              </p>
            )}
          </div>

          <button
            type="submit"
            disabled={isSubmitting || isLoading}
            className="w-full bg-blue-600 text-white py-3 px-4 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors font-medium"
            data-testid="login-button"
          >
            {isSubmitting ? (
              <div className="flex items-center justify-center">
                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2" />
                Logging in...
              </div>
            ) : (
              'Login'
            )}
          </button>
        </form>

        <div className="mt-6 text-center">
          <p className="text-sm text-gray-600">Demo credentials: admin@example.com / password123</p>
        </div>
      </div>
    </div>
  );
});

export default LoginForm;