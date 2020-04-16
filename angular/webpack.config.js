const purgecss = require('@fullhuman/postcss-purgecss')

module.exports = {
  module: {
    rules: [
      {
        test: /\.css$/,
        loader: 'postcss-loader',
        options: {
          ident: 'postcss',
          syntax: 'postcss-scss',
          plugins: () => [
            require('postcss-import'),
            require('autoprefixer'),
            purgecss({
              content: ['./**/*.html']
            })
          ]
        }
      }
    ]
  }
};
