/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: db-runners.ts
 * Last modified: 19/04/2023, 16:13
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import * as CronJob from "node-cron";

import config from "../utils/config";
import logger from "../utils/logger";
import { ADMIN } from "../utils/constants";
import { UserModel } from "./schemas/user-schema";
import { OtaTokenModel } from "./schemas/ota-token-schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class DbRunners {

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
}

export default new DbRunners;
