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
import * as Constant from "../utils/constants";
import Utilities from "../utils/utilities";
import { AlertTypeId } from "../utils/session";
import MailSender, { EmailReplacements } from "../utils/mail-sender";

import { UserModel } from "../db/schemas/user-schema";
import { OtaTokenModel } from "../db/schemas/ota-token-schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AuthController {

    getLoginPage(req: Request, res: Response): void {
        const { path, title, layout } = Constant.AUTH_LOGIN_EJS;
        res.render(path, { title, layout,
            pageAlert: utilities.extractAlertAndDestroy(req, AlertTypeId.LOGIN_PAGE),
        });
    };

    async postLoginPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.AUTH_LOGIN_EJS;
        const { loginOrEmail, password } = req.body;
        try {
            const user = await UserModel.findOne({ $or: [ { login: loginOrEmail }, { email: loginOrEmail } ] });
            if (!user) {
                throw new Error("Invalid email and/or password. Try again with another credentials.");
            }
            if (!user.compareHash(password)) {
                throw new Error("Invalid email and/or password. Try again with another credentials.");
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
                type: Constant.ALERT_SUCCESS,
                message: `You successfully logged into <strong>${user.role.toLowerCase()}</strong> account.`,
            };
            logger.info(`Successfully login to: '${user.login}' account`);
            res.redirect(`/cms/projects`);
        } catch (ex: any) {
            logger.error(`Authentication failed. Cause: ${ex.message}`);
            res.render(path, { title, layout,
                generalError: ex.message,
                form: req.body,
            });
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getLogoutRedirect(req: Request, res: Response): void {
        const { role } = req.session.loggedUser!;
        req.session[AlertTypeId.LOGIN_PAGE] = {
            type: Constant.ALERT_SUCCESS,
            message: `Successfully logout from <strong>${role.toLowerCase()}</strong> account.`,
        };
        logger.info(`Successfully logout from: '${req.session.loggedUser?.login}' account`);
        req.session.loggedUser = null;
        res.redirect("/login");
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getFirstLoginPage(req: Request, res: Response): void {
        const { path, title, layout } = Constant.AUTH_FIRST_LOGIN_EJS;
        res.render(path, { title, layout });
    };

    async postFirstLoginPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.AUTH_FIRST_LOGIN_EJS;
        const { newPassword, repeatNewPassword } = req.body;
        try {
            const user = await UserModel.findOne({login: req.session.loggedUser?.login});
            if (!user) throw new Error("User based passed login or email not found.");

            Utilities.validatePassword(user, newPassword, repeatNewPassword);

            user.password = newPassword;
            user.firstLogin = false;
            await user.save();

            req.session.loggedUser!.isFirstLogin = false;
            req.session[AlertTypeId.CMS_PROJECTS_PAGE] = {
                type: Constant.ALERT_SUCCESS,
                message: "Password for your account was successfully changed.",
            };
            logger.info(`Successfully change default password for: '${user.login}' account`);
            res.redirect("/cms/projects");
        } catch (ex: any) {
            logger.error(`First password failure changed. Cause: ${ex.message}`);
            res.render(path, { title, layout,
                generalError: ex.message,
                form: req.body,
            });
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getRequestChangePasswordPage(req: Request, res: Response): void {
        const { path, title, layout } = Constant.AUTH_REQUEST_CHANGE_PASSWORD_EJS;
        res.render(path, { title, layout,
            pageAlert: utilities.extractAlertAndDestroy(req, AlertTypeId.REQUEST_CHANGE_PASSWORD_PAGE),
        });
    };

    async postRequestChangePasswordPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.AUTH_REQUEST_CHANGE_PASSWORD_EJS;
        const { loginOrEmail } = req.body;
        try {
            const user = await UserModel.findOne({ $or: [ { login: loginOrEmail }, { email: loginOrEmail } ] });
            if (!user) {
                throw new Error("User based passed login or email not found.");
            }
            let generatedToken: string = "";
            do {
                generatedToken = Utilities.otaTokenGenerator();
            } while (await OtaTokenModel.findOne({ token: generatedToken }));

            const otaToken = new OtaTokenModel({
                token: generatedToken,
                expiredAt: Utilities.nowPlusNminutes(10),
                isExpired: false,
                user: user._id,
            });
            await otaToken.save();

            const replacements: EmailReplacements = {
                userLogin: user.login,
                linkWithToken: `${Utilities.getFullUrl(req)}/change-password/${generatedToken}`,
                regenerateToken: `${Utilities.getFullUrl(req)}/request-change-password`,
            };
            await MailSender.sendEmail(user.email, `Request for change password (${user.login})`,
                Constant.CHANGE_PASSWORD_MAIL_TEMPLATE, replacements);

            req.session[AlertTypeId.REQUEST_CHANGE_PASSWORD_PAGE] = {
                type: Constant.ALERT_SUCCESS,
                message: `Check your email box <strong>${user.email}</strong> and follow on the rest of instructions.`,
            };
            logger.info(`Successfully send request for change password for: '${user.login}' account`);
            res.redirect("/request-change-password");
        } catch (ex: any) {
            logger.error(`Request password failure sended. Cause: ${ex.message}`);
            res.render(path, { title, layout,
                generalError: ex.message,
                form: req.body,
            });
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getChangePasswordPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.AUTH_CHANGE_PASSWORD_EJS;
        const { token } = req.params;
        const otaToken = await OtaTokenModel.findOne({ token });
        const isExpired = otaToken === null || otaToken.isExpired || otaToken.expiredAt < new Date;
        res.render(path, { title, layout,
            tokenIsValid: !isExpired,
            token,
        });
    };

    async postChangePasswordPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.AUTH_CHANGE_PASSWORD_EJS;
        const { token } = req.params;
        const { newPassword, repeatNewPassword } = req.body;
        let isInvalid: boolean = false;
        try {
            const otaToken = await OtaTokenModel.findOne({ token }).populate("user");
            isInvalid = otaToken === null || otaToken.isExpired || otaToken.expiredAt < new Date;
            if (isInvalid) {
                throw new Error("Token is invalid, expired or already used. Try with another token");
            }
            const user = otaToken!.user;
            Utilities.validatePassword(user, newPassword, repeatNewPassword);

            user.password = newPassword;
            otaToken!.isExpired = true;
            await otaToken!.save();

            await UserModel.findOneAndUpdate({ "login": user.login }, { "password": newPassword });

            req.session[AlertTypeId.LOGIN_PAGE] = {
                type: ALERT_SUCCESS,
                message: "Password for your account was successfully changed.",
            };
            logger.info(`Successfully change password for: '${user.login}' account`);
            res.redirect("/login");
        } catch (ex: any) {
            logger.error(`Password failure changed. Cause: ${ex.message}`);
            res.render(path, { title, layout,
                generalError: ex.message,
                tokenIsValid: !isInvalid,
                form: req.body,
                token,
            });
        }

    };
}

export default new AuthController;
