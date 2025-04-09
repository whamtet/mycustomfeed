const colors = require('tailwindcss/colors')

module.exports = {
  content: ["resources/public-api/*.html"],
  theme: {
    extend: {},
    colors: {
      ...colors,
      'clj-green-light': '#A2DA5F',
      'clj-blue-light': '#97B3F8',
      'clj-blue': '#6180D2'
    }
  },
  plugins: [],
}
