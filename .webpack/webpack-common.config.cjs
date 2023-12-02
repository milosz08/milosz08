'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */

const path = require('path');
const dotenv = require('dotenv');
const { DefinePlugin } = require('webpack');
const nodeExternals = require('webpack-node-externals');
const NodemonPlugin = require('nodemon-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const RemoveEmptyScriptsPlugin = require('webpack-remove-empty-scripts');

dotenv.config();

const loadableAssetModules = [
  { name: 'bootstrap-addons', ext: 'js' },
  { name: 'common-public', ext: 'js' },
  { name: 'copy-clone-link', ext: 'js' },
  { name: 'images-preview', ext: 'js' },
  { name: 'tech-stacks-inserter', ext: 'js' },
  { name: 'toggle-password', ext: 'js' },
  { name: 'common', ext: 'css' },
  { name: 'main', ext: 'css' },
  { name: 'variables', ext: 'css' },
];

module.exports = isProd => ({
  entry: {
    index: path.resolve(__dirname, '..', 'src', 'index.ts'),
    ...loadableAssetModules.reduce(
      (acc, module) => ({
        ...acc,
        [module.name]: path.resolve(
          __dirname,
          '..',
          'src',
          'public',
          module.ext,
          `${module.name}.${module.ext}`
        ),
      }),
      {}
    ),
  },
  target: 'node',
  externals: [nodeExternals()],
  output: {
    path: path.resolve(__dirname, '..', 'dist'),
    filename: pathData => {
      return pathData.chunk.name === 'index'
        ? '[name].js'
        : `public/js/[name].js`;
    },
    clean: isProd,
    publicPath: '/',
  },
  resolve: {
    extensions: ['.js', '.ts'],
    modules: ['node_modules'],
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /\/node_modules\//,
        use: [
          {
            loader: 'babel-loader',
            options: {
              presets: ['@babel/preset-env'],
            },
          },
        ],
      },
      {
        test: /\.ts$/,
        exclude: /\/node_modules\//,
        use: [{ loader: 'ts-loader' }],
      },
      {
        test: /\.css$/,
        use: [
          { loader: MiniCssExtractPlugin.loader },
          { loader: 'css-loader' },
        ],
      },
    ],
  },
  plugins: [
    new RemoveEmptyScriptsPlugin(),
    new NodemonPlugin(),
    new DefinePlugin({
      __EXPRESS_PORT__: JSON.stringify(
        process.env[isProd ? 'PROD_EXPRESS_PORT' : 'DEV_EXPRESS_PORT']
      ),
    }),
    new CleanWebpackPlugin({
      cleanOnceBeforeBuildPatterns: ['!public/uploads/**'],
      cleanAfterEveryBuildPatterns: ['!public/uploads/**'],
    }),
    new MiniCssExtractPlugin({
      filename: `public/styles/[name].css`,
    }),
    new CopyWebpackPlugin({
      patterns: [
        {
          from: path.resolve(__dirname, '..', 'src', 'public'),
          to: path.resolve(__dirname, '..', 'dist', 'public'),
          globOptions: {
            ignore: ['**/js/*.js'],
            ignore: ['**/styles/*.css'],
          },
        },
        {
          from: path.resolve(__dirname, '..', 'src', 'views'),
          to: path.resolve(__dirname, '..', 'dist', 'views'),
        },
      ],
    }),
  ],
});
