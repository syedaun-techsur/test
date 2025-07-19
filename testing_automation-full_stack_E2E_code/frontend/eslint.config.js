import js from '@eslint/js';
import globals from 'globals';

export default {
  ignorePatterns: ['dist'],
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended',
  ],
  files: ['**/*.{ts,tsx}'],
  parserOptions: {
    ecmaVersion: 2020,
  },
  env: {
    browser: true,
  },
  globals: globals.browser,
  plugins: ['react-hooks', 'react-refresh', '@typescript-eslint'],
  rules: {
    'react-hooks/rules-of-hooks': 'error',
    'react-hooks/exhaustive-deps': 'warn',
    'react-refresh/only-export-components': [
      'warn',
      { allowConstantExport: true },
    ],
  },
};