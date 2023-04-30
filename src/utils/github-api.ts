/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: github-api.ts
 * Last modified: 30/04/2023, 02:11
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { ProjectModel } from "../db/schemas/project.schema";
import axios from "axios";

import config from "./config";
import { ProjectSelectDataModel } from "../models/project-select-data.model";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GithubApi {

    private axiosInstance = axios.create({
        headers: { "Authorization": `token ${config.GITHUB_API_TOKEN}` },
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getAllParsedNotPersistedProject(exactField = ""): Promise<ProjectSelectDataModel[]> {
        let projects: any = [];
        if (exactField) {
            projects = await ProjectModel.find({ name: { $not: { $regex: exactField } } });
        } else {
            projects = await ProjectModel.find({});
        }
        const { data } = await this.axiosInstance.get("https://api.github.com/users/Milosz08/repos");
        return data
            .map((d: any) => new ProjectSelectDataModel(d.id, d.name))
            .filter((d: ProjectSelectDataModel) => !projects.map((p: any) => p.id).includes(d.id))
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getRepoId(repoName: string): Promise<number> {
        const { data } = await this.axiosInstance.get(`https://api.github.com/repos/Milosz08/${repoName}`);
        return data.id;
    };
}

export default new GithubApi;

