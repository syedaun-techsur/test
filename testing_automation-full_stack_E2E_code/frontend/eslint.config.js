import js from '@eslint/js';
import globals from 'globals';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';
import { Linter } from 'eslint';
import { ParserOptions } from '@typescript-eslint/parser';

const tsParserOptions: ParserOptions = {
  project: './tsconfig.json',
};

const tsConfig: Linter.FlatConfig = {
  files: ['**/*.{ts,tsx}'],
  languageOptions: {
    parser: '@typescript-eslint/parser',
    parserOptions: tsParserOptions,
    ecmaVersion: 2020,
    globals: globals.browser,
  },
  plugins: {
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
  js.configs.recommended,
  tsConfig,
];