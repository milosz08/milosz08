/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: is-not-logged.middleware.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:49:37
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
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
