/**
 * Created by emamoshin on 01.02.2018.
 */
const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const pkg = require('../package.json');

const extractLess = new ExtractTextPlugin({
  filename: 'n2o.css',
  disable: process.env.NODE_ENV === 'development',
});

module.exports = {
  context: path.resolve(__dirname, '../src'),

  entry: ['./sass/n2o.scss'],

  output: {
    path: path.resolve(__dirname, '../dist'),
    filename: 'n2o.js',
    library: 'N2O',
    libraryTarget: 'umd',
  },

  stats: {
    colors: true,
    reasons: false,
    hash: false,
    version: false,
    timings: true,
    chunks: false,
    chunkModules: false,
    cached: false,
    cachedAssets: false,
  },

  resolve: {
    modules: [path.resolve(__dirname, '../src'), 'node_modules'],
    extensions: ['.js', '.jsx'],
  },

  module: {
    rules: [
      {
        test: /\.scss/,
        use: extractLess.extract({
          use: [
            {
              loader: 'css-loader',
            },
            {
              loader: 'sass-loader',
            },
          ],
          // use style-loader in development
          fallback: 'style-loader',
        }),
      },
      {
        test: /.(ttf|otf|eot|svg|woff(2)?)(\?[a-z0-9]+)?$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[name].[ext]',
              outputPath: 'fonts/', // where the fonts will go
              publicPath: './fonts/', // override the default path
            },
          },
        ],
      },
    ],
  },

  externals: {
    react: {
      root: 'React',
      commonjs: 'react',
      commonjs2: 'react',
      amd: 'react',
    },
    'react-dom': {
      root: 'ReactDOM',
      commonjs: 'react-dom',
      commonjs2: 'react-dom',
      amd: 'react-dom',
    },
  },

  plugins: [extractLess],
};
