/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import { HtmlRenderer, Parser } from 'commonmark';
import { Request } from 'express';
import path from 'path';
import { User } from '../db/schemas/user.schema';
import * as Constant from './constants';
import { AlertType, AlertTypeId } from './session';

class Utilities {
  private readonly CHARS =
    '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';

  getProjectRootPath(relativeDir: string): string {
    return path.join(__dirname, relativeDir);
  }

  extractAlertAndDestroy(req: Request, alertId: AlertTypeId): AlertType {
    let pageAlert = null;
    if (req.session[alertId]) {
      pageAlert = JSON.parse(JSON.stringify(req.session[alertId]));
    }
    req.session[alertId] = null;
    return pageAlert;
  }

  otaTokenGenerator(length: number = 10): string {
    let token: string = '';
    for (let i = 0; i < length; i++) {
      token += this.CHARS.charAt(Math.random() * this.CHARS.length);
    }
    return token;
  }

  nowPlusNminutes(minutes: number): Date {
    const date: Date = new Date();
    date.setMinutes(date.getMinutes() + minutes);
    return date;
  }

  validatePassword(
    user: User,
    newPassword: string,
    repeatNewPassword: string
  ): void {
    if (!Constant.PASSWORD_REGEX.test(newPassword)) {
      throw new Error(
        'Password must have at least 8 characters, one big letter, one number and one ' +
          'special character.'
      );
    }
    if (user.compareHash(newPassword)) {
      throw new Error('New password must be different from actual.');
    }
    if (newPassword !== repeatNewPassword) {
      throw new Error('Password and repeat password fields are not the same.');
    }
  }

  getFullUrl(req: Request): string {
    return req.protocol + '://' + req.get('host');
  }

  validatePaginationDataAndGetUrl(
    paginationUrl: string,
    selectedPage: number,
    pagesCount: number,
    totalPerPage: number
  ): string {
    const totalDefault = Constant.PAGINATION_STATES[0];
    if ((selectedPage < 1 || selectedPage > pagesCount) && pagesCount > 0) {
      return `${paginationUrl}page=1&total=${totalPerPage}`;
    }
    if (!Constant.PAGINATION_STATES.some(s => s === totalPerPage)) {
      return `${paginationUrl}page=${selectedPage}&total=${totalDefault}`;
    }
    return '';
  }

  parseMarkdown(rawData: string): string {
    const reader = new Parser();
    const writer = new HtmlRenderer();
    return writer.render(reader.parse(rawData));
  }
}

export default new Utilities();
