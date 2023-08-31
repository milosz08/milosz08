/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: logged-user.middleware.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:49:42
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import { Request, Response } from 'express';
import { ADMIN } from '../utils/constants';

export default function loggedUserMiddleware(
  req: Request,
  res: Response,
  next: CallableFunction
) {
  res.locals.loggedUser = req.session.loggedUser;
  res.locals.isAdmin = req.session.loggedUser?.role === ADMIN;
  next();
}
