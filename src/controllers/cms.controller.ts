/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: cms.controller.ts
 * Last modified: 19/04/2023, 12:08
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { Request, Response } from "express";

import * as View from "../utils/constants";
import { CMS_LAYOUT } from "../utils/constants";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class CmsController {

    personalsDataPage(req: Request, res: Response): void {
        res.render(View.CMS_PERSONAL_DATA_EJS, {
            title: "Personal data",
            layout: CMS_LAYOUT,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    projectsPage(req: Request, res: Response): void {
        res.render(View.CMS_PROJECTS_EJS, {
            title: "Projects",
            layout: CMS_LAYOUT,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    addProjectPage(req: Request, res: Response): void {
        res.render(View.CMS_ADD_UPDATE_PROJECT_EJS, {
            title: "Add projects",
            layout: CMS_LAYOUT,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    updateProjectPage(req: Request, res: Response): void {
        res.render(View.CMS_ADD_UPDATE_PROJECT_EJS, {
            title: "Update projects",
            layout: CMS_LAYOUT,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    deleteProjectRedirect(req: Request, res: Response): void {



        res.redirect("/cms/projects");
    };
}

export default new CmsController;
