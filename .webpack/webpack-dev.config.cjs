'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: webpack-dev.config.cjs
 *   Created at: 2023-08-29, 20:18:25
 *   Last updated at: 2023-08-31, 19:47:55
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
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
