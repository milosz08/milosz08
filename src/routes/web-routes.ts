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

router.get("/",                                     homeController.getHomePage);
router.get("/project/:name",                        homeController.getProjectDetailsPage);

router.get("/login",                                authController.getLoginPage);
router.get("/logout",                               authController.getLogoutPage);
router.get("/request-change-password",              authController.getRequestChangePasswordPage);
router.get("/change-password",                      authController.getChangePasswordPage);
router.get("/first-login",                          authController.getFirstLoginPage);

router.get("/cms/accounts",                         accountController.getAccountsPage);
router.get("/cms/create-account",                   accountController.getCreateAccountPage);
router.get("/cms/update-account/:accountId",        accountController.getUpdateAccountPage);
router.get("/cms/delete-account/:accountId",        accountController.getDeleteAccountRedirect);

router.get("/cms/personals",                        cmsController.getPersonalsDataPage);
router.get("/cms/projects",                         cmsController.getProjectsPage);
router.get("/cms/projects/add",                     cmsController.getAddProjectPage);
router.get("/cms/projects/update/:projectId",       cmsController.getUpdateProjectPage);
router.get("/cms/projects/delete/:projectId",       cmsController.getDeleteProjectRedirect);

router.post("/login",                               authController.postLoginPage);
router.post("/request-change-password",             authController.postRequestChangePasswordPage);
router.post("/change-password",                     authController.postChangePasswordPage);
router.post("/first-login",                         authController.postFirstLoginPage);

router.post("/cms/create-account",                  accountController.postCreateAccountPage);
router.post("/cms/update-account/:accountId",       accountController.postUpdateAccountPage);

router.post("/cms/personals",                       cmsController.postPersonalsDataPage);
router.post("/cms/projects/add",                    cmsController.postAddProjectPage);
router.post("/cms/projects/update/:projectId",      cmsController.postUpdateProjectPage);

router.get("/cms",                                  (req, res) => res.redirect("/cms/personals"));
router.get("*",                                     (req, res) => res.redirect("/"));

export default router;
