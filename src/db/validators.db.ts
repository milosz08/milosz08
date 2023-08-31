/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: validators.db.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:49:08
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import {
  ADMIN,
  EMAIL_REGEX,
  LINK_REGEX,
  LOGIN_REGEX,
  MODERATOR,
} from '../utils/constants';
import { PROJECT_STAGES } from '../utils/project-stages';

class ValidatorsDb {
  validateEmail(email: string): boolean {
    return EMAIL_REGEX.test(email);
  }

  validateRole(role: string): boolean {
    return [MODERATOR, ADMIN].some(r => r === role.toUpperCase());
  }

  validateStage(stage: string): boolean {
    return PROJECT_STAGES.some(s => s.slug === stage.toLowerCase());
  }

  validateLogin(login: string): boolean {
    return LOGIN_REGEX.test(login);
  }

  validateLink(link: string): boolean {
    return LINK_REGEX.test(link);
  }

  validateLinkNotRequired(link: string): boolean {
    return LINK_REGEX.test(link) || link === null;
  }
}

export default new ValidatorsDb();
