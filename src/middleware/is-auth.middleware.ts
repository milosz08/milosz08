/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import { Request, Response } from 'express';

export default function isAuthMiddleware(
  req: Request,
  res: Response,
  next: CallableFunction
) {
  const user = req.session.loggedUser;
  if (!user) {
    res.redirect('/login');
    return;
  }
  if (
    user.isFirstLogin &&
    !req.url.includes('first-login') &&
    !req.url.includes('logout')
  ) {
    res.redirect('/first-login');
    return;
  }
  next();
}
