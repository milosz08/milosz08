/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: web-routes.ts
 * Last modified: 15/04/2023, 18:20
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { Router } from "express";

import homeController from "../controllers/home.controller";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

const router: Router = Router();

router.get("/",                 homeController.homePage);
router.get("/project/:name",    homeController.projectDetailsPage);

router.get("*",                 (req, res) => res.redirect("/"));

export default router;
