/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: multer-config.ts
 * Last modified: 14/05/2023, 13:57
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import multer from "multer";
import utilities from "../utils/utilities";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, utilities.getProjectRootPath("../public/uploads/temp/"));
    },
    filename: (req, file, cb) => {
        const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
        cb(null, uniqueSuffix);
    },
});

export const upload = multer({ storage: storage });
