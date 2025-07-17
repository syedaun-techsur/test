import js from '@eslint/js';
import globals from 'globals';

export default {
  ignorePatterns: ['dist'],
  extends: [
    js.configs.recommended,
    'plugin:@typescript-eslint/recommended',
  ],
  files: ['**/*.{ts,tsx}'],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaVersion: 2020,
    sourceType: 'module',
    ecmaFeatures: {
      jsx: true,
    },
    // If your project has a tsconfig.json, uncomment and set the path below
    // project: './tsconfig.json',
  },
  env: {
    browser: true,
    es6: true,
  },
  globals: globals.browser,
  plugins: ['react-hooks', 'react-refresh', '@typescript-eslint'],
  rules: {
    // React hooks recommended rules
    'react-hooks/rules-of-hooks': 'error',
    'react-hooks/exhaustive-deps': 'warn',
    // React refresh rules
    'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
  },
};