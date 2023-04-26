/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: cms-personal-data.controller.ts
 * Last modified: 26/04/2023, 13:33
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { Request, Response } from "express";

import logger from "../utils/logger";
import utilities from "../utils/utilities";
import * as Constant from "../utils/constants";
import { AlertTypeId } from "../utils/session";

import { PersonalDataModel } from "../db/schemas/personal-data.schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class CmsPersonalDataController {

    async getPersonalsDataPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.CMS_PERSONAL_DATA_EJS;

        let dataIsEmptyMessage: string = "";
        const personalDetails = await PersonalDataModel.findOne();
        if (!personalDetails) {
            dataIsEmptyMessage = "Personal data model not exist in datase.";
        }
        res.render(path, { title, layout,
            generalError: dataIsEmptyMessage,
            pageAlert: utilities.extractAlertAndDestroy(req, AlertTypeId.CMS_PERSONAL_DATA_PAGE),
            form: personalDetails,
        });
    };

    async postPersonalsDataPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.CMS_PERSONAL_DATA_EJS;

        const { dscTop, mvnLink, dscBottom, firstEmail, secondEmail, ghLink } = req.body;
        const personalDetails = await PersonalDataModel.findOne();
        try {
            if (!personalDetails) {
                throw new Error("Personal data model not exist in datase.");
            }
            personalDetails.descriptionTop = dscTop;
            personalDetails.mavenCentralLink = mvnLink;
            personalDetails.descriptionBottom = dscBottom;
            personalDetails.firstEmail = firstEmail;
            personalDetails.secondEmail = secondEmail;
            personalDetails.githubAccountLink = ghLink;

            await personalDetails.save();

            req.session[AlertTypeId.CMS_PERSONAL_DATA_PAGE] = {
                type: Constant.ALERT_SUCCESS,
                message: "Personal informations was successfully updated.",
            };
            logger.info("Successfully updated personal data.");
            res.redirect("/cms/personal-data");
        } catch (ex: any) {
            logger.info(`Failure updated personal data. Cause: ${ex.message}`);
            res.render(path, { title, layout,
                generalError: ex.name !== "ValidationError" ? ex.message : "",
                errors: ex.errors,
                form: personalDetails,
            });
        }
    };
}

export default new CmsPersonalDataController;
