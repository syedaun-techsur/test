/** @type {import('tailwindcss').Config} */

module.exports = {
  // Explicitly set the content paths to scan for class names
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],

  // Define theme customization and extensions
  theme: {
    extend: {},
  },

  // Add any Tailwind plugins here
  plugins: [],

  // Explicitly define dark mode strategy for clarity (optional)
  darkMode: 'media', // or 'class' based on project preference
};