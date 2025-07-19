/** @type {import('tailwindcss').Config} */
module.exports = {
  // Specify files to scan for class names
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {}, // Extend default theme here
  },
  plugins: [], // Add Tailwind plugins here if needed
};