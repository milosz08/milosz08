'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */

const { merge } = require('webpack-merge');
const { SourceMapDevToolPlugin } = require('webpack');
const { commonWebpackConfig } = require('./webpack-common.config.cjs');

module.exports = merge(commonWebpackConfig(false), {
  mode: 'development',
  plugins: [
    new SourceMapDevToolPlugin({
      filename: '[file].map',
    }),
  ],
});
