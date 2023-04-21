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
import { ADMIN } from "../utils/constants";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export default function loggedUserMiddleware(req: Request, res: Response, next: CallableFunction) {
    res.locals.loggedUser = req.session.loggedUser;
    res.locals.isAdmin = req.session.loggedUser?.role === ADMIN;
    next();
}
