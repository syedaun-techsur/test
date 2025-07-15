import js from '@eslint/js'
import globals from 'globals'

export default {
  ignores: ['dist'],
  overrides: [
    {
      files: ['**/*.{ts,tsx}'],
      parser: '@typescript-eslint/parser',
      extends: [
        'eslint:recommended',
        'plugin:@typescript-eslint/recommended',
        'plugin:react-hooks/recommended',
        // react-refresh plugin config is generally used for Vite HMR, ESLint plugin usage is not typical,
        // so it is better to omit from ESLint extends to avoid config errors or keep as plugin usage instead of extends
      ],
      parserOptions: {
        ecmaVersion: 2020,
        sourceType: 'module',
        ecmaFeatures: {
          jsx: true,
        },
      },
      globals: globals.browser,
      rules: {},
    },
  ],
}