import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import tsPlugin from '@typescript-eslint/eslint-plugin'
import tsConfig from '@typescript-eslint/eslint-config'
import { flatCompat } from '@eslint/eslintrc'

// Convert older config style to flat config compat
const compat = flatCompat({ flatConfig: true })

export default [
  {
    ignores: ['dist'],
  },
  {
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      parserOptions: {
        ecmaVersion: 2024,
        sourceType: 'module',
        ecmaFeatures: {
          jsx: true,
        },
        project: './tsconfig.json',
      },
      globals: globals.browser,
      env: {
        browser: true,
      },
    },
    plugins: {
      '@typescript-eslint': tsPlugin,
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
    },
    rules: {
      ...compat(tsConfig.configs.recommended.rules),
      ...compat(reactHooks.configs['recommended'].rules),
      ...compat(reactRefresh.configs.vite.rules),
    },
    extends: [
      ...compat(js.configs.recommended),
      ...compat(tsConfig.configs.recommended),
      ...compat(reactHooks.configs['recommended']),
      ...compat(reactRefresh.configs.vite),
    ],
  },
]