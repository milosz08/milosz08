'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
