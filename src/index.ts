/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: index.ts
 * Last modified: 14/04/2023, 16:07
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import express, { Express } from "express";
import expressEjsLayouts from "express-ejs-layouts";
import cookieParser from "cookie-parser";
import expressSession from "express-session";

import dbInit from "./db/config.db";
import dbRunners from "./db/runners.db";
import router from "./routes/web-routes";

import config from "./utils/config";
import logger from "./utils/logger";
import utilities from "./utils/utilities";
import session from "./utils/session";
import { DEFAULT_LAYOUT } from "./utils/constants";

import variablesMiddleware from "./middleware/variables.middleware";
import loggedUserMiddleware from "./middleware/logged-user.middleware";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

const app: Express = express();
const port = config.PORT;

dbInit();

dbRunners.removeNotUsedOtaTokensCronSchedule();
dbRunners.migratePersonalDataToDb().then(_ => _);

app.use(expressSession(session.configure()));
app.use(expressEjsLayouts);
app.set("layout", DEFAULT_LAYOUT);

app.set("view engine", "ejs");
app.set("views", utilities.getProjectRootPath("/views"));

app.use("/assets", express.static(utilities.getProjectRootPath("public")));

app.use("/", variablesMiddleware);
app.use("/", loggedUserMiddleware);

app.use(express.urlencoded({ extended: true }));
app.use(cookieParser());

app.use(router);

app.listen(port, () => logger.info(`Server is up and running at @ http://127.0.0.1:${port}`));
