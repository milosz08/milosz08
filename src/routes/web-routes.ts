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

import cmsController from "../controllers/cms.controller";
import authController from "../controllers/auth.controller";
import homeController from "../controllers/home.controller";
import accountController from "../controllers/account.controller";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

const router: Router = Router();

router.get("/",                                     homeController.homePage);
router.get("/project/:name",                        homeController.projectDetailsPage);

router.get("/login",                                authController.loginPage);
router.get("/logout",                               authController.logoutPage);
router.get("/request-change-password",              authController.requestChangePasswordPage);
router.get("/change-password",                      authController.changePasswordPage);
router.get("/first-login",                          authController.firstLoginPage);

router.get("/cms/accounts",                         accountController.accountsPage);
router.get("/cms/create-account",                   accountController.createAccountPage);
router.get("/cms/update-account/:accountId",        accountController.updateAccountPage);
router.get("/cms/delete-account/:accountId",        accountController.deleteAccountRedirect);

router.get("/cms/personals",                        cmsController.personalsDataPage);
router.get("/cms/projects",                         cmsController.projectsPage);
router.get("/cms/projects/add",                     cmsController.addProjectPage);
router.get("/cms/projects/update/:projectId",       cmsController.updateProjectPage);
router.get("/cms/projects/delete/:projectId",       cmsController.deleteProjectRedirect);

router.get("/cms",                                  (req, res) => res.redirect("/cms/personals"));
router.get("*",                                     (req, res) => res.redirect("/"));

export default router;
