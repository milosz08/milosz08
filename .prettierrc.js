'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: .prettierrc.cjs
 *   Created at: 2023-08-29, 19:54:48
 *   Last updated at: 2023-08-29, 20:16:18
 *   Project name: <<msph_projectName>>
 *
 *  LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

module.exports = {
  tabWidth: 2,
  printWidth: 80,
  useTabs: false,
  singleQuote: true,
  semi: true,
  bracketSpacing: true,
  arrowParens: 'avoid',
  trailingComma: 'es5',
  bracketSameLine: true,
  importOrderParserPlugins: ['typescript', 'decorators-legacy'],
  plugins: ['@trivago/prettier-plugin-sort-imports'],
  importOrder: ['^(@.*)$', '^([a-zA-Z].*)$', '^[./]', '^[../]'],
  importOrderSeparation: false,
  importOrderSortSpecifiers: true,
};
