/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: auth.controller.ts
 * Last modified: 19/04/2023, 12:12
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { Request, Response } from "express";

import logger from "../utils/logger";
import utilities from "../utils/utilities";
import * as View from "../utils/constants";
import { AlertTypeId } from "../utils/session";
import { ALERT_SUCCESS } from "../utils/constants";
import { UserModel } from "../db/schemas/user-schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AuthController {

    getLoginPage(req: Request, res: Response): void {
        const { path, title, layout } = View.AUTH_LOGIN_EJS;
        res.render(path, { title, layout,
            pageAlert: utilities.extractAlertAndDestroy(req, AlertTypeId.LOGIN_PAGE),
        });
    };

    async postLoginPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.AUTH_LOGIN_EJS;
        const { loginOrEmail, password } = req.body;
        try {
            const user = await UserModel.findOne({ $or: [ { login: loginOrEmail }, { email: loginOrEmail } ] });
            if (!user) {
                throw new Error("user not found");
            }
            if (!user.compareHash(password)) {
                throw new Error("invalid password");
            }
            req.session.loggedUser = {
                login: user.login,
                role: user.role,
                isFirstLogin: user.firstLogin,
            };
            if (user.firstLogin) {
                res.redirect(`/first-login`);
                return;
            }
            req.session[AlertTypeId.CMS_PROJECTS_PAGE]  = {
                type: ALERT_SUCCESS,
                message: `You successfully logged into <strong>${user.role.toLowerCase()}</strong> account.`,
            };
            res.redirect(`/cms/projects`);
        } catch (ex: any) {
            logger.error(`Authentication failed. Cause: ${ex.message}`);
            res.render(path, { title, layout,
                errors: true,
                form: req.body,
            });
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getLogoutRedirect(req: Request, res: Response): void {
        const { role } = req.session.loggedUser!;
        req.session[AlertTypeId.LOGIN_PAGE] = {
            type: ALERT_SUCCESS,
            message: `Successfully logout from <strong>${role.toLowerCase()}</strong> account.`,
        };
        req.session.loggedUser = null;
        res.redirect("/login");
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getFirstLoginPage(req: Request, res: Response): void {
        const { path, title, layout } = View.AUTH_FIRST_LOGIN_EJS;
        res.render(path, { title, layout });
    };

    async postFirstLoginPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.AUTH_FIRST_LOGIN_EJS;
        const { newPassword, repeatNewPassword } = req.body;
        if (newPassword !== repeatNewPassword) {
            res.render(path, { title, layout,
                generalError: "Password and repeat password fields are not the same.",
                form: req.body,
            });
            return;
        }
        try {
            const user = await UserModel.findOne({ login: req.session.loggedUser?.login });
            if (!user) throw new Error("user not found");

            if (user.compareHash(newPassword)) {
                res.render(path, { title, layout,
                    generalError: "New password must be different from old pre-generated password.",
                    form: req.body,
                });
                return;
            }
            user.password = newPassword;
            user.firstLogin = false;
            await user.save();

            req.session.loggedUser!.isFirstLogin = false;
            req.session[AlertTypeId.CMS_PROJECTS_PAGE] = {
                type: ALERT_SUCCESS,
                message: "Successfully changed default password for your account.",
            };
            res.redirect("/cms/projects");
        } catch (ex: any) {
            logger.error(`First password failure changed. Cause: ${ex.message}`);
            res.render(path, { title, layout,
                errors: ex.errors,
                form: req.body,
            });
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getRequestChangePasswordPage(req: Request, res: Response): void {
        const { path, title, layout } = View.AUTH_REQUEST_CHANGE_PASSWORD_EJS;
        res.render(path, { title, layout });
    };

    async postRequestChangePasswordPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.AUTH_REQUEST_CHANGE_PASSWORD_EJS;
        res.render(path, { title, layout });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getChangePasswordPage(req: Request, res: Response): void {
        const { path, title, layout } = View.AUTH_CHANGE_PASSWORD_EJS;
        res.render(path, { title, layout });
    };

    async postChangePasswordPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.AUTH_CHANGE_PASSWORD_EJS;
        res.render(path, { title, layout });
    };
}

export default new AuthController;
