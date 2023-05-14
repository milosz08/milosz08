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
import mongoose from "mongoose";

import logger from "../utils/logger";
import utilities from "../utils/utilities";
import githubApi from "../utils/github-api";
import * as Constant from "../utils/constants";
import { AlertTypeId } from "../utils/session";
import projectImages from "../files/project-images";

import { ProjectModel } from "../db/schemas/project.schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class CmsProjectsController {

    async getProjectsPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.CMS_PROJECTS_EJS;
        const { q, page, total } = req.query;

        let selectedPage = Number(page) || 1;
        const totalPerPage = Number(total) || Constant.PAGINATION_STATES[0];
        const paginationUrl = q ? `/cms/projects?q=${q}&` : "/cms/projects?";

        const regex = { $regex: q || "", $options: "i" };
        const where = { $or: [ { name: regex }, { alternativeName: regex } ] };
        let query = ProjectModel.find(where).sort({ position: 1 });

        const resultsCount = await ProjectModel.find(where).count();
        const pagesCount = Math.ceil(resultsCount / totalPerPage);

        const retUrl = utilities.validatePaginationDataAndGetUrl(paginationUrl, selectedPage, pagesCount, totalPerPage);
        if (retUrl) {
            res.redirect(retUrl);
            return;
        }
        query = query.skip((selectedPage - 1) * totalPerPage);
        query = query.limit(totalPerPage);
        const projects = await query.exec();

        res.render(path, { title, layout,
            pageAlert: utilities.extractAlertAndDestroy(req, AlertTypeId.CMS_PROJECTS_PAGE),
            projects,
            page: selectedPage,
            pagesCount,
            resultsCount,
            totalPerPage,
            paginationUrl,
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getAddProjectPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.CMS_ADD_PROJECT_EJS;
        const notPersistProjects = await githubApi.getAllParsedNotPersistedProject();

        res.render(path, { title, layout,
            projectAction: "Add",
            projects: notPersistProjects,
            techStacks: JSON.stringify([ { name: "", error: false, errorMess: "" } ]),
            projectImages: [],
            projectId: "",
        });
    };

    async postAddProjectPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.CMS_ADD_PROJECT_EJS;
        const { ghProject, altName, detDesc, techStacks, extLink } = req.body;

        const notPersistProjects = await githubApi.getAllParsedNotPersistedProject();
        const projectsCount = await ProjectModel.find({}).count();
        try {
            const newProject = new ProjectModel({
                id: await githubApi.getRepoId(ghProject),
                position: projectsCount + 1,
                name: ghProject,
                alternativeName: altName,
                externalLink: extLink || null,
                detailsDescription: detDesc,
                techStackPositions: techStacks.map((s: string, i: number) => ({ pos: i, name: s })),
            });
            const savedProject = await newProject.save();
            const preImages = await ProjectModel.findById(savedProject._id);
            if (preImages) {
                await projectImages.saveProjectImages(req, preImages._id.toString());
            }
            req.session[AlertTypeId.CMS_PROJECTS_PAGE] = {
                type: Constant.ALERT_SUCCESS,
                message: `Project <strong>${newProject.name}</strong> was successfully created.`,
            };
            logger.info(`Successfull created project. Project: ${JSON.stringify(newProject)}`);
            res.redirect("/cms/projects");
        } catch (ex: any) {
            logger.error(`Failure created project. Cause: ${ex.message}`);

            const techStacksWithErrors = techStacks.map((s: string, i: number) => {
                const error = ex.errors["techStackPositions." + String(i) + ".name"];
                return { name: s, error, errorMess: error?.message }
            });
            res.render(path, { title, layout,
                projectAction: "Add",
                errors: ex.errors,
                projects: notPersistProjects,
                form: req.body,
                techStacks: JSON.stringify(techStacksWithErrors),
                projectImages: [],
                projectId: "",
            });
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getUpdateProjectPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.CMS_UPDATE_PROJECT_EJS;
        const { projectId } = req.params;

        const projectsCount = await ProjectModel.find({}).count();
        if (!mongoose.Types.ObjectId.isValid(projectId)) {
            res.redirect("/cms/projects");
            return;
        }
        const project = await ProjectModel.findById(projectId);
        if (!project) {
            res.redirect("/cms/projects");
            return;
        }
        const notPersistProjects = await githubApi.getAllParsedNotPersistedProject(project.name);
        const { name, alternativeName, position, externalLink, detailsDescription } = project;
        const techStacks = project.techStackPositions.map(e => ({ name: e.name, error: false, errorMess: "" }));

        res.render(path, { title, layout,
            projectAction: "Update",
            pageAlert: utilities.extractAlertAndDestroy(req, AlertTypeId.CMS_PROJECT_UPDATE_PAGE),
            projects: notPersistProjects,
            posMax: projectsCount,
            form: { ghProject: name, listPos: position, altName: alternativeName,
                extLink: externalLink, detDesc: detailsDescription },
            techStacks: JSON.stringify(techStacks),
            projectImages: await projectImages.parseToFullPaths(projectId),
            projectId,
        });
    };

    async postUpdateProjectPage(req: Request, res: Response): Promise<void> {
        const { path, title, layout } = Constant.CMS_UPDATE_PROJECT_EJS;
        const { ghProject, listPos, altName, extLink, detDesc, techStacks } = req.body;
        const { projectId } = req.params;
        if (!mongoose.Types.ObjectId.isValid(projectId)) {
            res.redirect("/cms/projects");
            return;
        }
        const updatedProject = await ProjectModel.findById(projectId);
        if (!updatedProject) {
            res.redirect("/cms/projects");
            return;
        }
        const notPersistProjects = await githubApi.getAllParsedNotPersistedProject(updatedProject.name);
        const projectsCount = await ProjectModel.find({}).count();
        try {
            const updatedProjectPos = await ProjectModel.findOne({ position: listPos });
            const prevValue = updatedProject.position;
            if (updatedProjectPos) {
                updatedProjectPos.position = prevValue;
                await updatedProjectPos.save();
            }
            await projectImages.saveProjectImages(req, projectId);

            updatedProject.id = notPersistProjects.find(e => e.name === ghProject)?.id;
            updatedProject.name = ghProject;
            updatedProject.position = listPos;
            updatedProject.alternativeName = altName;
            updatedProject.externalLink = extLink || null;
            updatedProject.detailsDescription = detDesc;
            updatedProject.techStackPositions = techStacks.map((s: string, i: number) => ({ pos: i, name: s }));

            await updatedProject.save();

            req.session[AlertTypeId.CMS_PROJECTS_PAGE] = {
                type: Constant.ALERT_SUCCESS,
                message: `Project <strong>${updatedProject.name}</strong> was successfully updated.`,
            };
            logger.info(`Successfull updated project. Project: ${JSON.stringify(updatedProject)}`);
            res.redirect("/cms/projects");
        } catch (ex: any) {
            logger.error(`Failure updated project. Cause: ${ex.message}`);

            const techStacksWithErrors = techStacks.map((s: string, i: number) => {
                const error = ex.errors["techStackPositions." + String(i) + ".name"];
                return { name: s, error, errorMess: error?.message }
            });
            res.render(path, { title, layout,
                projectAction: "Update",
                errors: ex.errors,
                projects: notPersistProjects,
                form: req.body,
                posMax: projectsCount,
                techStacks: JSON.stringify(techStacksWithErrors),
                projectImages: await projectImages.parseToFullPaths(projectId),
                projectId,
            });
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getDeleteProjectRedirect(req: Request, res: Response): Promise<void> {
        const { projectId } = req.params;

        let alertType: string = Constant.ALERT_SUCCESS;
        let alertMessage: string = "";

        if (!mongoose.Types.ObjectId.isValid(projectId)) {
            res.redirect("/cms/projects");
            return;
        }
        try {
            const project = await ProjectModel.findById(projectId);
            if (!project) {
                res.redirect("/cms/projects");
                return;
            }
            const remainingElements = await ProjectModel
                .findByIdAndUpdate(
                    { _id: projectId },
                    { $pull: { arrayField: { _id: projectId } } },
                    { new: true }
                )
                .select({ position: 1 })
                .then((deletedElement) =>
                    ProjectModel.find({ position: { $gt: deletedElement!.position } }).sort({ position: 1 })
                );
            await ProjectModel.updateMany(
                { _id: { $in: remainingElements.map((e) => e._id) } },
                { $inc: { position: -1 } }
            );
            await ProjectModel.findByIdAndRemove(projectId);
            await projectImages.deleteAllProjectImages(projectId);

            alertMessage = `Project <strong>${project.name}</strong> was successfully removed.`;
            logger.info(`Successfull delete project: ${JSON.stringify(project)}.`);
        } catch (ex: any) {
            alertType = Constant.ALERT_DANGER;
            alertMessage = ex.message;
            logger.error(`Failure delete project. Cause: ${ex.message}`);
        }
        req.session[AlertTypeId.CMS_PROJECTS_PAGE] = {
            type: alertType,
            message: alertMessage,
        };
        res.redirect("/cms/projects");
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getDeleteProjectImageRedirect(req: Request, res: Response): Promise<void> {
        const { projectId, image } = req.params;

        let alertType: string = Constant.ALERT_SUCCESS;
        let alertMessage: string = "";

        if (!mongoose.Types.ObjectId.isValid(projectId)) {
            res.redirect("/cms/projects");
            return;
        }
        try {
            const project = await ProjectModel.findById(projectId);
            if (!project) {
                res.redirect("/cms/projects");
                return;
            }
            await projectImages.deleteSingleProjectImage(projectId, image);

            alertMessage = `Project image <strong>${image}</strong> was successfully removed.`;
            logger.info(`Successfull delete project image: ${image}.`);
        } catch (ex: any) {
            alertType = Constant.ALERT_DANGER;
            alertMessage = ex.message;
            logger.error(`Failure delete project image. Cause: ${ex.message}`);
        }
        req.session[AlertTypeId.CMS_PROJECT_UPDATE_PAGE] = {
            type: alertType,
            message: alertMessage,
        };
        res.redirect(`/cms/project/update/${projectId}`);
    };
}

export default new CmsProjectsController;
