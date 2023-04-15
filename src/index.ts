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
import dotenv from "dotenv";

import utilities from "./utils/utilities";
import router from "./routes/web-routes";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

dotenv.config();

const app: Express = express();
const port = process.env.EXPRESS_PORT || 4041;

app.use(expressEjsLayouts);
app.set("layout", "layouts/header-footer-layout");

app.set("view engine", "ejs");
app.set("views", utilities.getProjectRootPath("/views"));

app.use('/assets', express.static(utilities.getProjectRootPath("public")));
app.use(router);

app.listen(port, () => {
    console.log(`[server]: Server is running at http://127.0.0.1:${port}`);
});
