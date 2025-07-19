import js from '@eslint/js';
import globals from 'globals';

export default {
  ignorePatterns: ['dist'],
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended',
  ],
  files: ['**/*.{ts,tsx}'],
  env: {
    browser: true,
  },
  globals: globals.browser,
  parser: '@typescript-eslint/parser',
  plugins: ['@typescript-eslint', 'react-hooks', 'react-refresh'],
  rules: {
    'react-hooks/rules-of-hooks': 'error',
    'react-hooks/exhaustive-deps': 'warn',
    'react-refresh/only-export-components': [
      'warn',
      { allowConstantExport: true },
    ],
  },
};