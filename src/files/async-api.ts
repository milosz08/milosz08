/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: async-api.ts
 * Last modified: 14/05/2023, 15:10
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import util from "util";
import fs from "fs";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export default {
    mkdirAsync: util.promisify(fs.mkdir),
    existAsync: util.promisify(fs.exists),
    renameAsync: util.promisify(fs.rename),
    unlinkAsync: util.promisify(fs.unlink),
    rmdirAsync: util.promisify(fs.rmdir),
    readdirAsync: util.promisify(fs.readdir),
};
