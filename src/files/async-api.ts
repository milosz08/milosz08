/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
