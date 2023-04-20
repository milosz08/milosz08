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

import * as View from "../utils/constants";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AccountController {

    getAccountsPage(req: Request, res: Response): void {
        const { path, title, layout } = View.ACCOUNT_ACCOUNTS_EJS;
        res.render(path, { title, layout });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getCreateAccountPage(req: Request, res: Response): void {
        const { path, title, layout } = View.ACCOUNT_ADD_ACCOUNT_EJS;
        res.render(path, { title, layout });
    };

    async postCreateAccountPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.ACCOUNT_ADD_ACCOUNT_EJS;
        res.render(path, { title, layout });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getUpdateAccountPage(req: Request, res: Response): void {
        const { path, title, layout } = View.ACCOUNT_UPDATE_ACCOUNT_EJS;
        res.render(path, { title, layout });
    };

    async postUpdateAccountPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.ACCOUNT_UPDATE_ACCOUNT_EJS;
        res.render(path, { title, layout });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getDeleteAccountRedirect(req: Request, res: Response): Promise<void> {
        res.redirect("/cms/accounts");
    };
}

export default new AccountController;
