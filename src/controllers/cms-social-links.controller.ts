/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: cms-social-links.controller.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:48:24
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import { Request, Response } from 'express';
import mongoose from 'mongoose';
import { SocialLinkModel } from '../db/schemas/social-link.schema';
import * as Constant from '../utils/constants';
import logger from '../utils/logger';
import { AlertTypeId } from '../utils/session';
import utilities from '../utils/utilities';

class CmsSocialLinksController {
  async getSocialLinksPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_SOCIAL_LINKS_EJS;
    const { q, page, total } = req.query;

    const selectedPage = Number(page) || 1;
    const totalPerPage = Number(total) || Constant.PAGINATION_STATES[0];
    const paginationUrl = q
      ? `/cms/social-links?q=${q}&`
      : '/cms/social-links?';

    const regex = { $regex: q || '', $options: 'i' };
    const where = {
      $or: [{ paraphrase: regex }, { link: regex }, { iconClass: regex }],
    };
    let query = SocialLinkModel.find(where);

    const resultsCount = await SocialLinkModel.find(where).count();
    const pagesCount = Math.ceil(resultsCount / totalPerPage);

    const retUrl = utilities.validatePaginationDataAndGetUrl(
      paginationUrl,
      selectedPage,
      pagesCount,
      totalPerPage
    );
    if (retUrl) {
      res.redirect(retUrl);
      return;
    }
    query = query.skip((selectedPage - 1) * totalPerPage);
    query = query.limit(totalPerPage);
    const socialLinks = await query.exec();

    res.render(path, {
      title,
      layout,
      pageAlert: utilities.extractAlertAndDestroy(
        req,
        AlertTypeId.CMS_SOCIAL_LINKS_PAGE
      ),
      socialLinks,
      page: selectedPage,
      pagesCount,
      resultsCount,
      totalPerPage,
      paginationUrl,
    });
  }

  getAddSocialLinksPage(req: Request, res: Response) {
    const { path, title, layout } = Constant.CMS_ADD_SOCIAL_LINKS_EJS;
    res.render(path, { title, layout, socialLinkAction: 'Add' });
  }

  async postAddSocialLinksPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_ADD_SOCIAL_LINKS_EJS;
    const { paraphrase, link, iconClass } = req.body;
    try {
      const newSocialLink = new SocialLinkModel({
        paraphrase,
        link,
        iconClass,
      });
      const savedNewSocialLink = await newSocialLink.save();

      req.session[AlertTypeId.CMS_SOCIAL_LINKS_PAGE] = {
        type: Constant.ALERT_SUCCESS,
        message: 'Social link was successfully created.',
      };
      logger.info(
        `Successfull created new social link: ${JSON.stringify(
          savedNewSocialLink
        )}.`
      );
      res.redirect('/cms/social-links');
    } catch (ex: any) {
      logger.error(`Failure create new social link. Cause: ${ex.message}`);
      res.render(path, {
        title,
        layout,
        generalError: ex.name !== 'ValidationError' ? ex.message : '',
        errors: ex.errors,
        socialLinkAction: 'Add',
        form: req.body,
      });
    }
  }

  async getUpdateSocialLinksPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_UPDATE_SOCIAL_LINKS_EJS;
    const { socialLinkId } = req.params;
    if (!mongoose.Types.ObjectId.isValid(socialLinkId)) {
      res.redirect('/cms/social-links');
      return;
    }
    const socialLink = await SocialLinkModel.findById(socialLinkId);
    if (!socialLink) {
      res.redirect('/cms/social-links');
      return;
    }
    res.render(path, {
      title,
      layout,
      socialLinkAction: 'Update',
      form: socialLink,
    });
  }

  async postUpdateSocialLinksPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_UPDATE_SOCIAL_LINKS_EJS;
    const { paraphrase, link, iconClass } = req.body;
    const { socialLinkId } = req.params;
    try {
      if (!mongoose.Types.ObjectId.isValid(socialLinkId)) {
        res.redirect('/cms/social-links');
        return;
      }
      const socialLink = await SocialLinkModel.findById(socialLinkId);
      if (!socialLink) {
        res.redirect('/cms/social-links');
        return;
      }
      socialLink.paraphrase = paraphrase;
      socialLink.link = link;
      socialLink.iconClass = iconClass;

      const updatedSocialLink = await socialLink.save();

      req.session[AlertTypeId.CMS_SOCIAL_LINKS_PAGE] = {
        type: Constant.ALERT_SUCCESS,
        message: 'Social link was successfully updated.',
      };
      logger.info(
        `Successfull update social link: ${JSON.stringify(updatedSocialLink)}.`
      );
      res.redirect('/cms/social-links');
    } catch (ex: any) {
      logger.error(`Failure update social link. Cause: ${ex.message}`);
      res.render(path, {
        title,
        layout,
        generalError: ex.name !== 'ValidationError' ? ex.message : '',
        errors: ex.errors,
        socialLinkAction: 'Update',
        form: req.body,
      });
    }
  }

  async getDeleteSocialLinksRedirect(
    req: Request,
    res: Response
  ): Promise<void> {
    const { socialLinkId } = req.params;

    let alertType: string = Constant.ALERT_SUCCESS;
    let alertMessage: string = '';

    if (!mongoose.Types.ObjectId.isValid(socialLinkId)) {
      res.redirect('/cms/social-links');
      return;
    }
    try {
      const socialLink = await SocialLinkModel.findById(socialLinkId);
      if (!socialLink) {
        res.redirect('/cms/social-links');
        return;
      }
      await SocialLinkModel.findByIdAndRemove(socialLinkId);

      alertMessage = 'Social link was successfully removed.';
      logger.info(
        `Successfull delete social link: ${JSON.stringify(socialLink)}.`
      );
    } catch (ex: any) {
      alertType = Constant.ALERT_DANGER;
      alertMessage = ex.message;
      logger.error(`Failure delete social link. Cause: ${ex.message}`);
    }
    req.session[AlertTypeId.CMS_SOCIAL_LINKS_PAGE] = {
      type: alertType,
      message: alertMessage,
    };
    res.redirect('/cms/social-links');
  }
}

export default new CmsSocialLinksController();
