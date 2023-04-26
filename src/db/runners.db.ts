/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: runners.db.ts
 * Last modified: 19/04/2023, 16:13
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import * as CronJob from "node-cron";
import * as fs from "fs";

import config from "../utils/config";
import logger from "../utils/logger";
import { ADMIN } from "../utils/constants";
import utilities from "../utils/utilities";
import { PersonalJsonData } from "../models/personal-data.model";

import { UserModel } from "./schemas/user.schema";
import { OtaTokenModel } from "./schemas/ota-token.schema";
import { SocialLinkModel } from "./schemas/social-link.schema";
import { PersonalDataModel } from "./schemas/personal-data.schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class RunnersDb {

    private readonly PERSONAL_DATA_FILE = "data/personal-data.json";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async createDefaultUser(): Promise<void> {
        try {
            const userModel = await UserModel.findOne({ role: ADMIN });
            if (userModel !== null) {
                logger.info(`Load default user from db: ${JSON.stringify(userModel)}. 0 row affected.`);
                return;
            }
            const defaultUser = new UserModel({
                login: config.DEF_USER_LOGIN,
                email: config.DEF_USER_EMAIL,
                password: config.DEF_USER_PASSWORD,
                role: ADMIN,
                firstLogin: true,
            });
            const savedUser = await defaultUser.save();
            logger.info(`Saved default user in db: ${JSON.stringify(savedUser)}. 1 row affected.`);
        } catch (e) {
            logger.error("Unable to save default user in db. 0 rows affected.");
        }
    };

    removeNotUsedOtaTokensCronSchedule(): void {
        const scheduleJobFunction = CronJob.schedule("0 0 3 * * *", async () => {
            const deleted = await OtaTokenModel
                .deleteMany({ $and: [ { isExpired: false }, { expiredAt: { lte: new Date() } } ]});
            logger.info(`Removed unused ota tokens. Rows affected: ${deleted.deletedCount}`);
        });
        scheduleJobFunction.start();
    };

    async migratePersonalDataToDb(): Promise<void> {
        const rawFileData = fs.readFileSync(utilities.getProjectRootPath(`/public/${this.PERSONAL_DATA_FILE}`));
        const jsonFileData: PersonalJsonData = JSON.parse(rawFileData.toString());

        const personalData = await PersonalDataModel.find();
        if (personalData.length === 0) {
            const personalData = new PersonalDataModel({
                descriptionTop: jsonFileData.description_top,
                descriptionBottom: jsonFileData.description_bottom,
                mavenCentralLink: jsonFileData.maven_central_link,
                firstEmail: jsonFileData.first_email,
                secondEmail: jsonFileData.second_email,
                githubAccountLink: jsonFileData.github_account_link,
            });
            await personalData.save();
            logger.info(`Successfully migrated personal data from ${this.PERSONAL_DATA_FILE} file to db. 1 row affected.`);
        }
        const socialLinks = await SocialLinkModel.find();
        if (socialLinks.length === 0) {
            const socialLinks = jsonFileData.social_links.map(l => new SocialLinkModel({
                paraphrase: l.paraphrase,
                link: l.link,
                iconClass: l.icon_class,
            }));
            await SocialLinkModel.bulkSave(socialLinks);
            logger.info(`Successfully migrated social links from ${this.PERSONAL_DATA_FILE} file to db.
                ${jsonFileData.social_links.length} row affected.`);
        }
    };
}

export default new RunnersDb;
