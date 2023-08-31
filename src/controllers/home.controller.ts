/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: home.controller.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:48:29
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import { Request, Response } from 'express';
import { ProjectModel } from '../db/schemas/project.schema';
import projectImages from '../files/project-images';
import { GithubProjectDetailsApiModel } from '../models/github-project-details-api.model';
import * as View from '../utils/constants';
import githubApi from '../utils/github-api';
import logger from '../utils/logger';
import { PROJECT_STAGES } from '../utils/project-stages';
import utilities from '../utils/utilities';

class HomeController {
  async getHomePage(req: Request, res: Response): Promise<void> {
    const { path, title } = View.PUBLIC_PROJECTS_EJS;
    const { q, s, page } = req.query;

    const selectedPage = Number(page) || 1;
    let paginationUrl = q ? `?q=${q}&` : '?';
    if (s) paginationUrl += `s=${s}&`;

    const regex = { $regex: q || '', $options: 'i' };
    const stRegex = { $regex: s || '', $options: 'i' };
    let where: any = {
      $and: [{ $or: [{ name: regex }, { alternativeName: regex }] }],
    };
    if (s && s !== 'all') {
      where = {
        $and: [
          { $or: [{ name: regex }, { alternativeName: regex }] },
          { stage: stRegex },
        ],
      };
    }
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
    projectsData.sort(
      (x, y) => projectsMapped.indexOf(x.id) - projectsMapped.indexOf(y.id)
    );
    const mergedData = projects
      .filter(p => !idsToDelete.includes(p.id))
      .map((p, i) => ({ projectDb: p, projectApi: projectsData[i] }));

    res.render(path, {
      title,
      page: selectedPage,
      pagesCount,
      paginationUrl,
      mergedData,
      projectStages: PROJECT_STAGES,
      selectedStage: s,
    });
  }

  async getProjectDetailsPage(req: Request, res: Response): Promise<void> {
    const { path } = View.PUBLIC_PROJECT_DETAILS_EJS;
    const { name } = req.params;

    const project = await ProjectModel.findOne({ name });
    if (!project) {
      res.redirect('/');
      return;
    }
    project.detailsDescription = utilities.parseMarkdown(
      project.detailsDescription
    );
    project.techStackPositions.sort((x, y) => x.pos - y.pos);
    let projectApi: GithubProjectDetailsApiModel | null = null;
    try {
      projectApi = await githubApi.getSingleProjectDetails(name);
    } catch (ex: any) {
      await githubApi.deleteProjectAndMoveCursor(project._id.toString());
      logger.info(
        `Deleted already removed github project with id: '${project.id}'`
      );
      res.redirect('/');
      return;
    }
    res.render(path, {
      title: project.alternativeName,
      projectDb: project,
      projectApi,
      projectImages: await projectImages.parseToFullPaths(
        project._id.toString()
      ),
      projectStage:
        PROJECT_STAGES.find(s => s.slug === project.stage) || PROJECT_STAGES[0],
    });
  }
}

export default new HomeController();
