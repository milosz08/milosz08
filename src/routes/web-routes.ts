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

import isAuthMiddleware from "../middleware/is-auth.middleware";
import isAuthAdminMiddleware from "../middleware/is-auth-admin.middleware";
import isNotLoggedAdminMiddleware from "../middleware/is-not-logged.middleware";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

const router: Router = Router();

router.get("/",                                                                     homeController.getHomePage);
router.get("/project/:name",                                                        homeController.getProjectDetailsPage);

router.get("/login",                                isNotLoggedAdminMiddleware,     authController.getLoginPage);
router.get("/logout",                               isAuthMiddleware,               authController.getLogoutRedirect);
router.get("/request-change-password",              isNotLoggedAdminMiddleware,     authController.getRequestChangePasswordPage);
router.get("/change-password",                      isNotLoggedAdminMiddleware,     authController.getChangePasswordPage);
router.get("/first-login",                          isAuthMiddleware,               authController.getFirstLoginPage);

router.get("/cms/accounts",                         isAuthAdminMiddleware,          accountController.getAccountsPage);
router.get("/cms/account/add",                      isAuthAdminMiddleware,          accountController.getCreateAccountPage);
router.get("/cms/account/update/:accountId",        isAuthAdminMiddleware,          accountController.getUpdateAccountPage);
router.get("/cms/account/delete/:accountId",        isAuthAdminMiddleware,          accountController.getDeleteAccountRedirect);

router.get("/cms/personals",                        isAuthAdminMiddleware,          cmsController.getPersonalsDataPage);
router.get("/cms/projects",                         isAuthMiddleware,               cmsController.getProjectsPage);
router.get("/cms/project/add",                      isAuthMiddleware,               cmsController.getAddProjectPage);
router.get("/cms/project/update/:projectId",        isAuthMiddleware,               cmsController.getUpdateProjectPage);
router.get("/cms/project/delete/:projectId",        isAuthMiddleware,               cmsController.getDeleteProjectRedirect);

router.post("/login",                               isNotLoggedAdminMiddleware,     authController.postLoginPage);
router.post("/request-change-password",             isNotLoggedAdminMiddleware,     authController.postRequestChangePasswordPage);
router.post("/change-password",                     isNotLoggedAdminMiddleware,     authController.postChangePasswordPage);
router.post("/first-login",                         isAuthMiddleware,               authController.postFirstLoginPage);

router.post("/cms/account/add",                     isNotLoggedAdminMiddleware,     accountController.postCreateAccountPage);
router.post("/cms/account/update/:accountId",       isNotLoggedAdminMiddleware,     accountController.postUpdateAccountPage);

router.post("/cms/personals",                       isNotLoggedAdminMiddleware,     cmsController.postPersonalsDataPage);
router.post("/cms/project/add",                     isAuthMiddleware,               cmsController.postAddProjectPage);
router.post("/cms/project/update/:projectId",       isAuthMiddleware,               cmsController.postUpdateProjectPage);

router.get("/cms",                                  (req, res) => res.redirect("/cms/projects"));
router.get("*",                                     (req, res) => res.redirect("/"));

export default router;
