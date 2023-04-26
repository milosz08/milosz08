/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: cms-projects.controller.ts
 * Last modified: 19/04/2023, 12:08
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { Request, Response } from "express";

import utilities from "../utils/utilities";
import * as Constant from "../utils/constants";
import { AlertTypeId } from "../utils/session";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class CmsController {

    getPersonalsDataPage(req: Request, res: Response): void {
        const { path, title, layout } = View.CMS_PERSONAL_DATA_EJS;
        res.render(path, { title, layout });
    };

    async postPersonalsDataPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = View.CMS_PERSONAL_DATA_EJS;
        res.render(path, { title, layout });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getProjectsPage(req: Request, res: Response): void {
        const { path, title, layout } = Constant.CMS_PROJECTS_EJS;
        res.render(path, { title, layout,
            pageAlert: utilities.extractAlertAndDestroy(req, AlertTypeId.CMS_PROJECTS_PAGE),
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getAddProjectPage(req: Request, res: Response): void {
        const { path, title, layout } = Constant.CMS_ADD_PROJECT_EJS;
        res.render(path, { title, layout });
    };

    async postAddProjectPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.CMS_ADD_PROJECT_EJS;
        res.render(path, { title, layout });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getUpdateProjectPage(req: Request, res: Response): void {
        const { path, title, layout } = Constant.CMS_UPDATE_PROJECT_EJS;
        res.render(path, { title, layout });
    };

    async postUpdateProjectPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.CMS_UPDATE_PROJECT_EJS;
        res.render(path, { title, layout });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getDeleteProjectRedirect(req: Request, res: Response): Promise<void> {
        res.redirect("/cms/projects");
    };
}

export default new CmsProjectsController;
