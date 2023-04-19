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

import * as View from "../utils/constants";
import { EMPTY_LAYOUT } from "../utils/constants";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AuthController {

    loginPage(req: Request, res: Response): void {
        res.render(View.AUTH_LOGIN_EJS, {
            title: "Login",
            layout: EMPTY_LAYOUT,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    logoutPage(req: Request, res: Response): void {
        res.render(View.AUTH_LOGOUT_EJS, {
            title: "Logout",
            layout: EMPTY_LAYOUT,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    firstLoginPage(req: Request, res: Response): void {
        res.render(View.AUTH_FIRST_LOGIN_EJS, {
            title: "First login",
            layout: EMPTY_LAYOUT,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    requestChangePasswordPage(req: Request, res: Response): void {
        res.render(View.AUTH_REQUEST_CHANGE_PASSWORD_EJS, {
            title: "Send request to change password",
            layout: EMPTY_LAYOUT,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    changePasswordPage(req: Request, res: Response): void {
        res.render(View.AUTH_CHANGE_PASSWORD_EJS, {
            title: "Change password",
            layout: EMPTY_LAYOUT,
        });
    };
}

export default new AuthController;
