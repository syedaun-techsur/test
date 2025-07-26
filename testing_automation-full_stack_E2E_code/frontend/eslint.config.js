import js from '@eslint/js';
import globals from 'globals';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';
import tsPlugin from '@typescript-eslint/eslint-plugin';

export default {
  ignores: ['dist'],
  overrides: [
    {
      files: ['**/*.{ts,tsx}'],
      extends: ['eslint:recommended', 'plugin:@typescript-eslint/recommended'],
      languageOptions: {
        ecmaVersion: 2020,
        globals: globals.browser,
      },
      plugins: {
        'react-hooks': reactHooks,
        'react-refresh': reactRefresh,
        '@typescript-eslint': tsPlugin,
      },
      rules: {
        ...reactHooks.configs.recommended.rules,
        'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
        // You can add TS plugin rules here if needed
      },
    },
  ],
};