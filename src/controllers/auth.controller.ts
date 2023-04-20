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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class AuthController {

    getLoginPage(req: Request, res: Response): void {
        const { path, title, layout } = View.AUTH_LOGIN_EJS;
        res.render(path, { title, layout });
    };

    async postLoginPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.AUTH_LOGIN_EJS;
        res.render(path, { title, layout });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getLogoutPage(req: Request, res: Response): void {
        const { path, title, layout } = View.AUTH_LOGOUT_EJS;
        res.render(path, { title, layout });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getFirstLoginPage(req: Request, res: Response): void {
        const { path, title, layout } = View.AUTH_FIRST_LOGIN_EJS;
        res.render(path, { title, layout });
    };

    async postFirstLoginPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.AUTH_FIRST_LOGIN_EJS;
        res.render(path, { title, layout });
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
