/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: variables.middleware.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:49:46
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import { Request, Response } from 'express';
import middlewaresDb from '../db/middlewares.db';
import { SocialLinkModel } from '../db/schemas/social-link.schema';
import { PAGINATION_STATES } from '../utils/constants';

export default async function variablesMiddleware(
  req: Request,
  res: Response,
  next: CallableFunction
) {
  res.locals.url = req.url;
  res.locals.pageAlert = null;
  res.locals.nowYear = new Date().getFullYear();
  res.locals.errors = null;
  res.locals.generalError = null;
  res.locals.form = {};
  res.locals.query = req.query;
  res.locals.paginationStates = PAGINATION_STATES;
  res.locals.personalData =
    await middlewaresDb.getPersonalDataAndMarkdownParse();
  res.locals.socialLinks = await SocialLinkModel.find();
  next();
}
