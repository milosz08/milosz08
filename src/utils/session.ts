/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: session.ts
 * Last modified: 20/04/2023, 22:14
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { SessionOptions } from "express-session";
import config from "./config";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export type AlertType = { type: string, message: string } | null;
export type UserType = { login: string, role: string, isFirstLogin: boolean } | null

export enum AlertTypeId {
    LOGIN_PAGE = "loginPageAlert",
    REQUEST_CHANGE_PASSWORD_PAGE = "requestChangePasswordPageAlert",
    CMS_PROJECTS_PAGE = "cmsProjectsPageAlert",
    CMS_PROJECT_UPDATE_PAGE = "cmsProjectUpdatePageAlert",
    CMS_ACCOUNTS_PAGE = "cmsAccountsPageAlert",
    CMS_PERSONAL_DATA_PAGE = "cmsPersonalDataPageAlert",
    CMS_SOCIAL_LINKS_PAGE = "cmsSocialLinksPageAlert",
}

declare module 'express-session' {
    interface SessionData {
        loggedUser: UserType;
        [AlertTypeId.LOGIN_PAGE]: AlertType;
        [AlertTypeId.CMS_PROJECTS_PAGE]: AlertType;
    }
}

class Session {

    configure(): SessionOptions {
        return {
            secret: config.SESSION_KEY,
            saveUninitialized: true,
            cookie: { maxAge: 1000 * 60 * 60 * 24 },
            resave: false,
        };
    };
}

export default new Session;
