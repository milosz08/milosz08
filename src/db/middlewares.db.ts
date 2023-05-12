/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: middlewares.db.ts
 * Last modified: 19/04/2023, 16:08
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import * as bcrypt from "bcrypt";

import utilities from "../utils/utilities";
import { PersonalDataModel } from "./schemas/personal-data.schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class MiddlewaresDb {

    hashPassword(rawPassword: string): string {
        const salt = bcrypt.genSaltSync(10);
        return bcrypt.hashSync(rawPassword, salt)
    };

    async getPersonalDataAndMarkdownParse(): Promise<object> {
        const personalData = await PersonalDataModel.findOne();
        if (!personalData) return {};

        personalData.descriptionTop = utilities.parseMarkdown(personalData.descriptionTop);
        personalData.descriptionBottom = utilities.parseMarkdown(personalData.descriptionBottom);
        return personalData;
    };
}

export default new MiddlewaresDb;
