/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import * as fs from 'fs';
import * as CronJob from 'node-cron';
import { PersonalJsonData } from '../models/personal-data.model';
import config from '../utils/config';
import { ADMIN } from '../utils/constants';
import logger from '../utils/logger';
import utilities from '../utils/utilities';
import { OtaTokenModel } from './schemas/ota-token.schema';
import { PersonalDataModel } from './schemas/personal-data.schema';
import { SocialLinkModel } from './schemas/social-link.schema';
import { UserModel } from './schemas/user.schema';

class RunnersDb {
  private readonly PERSONAL_DATA_FILE = 'data/personal-data.json';

  async createDefaultUser(): Promise<void> {
    try {
      const userModel = await UserModel.findOne({ role: ADMIN });
      console.log(userModel);
      if (userModel !== null) {
        logger.info(
          `Load default user from db: ${JSON.stringify(
            userModel
          )}. 0 row affected.`
        );
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
      logger.info(
        `Saved default user in db: ${JSON.stringify(
          savedUser
        )}. 1 row affected.`
      );
    } catch (e) {
      logger.error('Unable to save default user in db. 0 rows affected.');
    }
  }

  removeNotUsedOtaTokensCronSchedule(): void {
    const scheduleJobFunction = CronJob.schedule('0 0 3 * * *', async () => {
      const deleted = await OtaTokenModel.deleteMany({
        $and: [{ isExpired: false }, { expiredAt: { lte: new Date() } }],
      });
      logger.info(
        `Removed unused ota tokens. Rows affected: ${deleted.deletedCount}`
      );
    });
    scheduleJobFunction.start();
  }

  async migratePersonalDataToDb(): Promise<void> {
    const rawFileData = fs.readFileSync(
      utilities.getProjectRootPath(`public/${this.PERSONAL_DATA_FILE}`)
    );
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
        githubName: jsonFileData.github_name,
        githubToken: jsonFileData.github_token,
      });
      await personalData.save();
      logger.info(
        `Successfully migrated personal data from ${this.PERSONAL_DATA_FILE} file to db. 1 row affected.`
      );
    }
    const socialLinks = await SocialLinkModel.find();
    if (socialLinks.length === 0) {
      const socialLinks = jsonFileData.social_links.map(
        l =>
          new SocialLinkModel({
            paraphrase: l.paraphrase,
            link: l.link,
            iconClass: l.icon_class,
          })
      );
      await SocialLinkModel.bulkSave(socialLinks);
      logger.info(`Successfully migrated social links from ${this.PERSONAL_DATA_FILE} file to db.
                ${jsonFileData.social_links.length} row affected.`);
    }
  }
}

export default new RunnersDb();
