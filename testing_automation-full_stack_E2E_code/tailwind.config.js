/** @type {import('tailwindcss').Config} */
module.exports = {
  // Paths to all template files in the project
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  
  // Enable dark mode via class strategy (optional but recommended)
  darkMode: 'class',

  theme: {
    extend: {},
  },

  plugins: [],
};