/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
