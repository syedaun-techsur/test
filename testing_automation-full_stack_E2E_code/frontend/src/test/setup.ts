import '@testing-library/jest-dom';

/**
 * Global test setup file for frontend testing.
 * Configures testing-library and jest globals for better test isolation
 * and cleanup after each test.
 */

afterEach(() => {
  // Cleanup DOM elements mounted with @testing-library/react to prevent leaks
  // This is usually handled automatically, but explicit cleanup here is safe.
  // If using React 18+, automatic cleanup is default.
  // This provides backward compatibility and clarity.
  jest.clearAllMocks();
});