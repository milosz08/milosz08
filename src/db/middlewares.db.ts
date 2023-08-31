/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: middlewares.db.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:49:00
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import * as bcrypt from 'bcrypt';
import utilities from '../utils/utilities';
import { PersonalDataModel } from './schemas/personal-data.schema';

class MiddlewaresDb {
  hashPassword(rawPassword: string): string {
    const salt = bcrypt.genSaltSync(10);
    return bcrypt.hashSync(rawPassword, salt);
  }

  async getPersonalDataAndMarkdownParse(): Promise<object> {
    const personalData = await PersonalDataModel.findOne();
    if (!personalData) return {};

    personalData.descriptionTop = utilities.parseMarkdown(
      personalData.descriptionTop
    );
    personalData.descriptionBottom = utilities.parseMarkdown(
      personalData.descriptionBottom
    );
    return personalData;
  }
}

export default new MiddlewaresDb();
