/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: account.controller.ts
 * Last modified: 19/04/2023, 12:24
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { Request, Response } from "express";
import mongoose from "mongoose";

import logger from "../utils/logger";
import utilities from "../utils/utilities";
import * as Constant from "../utils/constants";
import { AlertTypeId } from "../utils/session";
import MailSender, { EmailReplacements } from "../utils/mail-sender";

import { UserModel } from "../db/schemas/user-schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AccountController {

    async getAccountsPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.ACCOUNT_ACCOUNTS_EJS;
        const { q, page, total } = req.query;

        let selectedPage = Number(page) || 1;
        const totalPerPage = Number(total) || Constant.PAGINATION_STATES[0];
        const paginationUrl = q ? `/cms/accounts?q=${q}&` : "/cms/accounts?";

        const regex = { $regex: q || "", $options: "i" };
        const where = { $or: [ { login: regex }, { email: regex } ] };
        let query = UserModel.find(where);

        const resultsCount = await UserModel.find(where).count();
        const pagesCount = Math.ceil(resultsCount / totalPerPage);

        const retUrl = utilities.validatePaginationDataAndGetUrl(paginationUrl, selectedPage, pagesCount, totalPerPage);
        if (retUrl) {
            res.redirect(retUrl);
            return;
        }
        query = query.skip((selectedPage - 1) * totalPerPage);
        query = query.limit(totalPerPage);
        const users = await query.exec();

        res.render(path, { title, layout,
            pageAlert: utilities.extractAlertAndDestroy(req, AlertTypeId.CMS_ACCOUNTS_PAGE),
            users,
            page: selectedPage,
            pagesCount,
            resultsCount,
            totalPerPage,
            paginationUrl,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getCreateAccountPage(req: Request, res: Response): void {
        const { path, title, layout } = Constant.ACCOUNT_ADD_ACCOUNT_EJS;
        res.render(path, { title, layout,
            accountAction: "Add",
            roleOptions: [ Constant.MODERATOR, Constant.ADMIN ],
        });
    };

    async postCreateAccountPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.ACCOUNT_ADD_ACCOUNT_EJS;
        const { login, email, role } = req.body;
        try {
            const firstPassword = utilities.otaTokenGenerator();
            const newUser = new UserModel({
                login,
                email,
                role,
                password: firstPassword,
                firstLogin: true,
            });
            const savedUser = await newUser.save();

            const replacements: EmailReplacements = {
                userLogin: login,
                loginLink: `${utilities.getFullUrl(req)}/login`,
                firstPassword,
            };
            await MailSender.sendEmail(email, `Welcome in CMS system! (${login})`,
                Constant.AFTER_CREATED_ACCOUNT_MAIL_TEMPLATE, replacements);

            req.session[AlertTypeId.CMS_ACCOUNTS_PAGE] = {
                type: Constant.ALERT_SUCCESS,
                message: `Account for <strong>${login}</strong> was successfully created. Check mailbox
                    <strong>${email}</strong> and follow for rest of instructions.`,
            };
            logger.info(`Successfull created user account and send email message. Account: ${savedUser}`);
            res.redirect("/cms/accounts");
        } catch (ex: any) {
            logger.error(`Failure created user account. Cause: ${ex.message}`);
            res.render(path, { title, layout,
                generalError: ex.name !== "ValidationError" ? ex.message : "",
                errors: ex.errors,
                accountAction: "Add",
                roleOptions: [ Constant.MODERATOR, Constant.ADMIN ],
                form: req.body,
            });
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getUpdateAccountPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.ACCOUNT_UPDATE_ACCOUNT_EJS;
        const { accountId } = req.params;
        if (!mongoose.Types.ObjectId.isValid(accountId)) {
            res.redirect("/cms/accounts");
            return;
        }
        const user = await UserModel.findById(req.params.accountId);
        if (!user) {
            res.redirect("/cms/accounts");
            return;
        }
        const { login, email, role } = user;
        res.render(path, { title, layout,
            accountAction: "Update",
            roleOptions: [ Constant.MODERATOR, Constant.ADMIN ],
            form: { login, email, role },
        });
    };

    async postUpdateAccountPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.ACCOUNT_UPDATE_ACCOUNT_EJS;
        const { login, email, role } = req.body;
        const { accountId } = req.params;
        try {
            if (!mongoose.Types.ObjectId.isValid(accountId)) {
                res.redirect("/cms/accounts");
                return;
            }
            const user = await UserModel.findById(accountId);
            if (!user) {
                res.redirect("/cms/accounts");
                return;
            }
            const restOfAdmins = await UserModel
                .find({ $and: [ { role: Constant.ADMIN }, { login: { $ne: user.login } } ] });
            if (restOfAdmins.length === 0 && role === Constant.MODERATOR) {
                throw new Error("In CMS system must be at least one user with administrator permissions.");
            }
            user.login = login;
            user.email = email;
            user.role = role;

            const updatedUser = await user.save();

            req.session[AlertTypeId.CMS_ACCOUNTS_PAGE] = {
                type: Constant.ALERT_SUCCESS,
                message: `Account with login <strong>${user.login}</strong> was successfully updated.`,
            };
            logger.info(`Successfull updated user account. Account: ${updatedUser}`);
            res.redirect("/cms/accounts");
        } catch (ex: any) {
            logger.error(`Failure updated account informations. Cause: ${ex.message}`);
            res.render(path, { title, layout,
                generalError: ex.name !== "ValidationError" ? ex.message : "",
                errors: ex.errors,
                accountAction: "Update",
                roleOptions: [ Constant.MODERATOR, Constant.ADMIN ],
                form: req.body,
            });
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getDeleteAccountRedirect(req: Request, res: Response): Promise<void> {
        const { accountId } = req.params;

        let alertType: string = Constant.ALERT_SUCCESS;
        let alertMessage: string = "";

        if (!mongoose.Types.ObjectId.isValid(accountId)) {
            res.redirect("/cms/accounts");
            return;
        }
        try {
            const user = await UserModel.findById(accountId);
            if (!user) {
                res.redirect("/cms/accounts");
                return;
            }
            const restOfAdmins = await UserModel
                .find({ $and: [ { role: Constant.ADMIN }, { login: { $ne: user.login } } ] });
            if (restOfAdmins.length === 0) {
                throw new Error("You cannot delete last user with administrator permissions.");
            }
            await UserModel.findByIdAndDelete(user._id);
            alertMessage = `User account with login <strong>${user.login}</strong> was successfully deleted.`;
            logger.info(`Successfull deleted user account. Account: ${user}`);
        } catch (ex: any) {
            alertType = Constant.ALERT_DANGER;
            alertMessage = ex.message;
            logger.error(`Failure deleted user account. Cause: ${ex.message}`);
        }
        req.session[AlertTypeId.CMS_ACCOUNTS_PAGE] = {
            type: alertType,
            message: alertMessage,
        };
        res.redirect("/cms/accounts");
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getRevokeAccountPasswordRedirect(req: Request, res: Response): Promise<void> {
        const { accountId } = req.params;

        let alertType: string = Constant.ALERT_SUCCESS;
        let alertMessage: string = "";

        if (!mongoose.Types.ObjectId.isValid(accountId)) {
            res.redirect("/cms/accounts");
            return;
        }
        try {
            const user = await UserModel.findById(accountId);
            if (!user) {
                res.redirect("/cms/accounts");
                return;
            }
            const revokedPassword = utilities.otaTokenGenerator();

            user.password = revokedPassword;
            user.firstLogin = true;
            await user.save();

            const replacements: EmailReplacements = {
                userLogin: user.login,
                loginLink: `${utilities.getFullUrl(req)}/login`,
                revokedPassword,
            };
            await MailSender.sendEmail(user.email, `Your password was revoked! (${user.login})`,
                Constant.AFTER_REVOKED_PASSWORD_MAIL_TEMPLATE, replacements);

            alertMessage = `Password for account with login <strong>${user.login}</strong> was successfully revoked.
                Message with other instructions was send to <strong>${user.email}</strong>.`;
            logger.info(`Successfull revoked user password for ${user.login} and send email message`);
        } catch (ex: any) {
            alertType = Constant.ALERT_DANGER;
            alertMessage = ex.message;
            logger.info(`Failure revoked user password. Cause: ${ex.message}`);
        }
        req.session[AlertTypeId.CMS_ACCOUNTS_PAGE] = {
            type: alertType,
            message: alertMessage,
        };
        res.redirect("/cms/accounts");
    };
}

export default new AccountController;
