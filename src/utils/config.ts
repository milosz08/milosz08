/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: config.ts
 * Last modified: 19/04/2023, 14:17
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import * as process from "process";
import * as dotenv from "dotenv";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

dotenv.config();

export default {
    PORT:                   process.env.EXPRESS_PORT            || 80,
    NODE_ENV:               process.env.NODE_ENV                || "development",
    SESSION_KEY:            process.env.SESSION_KEY             || "vtg290d203dkd0kd09kdgghhmnkj57jh4hg3fgv5h65h6h",
    CONNECTION_STRING:      process.env.CONNECTION_STRING       || "mongodb://127.0.0.1:27017/db",
    DEF_USER_LOGIN:         process.env.DEF_USER_LOGIN          || "admin",
    DEF_USER_EMAIL:         process.env.DEF_USER_EMAIL          || "admin@example.com",
    DEF_USER_PASSWORD:      process.env.DEF_USER_PASSWORD       || "Admin123@"
};
