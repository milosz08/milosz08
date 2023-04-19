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
import { CMS_LAYOUT } from "../utils/constants";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AccountController {

    accountsPage(req: Request, res: Response): void {
        res.render(View.ACCOUNT_ACCOUNTS_EJS, {
            title: "Accounts",
            layout: CMS_LAYOUT
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    createAccountPage(req: Request, res: Response): void {
        res.render(View.ACCOUNT_ADD_UPDATE_ACCOUNT_EJS, {
            title: "Create account",
            layout: CMS_LAYOUT
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    updateAccountPage(req: Request, res: Response): void {
        res.render(View.ACCOUNT_ADD_UPDATE_ACCOUNT_EJS, {
            title: "Update account",
            layout: CMS_LAYOUT
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    deleteAccountRedirect(req: Request, res: Response): void {
        res.redirect("/cms/accounts");
    };
}

export default new AccountController;
