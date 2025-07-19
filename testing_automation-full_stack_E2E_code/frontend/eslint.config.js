import { Linter } from 'eslint';
import { parser as tsParser } from '@typescript-eslint/parser';
import { eslintRecommended } from '@eslint/js';
import { reactHooks } from 'eslint-plugin-react-hooks';
import { reactRefresh } from 'eslint-plugin-react-refresh';
import { typescriptEslintPlugin } from '@typescript-eslint/eslint-plugin';
import globals from 'globals';

const config: Linter.FlatConfig = {
  ignores: ['dist'],
  files: ['**/*.{ts,tsx}'],
  languageOptions: {
    ecmaVersion: 2020,
    sourceType: 'module',
    parser: tsParser,
    globals: globals.browser,
  },
  plugins: {
    '@typescript-eslint': typescriptEslintPlugin,
    'react-hooks': reactHooks,
    'react-refresh': reactRefresh,
  },
  rules: {
    ...reactHooks.configs.recommended.rules,
    'react-refresh/only-export-components': [
      'warn',
      { allowConstantExport: true },
    ],
  },
};

export default [
  eslintRecommended,
  {
    ...config,
    languageOptions: {
      ...config.languageOptions,
      parserOptions: {
        projectService: true,
        tsconfigRootDir: import.meta.dirname,
      },
    },
  },
];