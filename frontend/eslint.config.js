import js from '@eslint/js'
import globals from 'globals'
import { flatConfigGlobalIgnore } from 'eslint/config'

export default [
  flatConfigGlobalIgnore(['dist']),
  {
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
    plugins: {
      'react-hooks': 'eslint-plugin-react-hooks',
      'react-refresh': 'eslint-plugin-react-refresh',
      '@typescript-eslint': '@typescript-eslint/eslint-plugin',
    },
    extends: [
      js.configs.recommended,
      'plugin:@typescript-eslint/recommended',
      'plugin:react-hooks/recommended',
      'plugin:react-refresh/recommended',
    ],
    rules: {
      // Add any custom rule overrides here if needed
    },
  },
]