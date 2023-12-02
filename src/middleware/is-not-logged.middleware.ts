/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import { Request, Response } from 'express';

export default function isNotLoggedAdminMiddleware(
  req: Request,
  res: Response,
  next: CallableFunction
) {
  const user = req.session.loggedUser;
  if (user) {
    res.redirect('/cms/projects');
    return;
  }
  next();
}
