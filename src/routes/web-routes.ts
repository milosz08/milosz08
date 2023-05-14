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

import { Request, Response, Router } from "express";

import authController from "../controllers/auth.controller";
import homeController from "../controllers/home.controller";
import cmsAccountController from "../controllers/cms-account.controller";
import cmsProjectsController from "../controllers/cms-projects.controller";
import cmsSocialLinksController from "../controllers/cms-social-links.controller";
import cmsPersonalDataController from "../controllers/cms-personal-data.controller";

import isAuthMiddleware from "../middleware/is-auth.middleware";
import isAuthAdminMiddleware from "../middleware/is-auth-admin.middleware";
import isNotLoggedAdminMiddleware from "../middleware/is-not-logged.middleware";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

const router: Router = Router();

router.get("/",                                                                             homeController.getHomePage);
router.get("/project/:name",                                                                homeController.getProjectDetailsPage);

router.get("/login",                                        isNotLoggedAdminMiddleware,     authController.getLoginPage);
router.get("/logout",                                       isAuthMiddleware,               authController.getLogoutRedirect);
router.get("/request-change-password",                      isNotLoggedAdminMiddleware,     authController.getRequestChangePasswordPage);
router.get("/change-password/:token",                       isNotLoggedAdminMiddleware,     authController.getChangePasswordPage);
router.get("/first-login",                                  isAuthMiddleware,               authController.getFirstLoginPage);

router.get("/cms/accounts",                                 isAuthAdminMiddleware,          cmsAccountController.getAccountsPage);
router.get("/cms/account/add",                              isAuthAdminMiddleware,          cmsAccountController.getCreateAccountPage);
router.get("/cms/account/update/:accountId",                isAuthAdminMiddleware,          cmsAccountController.getUpdateAccountPage);
router.get("/cms/account/delete/:accountId",                isAuthAdminMiddleware,          cmsAccountController.getDeleteAccountRedirect);
router.get("/cms/account/revoke-password/:accountId",       isAuthAdminMiddleware,          cmsAccountController.getRevokeAccountPasswordRedirect);

router.get("/cms/social-links",                             isAuthAdminMiddleware,          cmsSocialLinksController.getSocialLinksPage);
router.get("/cms/social-link/add",                          isAuthAdminMiddleware,          cmsSocialLinksController.getAddSocialLinksPage);
router.get("/cms/social-link/update/:socialLinkId",         isAuthAdminMiddleware,          cmsSocialLinksController.getUpdateSocialLinksPage);
router.get("/cms/social-link/delete/:socialLinkId",         isAuthAdminMiddleware,          cmsSocialLinksController.getDeleteSocialLinksRedirect);

router.get("/cms/personal-data",                            isAuthAdminMiddleware,          cmsPersonalDataController.getPersonalsDataPage);

router.get("/cms/projects",                             isAuthMiddleware,               cmsProjectsController.getProjectsPage);
router.get("/cms/project/add",                          isAuthMiddleware,               cmsProjectsController.getAddProjectPage);
router.get("/cms/project/update/:projectId",            isAuthMiddleware,               cmsProjectsController.getUpdateProjectPage);
router.get("/cms/project/delete/:projectId",            isAuthMiddleware,               cmsProjectsController.getDeleteProjectRedirect);

router.post("/login",                                       isNotLoggedAdminMiddleware,     authController.postLoginPage);
router.post("/request-change-password",                     isNotLoggedAdminMiddleware,     authController.postRequestChangePasswordPage);
router.post("/change-password/:token",                      isNotLoggedAdminMiddleware,     authController.postChangePasswordPage);
router.post("/first-login",                                 isAuthMiddleware,               authController.postFirstLoginPage);

router.post("/cms/account/add",                             isAuthAdminMiddleware,          cmsAccountController.postCreateAccountPage);
router.post("/cms/account/update/:accountId",               isAuthAdminMiddleware,          cmsAccountController.postUpdateAccountPage);

router.post("/cms/social-link/add",                         isAuthAdminMiddleware,          cmsSocialLinksController.postAddSocialLinksPage);
router.post("/cms/social-link/update/:socialLinkId",        isAuthAdminMiddleware,          cmsSocialLinksController.postUpdateSocialLinksPage);

router.post("/cms/personal-data",                           isAuthAdminMiddleware,          cmsPersonalDataController.postPersonalsDataPage);

router.post("/cms/project/add",                         isAuthMiddleware,               cmsProjectsController.postAddProjectPage);
router.post("/cms/project/update/:projectId",           isAuthMiddleware,               cmsProjectsController.postUpdateProjectPage);

router.get("/cms",                                          (req: Request, res: Response) => res.redirect("/cms/projects"));
router.get("*",                                             (req: Request, res: Response) => res.redirect("/"));

export default router;
