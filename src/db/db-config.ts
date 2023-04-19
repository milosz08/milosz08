/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: db-config.ts
 * Last modified: 19/04/2023, 15:04
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import mongoose from "mongoose";

import config from "../utils/config";
import logger from "../utils/logger";
import dbRunners from "./db-runners";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export default function (): void {
    mongoose.connect(config.CONNECTION_STRING).then(_ => {
        logger.info("Successfully connected into the database");
    });
    dbRunners.createDefaultUser().then(_ => _);
};
