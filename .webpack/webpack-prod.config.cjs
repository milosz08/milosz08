'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
