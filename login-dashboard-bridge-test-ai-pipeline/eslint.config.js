import js from "@eslint/js";
import globals from "globals";

export default {
  ignorePatterns: ["dist"],
  extends: [js.configs.recommended, "plugin:@typescript-eslint/recommended"],
  files: ["**/*.{ts,tsx}"],
  languageOptions: {
    ecmaVersion: 2020,
    globals: globals.browser,
  },
  plugins: ["react-hooks", "@typescript-eslint"],
  rules: {
    "react-hooks/rules-of-hooks": "error",
    "react-hooks/exhaustive-deps": "warn",
    "@typescript-eslint/no-unused-vars": "off",
  },
};