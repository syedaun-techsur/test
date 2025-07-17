import js from '@eslint/js';
import globals from 'globals';
import react from 'eslint-plugin-react';
import { config, configs } from 'typescript-eslint';

export default config(
  { ignores: ['dist'] },
  {
    extends: [js.configs.recommended, ...configs.recommended],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
    },
    plugins: {
      react,
    },
    rules: {
      ...react.configs.recommended.rules,
      'react/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
    },
  }
);