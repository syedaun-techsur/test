import js from '@eslint/js';
import globals from 'globals';

export default {
  ignores: ['dist'],
  files: ['**/*.{ts,tsx}'],
  languageOptions: {
    parser: '@typescript-eslint/parser',
    parserOptions: {
      ecmaVersion: 2020,
      sourceType: 'module',
      ecmaFeatures: {
        jsx: true,
      },
    },
    globals: globals.browser,
  },
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/recommended',
  ],
  plugins: ['react-hooks', 'react-refresh'],
  rules: {
    // React hooks recommended rules:
    'react-hooks/rules-of-hooks': 'error',
    'react-hooks/exhaustive-deps': 'warn',

    'react-refresh/only-export-components': [
      'warn',
      { allowConstantExport: true },
    ],
  },
};