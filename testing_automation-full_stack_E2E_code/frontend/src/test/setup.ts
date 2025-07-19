import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';

// Automatically cleanup after each test to prevent memory leaks and test interference
afterEach(() => {
  cleanup();
});

// Placeholder for additional global setup, such as mocking or extending Jest matchers
// Example:
// expect.extend(customMatchers);