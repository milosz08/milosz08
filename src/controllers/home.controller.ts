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
import utilities from "../utils/utilities";
import githubApi from "../utils/github-api";
import projectImages from "../files/project-images";

import { ProjectModel } from "../db/schemas/project.schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class HomeController {

    async getHomePage(req: Request, res: Response): Promise<void> {
        const { path, title } = View.PUBLIC_PROJECTS_EJS;
        const { q, page } = req.query;

        let selectedPage = Number(page) || 1;
        const paginationUrl = q ? `?q=${q}&` : "?";

        const regex = { $regex: q || "", $options: "i" };
        const where = { $or: [ { name: regex }, { alternativeName: regex } ] };
        let query = ProjectModel.find(where).sort({ position: 1 });

        const resultsCount = await ProjectModel.find(where).count();
        const pagesCount = Math.ceil(resultsCount / 6);

        if ((selectedPage < 1 || selectedPage > pagesCount) && pagesCount > 0) {
            res.redirect(`${paginationUrl}page=1`);
            return;
        }
        query = query.skip((selectedPage - 1) * 6);
        query = query.limit(6);
        const projects = await query.exec();

        const projectsData = await githubApi.getAllUserProjects(Array.from(projects).map(p => p.name));
        const projectsMapped = projects.map(p => p.id);
        projectsData.sort((x, y) => projectsMapped.indexOf(x.id) - projectsMapped.indexOf(y.id))
        const mergedData = projects.map((p, i) => ({ projectDb: p, projectApi: projectsData[i] }));

        res.render(path, { title,
            page: selectedPage,
            pagesCount,
            paginationUrl,
            mergedData
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getProjectDetailsPage(req: Request, res: Response): Promise<void> {
        const { path } = View.PUBLIC_PROJECT_DETAILS_EJS;
        const { name } = req.params;

        const project  = await ProjectModel.findOne({ name });
        if (!project) {
            res.redirect("/");
            return;
        }
        project.detailsDescription = utilities.parseMarkdown(project.detailsDescription);
        project.techStackPositions.sort((x, y) => x.pos - y.pos);
        const projectApi = await githubApi.getSingleProjectDetails(name);

        res.render(path, {
            title: project.alternativeName,
            projectDb: project,
            projectApi,
            projectImages: await projectImages.parseToFullPaths(project._id.toString()),
        });
    };
}

export default new HomeController;
