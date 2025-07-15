import { FlatCompat, ESLintConfig } from '@eslint/eslintrc'
import globals from 'globals'

// Plugins are referenced as strings in `plugins` and their recommended configs in `extends`
const globalIgnore = {
  ignores: ['dist']
}

const config = {
  ignores: globalIgnore.ignores,
  overrides: [
    {
      files: ['**/*.{ts,tsx}'],
      parser: '@typescript-eslint/parser',
      parserOptions: {
        ecmaVersion: 2021,
        sourceType: 'module',
        ecmaFeatures: {
          jsx: true
        }
      },
      env: {
        browser: true,
        es2021: true
      },
      plugins: [
        'react-hooks',
        'react-refresh',
        '@typescript-eslint'
      ],
      extends: [
        'eslint:recommended',
        'plugin:@typescript-eslint/recommended',
        'plugin:react-hooks/recommended',
        'plugin:react-refresh/recommended'
      ],
      globals: globals.browser
    }
  ]
}

export default config