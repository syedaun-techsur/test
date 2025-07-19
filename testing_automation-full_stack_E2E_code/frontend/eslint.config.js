import js from '@eslint/js';
import { config as typescriptEslintConfig, configs as typescriptEslintConfigs } from 'typescript-eslint';
import { onlyExportComponents } from 'eslint-plugin-react-refresh';

export default typescriptEslintConfig(
  { ignores: ['dist'] },
  {
    extends: [js.configs.recommended, ...typescriptEslintConfigs.recommended],
    files: ['**/*.{ts,tsx}'],
    languageOptions: {
      ecmaVersion: 2020,
      globals: { ...globals.browser, ...globals.jest },
    },
    plugins: {
      'react-refresh': onlyExportComponents,
    },
    rules: {
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],
    },
  }
);