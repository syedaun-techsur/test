import js from '@eslint/js';
import globals from 'globals';
import reactHooks from 'eslint-plugin-react-hooks';
import { createRequire } from 'module';
import { globalIgnores } from 'eslint/config';

const require = createRequire(import.meta.url);
const tsEslint = require('@typescript-eslint/eslint-plugin');

export default [
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      js.configs.recommended,
      tsEslint.configs.recommended,
      reactHooks.configs['recommended-latest'],
    ],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
    },
  },
];