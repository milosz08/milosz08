/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: async-api.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:49:14
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import fs from 'fs';
import util from 'util';

export default {
  mkdirAsync: util.promisify(fs.mkdir),
  existAsync: util.promisify(fs.exists),
  renameAsync: util.promisify(fs.rename),
  unlinkAsync: util.promisify(fs.unlink),
  rmdirAsync: util.promisify(fs.rmdir),
  readdirAsync: util.promisify(fs.readdir),
};
