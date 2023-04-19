/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: home.controller.ts
 * Last modified: 15/04/2023, 18:25
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { Request, Response } from "express";

import * as View from "../utils/constants";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class HomeController {

    homePage(req: Request, res: Response): void {
        res.render(View.PUBLIC_PROJECTS_EJS, {
            title: "Projects",
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    projectDetailsPage(req: Request, res: Response): void {
        res.render(View.PUBLIC_PROJECT_DETAILS_EJS, {
            title: "Project",
        });
    };
}

export default new HomeController;
