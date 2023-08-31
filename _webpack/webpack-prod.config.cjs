'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: webpack-prod.config.cjs
 *   Created at: 2023-08-29, 20:18:18
 *   Last updated at: 2023-08-31, 19:47:59
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

const { merge } = require('webpack-merge');
const TerserWebpackPlugin = require('terser-webpack-plugin');
const CssMinimizerWebpackPlugin = require('css-minimizer-webpack-plugin');
const commonWebpackConfig = require('./webpack-common.config.cjs');

module.exports = merge(commonWebpackConfig(true), {
  mode: 'production',
  optimization: {
    minimize: true,
    minimizer: [
      new CssMinimizerWebpackPlugin({
        minimizerOptions: {
          preset: [
            'default',
            {
              discardComments: {
                removeAll: true,
              },
            },
          ],
        },
      }),
      new TerserWebpackPlugin({
        terserOptions: {
          mangle: true,
          compress: true,
          output: {
            comments: false,
          },
        },
      }),
    ],
  },
});
