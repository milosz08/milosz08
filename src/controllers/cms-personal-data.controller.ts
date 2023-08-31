/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: cms-personal-data.controller.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:48:15
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import { Request, Response } from 'express';
import { PersonalDataModel } from '../db/schemas/personal-data.schema';
import * as Constant from '../utils/constants';
import logger from '../utils/logger';
import { AlertTypeId } from '../utils/session';
import utilities from '../utils/utilities';

class CmsPersonalDataController {
  async getPersonalsDataPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_PERSONAL_DATA_EJS;

    let dataIsEmptyMessage: string = '';
    const personalDetails = await PersonalDataModel.findOne();
    if (!personalDetails) {
      dataIsEmptyMessage = 'Personal data model not exist in datase.';
    }
    res.render(path, {
      title,
      layout,
      generalError: dataIsEmptyMessage,
      pageAlert: utilities.extractAlertAndDestroy(
        req,
        AlertTypeId.CMS_PERSONAL_DATA_PAGE
      ),
      form: personalDetails,
    });
  }

  async postPersonalsDataPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_PERSONAL_DATA_EJS;

    const {
      dscTop,
      mvnLink,
      dscBottom,
      firstEmail,
      secondEmail,
      ghLink,
      ghName,
      ghToken,
    } = req.body;
    const personalDetails = await PersonalDataModel.findOne();
    try {
      if (!personalDetails) {
        throw new Error('Personal data model not exist in datase.');
      }
      personalDetails.descriptionTop = dscTop;
      personalDetails.mavenCentralLink = mvnLink;
      personalDetails.descriptionBottom = dscBottom;
      personalDetails.firstEmail = firstEmail;
      personalDetails.secondEmail = secondEmail;
      personalDetails.githubAccountLink = ghLink;
      personalDetails.githubName = ghName;
      personalDetails.githubToken = ghToken;

      await personalDetails.save();

      req.session[AlertTypeId.CMS_PERSONAL_DATA_PAGE] = {
        type: Constant.ALERT_SUCCESS,
        message: 'Personal informations was successfully updated.',
      };
      logger.info('Successfully updated personal data.');
      res.redirect('/cms/personal-data');
    } catch (ex: any) {
      logger.info(`Failure updated personal data. Cause: ${ex.message}`);
      res.render(path, {
        title,
        layout,
        generalError: ex.name !== 'ValidationError' ? ex.message : '',
        errors: ex.errors,
        form: personalDetails,
      });
    }
  }
}

export default new CmsPersonalDataController();
