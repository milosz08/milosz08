/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: variables.middleware.ts
 * Last modified: 20/04/2023, 21:36
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { Request, Response } from "express";

import { PAGINATION_STATES } from "../utils/constants";

import middlewaresDb from "../db/middlewares.db";
import { SocialLinkModel } from "../db/schemas/social-link.schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export default async function variablesMiddleware(req: Request, res: Response, next: CallableFunction) {
    res.locals.url = req.url;
    res.locals.pageAlert = null;
    res.locals.nowYear = new Date().getFullYear();
    res.locals.errors = null;
    res.locals.generalError = null;
    res.locals.form = {};
    res.locals.query = req.query;
    res.locals.paginationStates = PAGINATION_STATES;
    res.locals.personalData = await middlewaresDb.getPersonalDataAndMarkdownParse();
    res.locals.socialLinks = await SocialLinkModel.find();
    next();
}
