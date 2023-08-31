/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: project-images.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:49:25
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import { Request } from 'express';
import * as path from 'path';
import utilities from '../utils/utilities';
import asyncApi from './async-api';

type ProjectImage = {
  absolutePath: string;
  imageName: string;
  index: number;
};

class ProjectImages {
  private readonly BASE_PATH = 'public/uploads/projects';

  async parseToFullPaths(projectId: string): Promise<ProjectImage[]> {
    const dirPath = utilities.getProjectRootPath(
      `${this.BASE_PATH}/${projectId}`
    );
    if (!(await asyncApi.existAsync(dirPath))) return [];

    const files = await asyncApi.readdirAsync(dirPath);
    return files.map((file, idx) => ({
      absolutePath: `/assets/uploads/projects/${projectId}/${file}`,
      imageName: file,
      index: idx,
    }));
  }

  async saveProjectImages(req: Request, projectId: string): Promise<void> {
    if (!req.files) return;
    const files = req.files as Express.Multer.File[];
    if (files.length === 0) return;

    const dirPath = utilities.getProjectRootPath(
      `${this.BASE_PATH}/${projectId}`
    );
    await asyncApi.mkdirAsync(dirPath, { recursive: true });
    for (const file of files) {
      const movingFile = file.destination + file.filename;
      const toDir =
        dirPath + '/' + file.filename + path.extname(file.originalname);
      if (!(await asyncApi.existAsync(movingFile))) continue;
      await asyncApi.renameAsync(movingFile, toDir);
    }
  }

  async deleteSingleProjectImage(
    projectId: string,
    imageFilename: string
  ): Promise<void> {
    const dirPath = utilities.getProjectRootPath(
      `${this.BASE_PATH}/${projectId}`
    );
    const filePath = `${dirPath}/${imageFilename}`;
    if (!(await asyncApi.existAsync(filePath))) return;
    await asyncApi.unlinkAsync(filePath);

    const files = await asyncApi.readdirAsync(dirPath);
    if (files.length !== 0) return;

    await asyncApi.rmdirAsync(dirPath);
  }

  async deleteAllProjectImages(projectId: string): Promise<void> {
    const dirPath = utilities.getProjectRootPath(
      `${this.BASE_PATH}/${projectId}`
    );
    if (!(await asyncApi.existAsync(dirPath))) return;
    await asyncApi.rmdirAsync(dirPath, { recursive: true });
  }
}

export default new ProjectImages();
