/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
