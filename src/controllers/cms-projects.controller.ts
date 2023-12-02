/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import { Request, Response } from 'express';
import mongoose from 'mongoose';
import { ProjectModel } from '../db/schemas/project.schema';
import projectImages from '../files/project-images';
import * as Constant from '../utils/constants';
import githubApi from '../utils/github-api';
import logger from '../utils/logger';
import { PROJECT_STAGES } from '../utils/project-stages';
import { AlertTypeId } from '../utils/session';
import utilities from '../utils/utilities';

class CmsProjectsController {
  async getProjectsPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_PROJECTS_EJS;
    const { q, page, total } = req.query;

    const selectedPage = Number(page) || 1;
    const totalPerPage = Number(total) || Constant.PAGINATION_STATES[0];
    const paginationUrl = q ? `/cms/projects?q=${q}&` : '/cms/projects?';

    const regex = { $regex: q || '', $options: 'i' };
    const where = { $or: [{ name: regex }, { alternativeName: regex }] };
    let query = ProjectModel.find(where).sort({ position: 1 });

    const resultsCount = await ProjectModel.find(where).count();
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
    const projects = await query.exec();

    const projectsData = await githubApi.getAllUserProjects(
      Array.from(projects).map(p => p.name)
    );
    const apiProjMapped = projectsData.map(p => p.id);
    const projectsMapped = projects.map(p => p.id);

    const idsToDelete: number[] = [
      ...new Set([...apiProjMapped, ...projectsMapped]),
    ].filter(
      value => !apiProjMapped.includes(value) || !projectsMapped.includes(value)
    );
    if (idsToDelete.length > 0) {
      const projectsToDelete = await ProjectModel.find({
        id: { $in: idsToDelete },
      });
      for (const project of [...projectsToDelete]) {
        await githubApi.deleteProjectAndMoveCursor(project._id.toString());
      }
      logger.info(
        `Deleted already removed github projects with ids: '${idsToDelete}'`
      );
    }
    res.render(path, {
      title,
      layout,
      pageAlert: utilities.extractAlertAndDestroy(
        req,
        AlertTypeId.CMS_PROJECTS_PAGE
      ),
      projects: projects.filter(p => !idsToDelete.includes(p.id)),
      page: selectedPage,
      pagesCount,
      resultsCount,
      totalPerPage,
      paginationUrl,
    });
  }

  async getAddProjectPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_ADD_PROJECT_EJS;
    const notPersistProjects =
      await githubApi.getAllParsedNotPersistedProject();

    res.render(path, {
      title,
      layout,
      projectAction: 'Add',
      projects: notPersistProjects,
      techStacks: JSON.stringify([{ name: '', error: false, errorMess: '' }]),
      projectImages: [],
      projectId: '',
      projectStages: PROJECT_STAGES,
    });
  }

  async postAddProjectPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_ADD_PROJECT_EJS;
    const { ghProject, altName, stage, detDesc, techStacks, extLink } =
      req.body;

    const notPersistProjects =
      await githubApi.getAllParsedNotPersistedProject();
    const projectsCount = await ProjectModel.find({}).count();
    if (await githubApi.deleteAlreadyRemovedProject(req, res, ghProject)) {
      res.redirect('/cms/projects');
      return;
    }
    try {
      const newProject = new ProjectModel({
        id: await githubApi.getRepoId(ghProject),
        position: projectsCount + 1,
        name: ghProject,
        stage,
        alternativeName: altName,
        externalLink: extLink || null,
        detailsDescription: detDesc,
        techStackPositions: techStacks.map((s: string, i: number) => ({
          pos: i,
          name: s,
        })),
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
      logger.info(
        `Successfull created project. Project: ${JSON.stringify(newProject)}`
      );
      res.redirect('/cms/projects');
    } catch (ex: any) {
      logger.error(`Failure created project. Cause: ${ex.message}`);

      const techStacksWithErrors = techStacks.map((s: string, i: number) => {
        const error = ex.errors['techStackPositions.' + String(i) + '.name'];
        return { name: s, error, errorMess: error?.message };
      });
      res.render(path, {
        title,
        layout,
        projectAction: 'Add',
        errors: ex.errors,
        projects: notPersistProjects,
        form: req.body,
        techStacks: JSON.stringify(techStacksWithErrors),
        projectImages: [],
        projectId: '',
        projectStages: PROJECT_STAGES,
      });
    }
  }

  async getUpdateProjectPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_UPDATE_PROJECT_EJS;
    const { projectId } = req.params;

    const projectsCount = await ProjectModel.find({}).count();
    if (!mongoose.Types.ObjectId.isValid(projectId)) {
      res.redirect('/cms/projects');
      return;
    }
    const project = await ProjectModel.findById(projectId);
    if (!project) {
      res.redirect('/cms/projects');
      return;
    }
    const notPersistProjects = await githubApi.getAllParsedNotPersistedProject(
      project.name
    );
    const {
      name,
      alternativeName,
      stage,
      position,
      externalLink,
      detailsDescription,
    } = project;
    const techStacks = project.techStackPositions.map(e => ({
      name: e.name,
      error: false,
      errorMess: '',
    }));

    if (
      await githubApi.deleteAlreadyRemovedProject(
        req,
        res,
        name,
        project._id.toString(),
        true
      )
    ) {
      res.redirect('/cms/projects');
      return;
    }
    res.render(path, {
      title,
      layout,
      projectAction: 'Update',
      pageAlert: utilities.extractAlertAndDestroy(
        req,
        AlertTypeId.CMS_PROJECT_UPDATE_PAGE
      ),
      projects: notPersistProjects,
      posMax: projectsCount,
      form: {
        ghProject: name,
        stage,
        listPos: position,
        altName: alternativeName,
        extLink: externalLink,
        detDesc: detailsDescription,
      },
      techStacks: JSON.stringify(techStacks),
      projectImages: await projectImages.parseToFullPaths(projectId),
      projectId,
      projectStages: PROJECT_STAGES,
    });
  }

  async postUpdateProjectPage(req: Request, res: Response): Promise<void> {
    const { path, title, layout } = Constant.CMS_UPDATE_PROJECT_EJS;
    const { ghProject, listPos, altName, stage, extLink, detDesc, techStacks } =
      req.body;
    const { projectId } = req.params;
    if (!mongoose.Types.ObjectId.isValid(projectId)) {
      res.redirect('/cms/projects');
      return;
    }
    const updatedProject = await ProjectModel.findById(projectId);
    if (!updatedProject) {
      res.redirect('/cms/projects');
      return;
    }
    if (
      await githubApi.deleteAlreadyRemovedProject(
        req,
        res,
        updatedProject.name,
        updatedProject._id.toString(),
        true
      )
    ) {
      res.redirect('/cms/projects');
      return;
    }
    const notPersistProjects = await githubApi.getAllParsedNotPersistedProject(
      updatedProject.name
    );
    const projectsCount = await ProjectModel.find({}).count();
    try {
      const updatedProjectPos = await ProjectModel.findOne({
        position: listPos,
      });
      const prevValue = updatedProject.position;
      if (updatedProjectPos) {
        updatedProjectPos.position = prevValue;
        await updatedProjectPos.save();
      }
      await projectImages.saveProjectImages(req, projectId);

      updatedProject.id = notPersistProjects.find(e => e.name === ghProject)
        ?.id;
      updatedProject.name = ghProject;
      updatedProject.stage = stage;
      updatedProject.position = listPos;
      updatedProject.alternativeName = altName;
      updatedProject.externalLink = extLink || null;
      updatedProject.detailsDescription = detDesc;
      updatedProject.techStackPositions = techStacks.map(
        (s: string, i: number) => ({ pos: i, name: s })
      );

      await updatedProject.save();

      req.session[AlertTypeId.CMS_PROJECTS_PAGE] = {
        type: Constant.ALERT_SUCCESS,
        message: `Project <strong>${updatedProject.name}</strong> was successfully updated.`,
      };
      logger.info(
        `Successfull updated project. Project: ${JSON.stringify(
          updatedProject
        )}`
      );
      res.redirect('/cms/projects');
    } catch (ex: any) {
      logger.error(`Failure updated project. Cause: ${ex.message}`);

      const techStacksWithErrors = techStacks.map((s: string, i: number) => {
        const error = ex.errors['techStackPositions.' + String(i) + '.name'];
        return { name: s, error, errorMess: error?.message };
      });
      res.render(path, {
        title,
        layout,
        projectAction: 'Update',
        errors: ex.errors,
        projects: notPersistProjects,
        form: req.body,
        posMax: projectsCount,
        techStacks: JSON.stringify(techStacksWithErrors),
        projectImages: await projectImages.parseToFullPaths(projectId),
        projectId,
        projectStages: PROJECT_STAGES,
      });
    }
  }

  async getDeleteProjectRedirect(req: Request, res: Response): Promise<void> {
    const { projectId } = req.params;

    let alertType: string = Constant.ALERT_SUCCESS;
    let alertMessage: string = '';

    if (!mongoose.Types.ObjectId.isValid(projectId)) {
      res.redirect('/cms/projects');
      return;
    }
    try {
      const project = await ProjectModel.findById(projectId);
      if (!project) {
        res.redirect('/cms/projects');
        return;
      }
      await githubApi.deleteProjectAndMoveCursor(project._id.toString());

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
    res.redirect('/cms/projects');
  }

  async getDeleteProjectImageRedirect(
    req: Request,
    res: Response
  ): Promise<void> {
    const { projectId, image } = req.params;

    let alertType: string = Constant.ALERT_SUCCESS;
    let alertMessage: string = '';

    if (!mongoose.Types.ObjectId.isValid(projectId)) {
      res.redirect('/cms/projects');
      return;
    }
    try {
      const project = await ProjectModel.findById(projectId);
      if (!project) {
        res.redirect('/cms/projects');
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
  }
}

export default new CmsProjectsController();
