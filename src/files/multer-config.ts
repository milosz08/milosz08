/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import multer from 'multer';
import utilities from '../utils/utilities';

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, utilities.getProjectRootPath('public/uploads/temp/'));
  },
  filename: (req, file, cb) => {
    const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1e9);
    cb(null, uniqueSuffix);
  },
});

export const upload = multer({ storage: storage });
