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

import { IProjectSelectDataModel } from "../models/project-select-data.model";
import { IGithubProjectDataApiModel } from "../models/github-project-data-api.model";
import { IGithubProjectDetailsApiModel, ILanguage } from "../models/github-project-details-api.model";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GithubApi {

    private GH_REPOS = () => "https://api.github.com/users/Milosz08/repos";
    private GH_REPO = (repoName: string) => `https://api.github.com/repos/Milosz08/${repoName}`;
    private GH_REPO_LANGS = (repoName: string) => `https://api.github.com/repos/Milosz08/${repoName}/languages`;
    private GH_COLORS = () => "https://raw.githubusercontent.com/ozh/github-colors/master/colors.json";

    private axiosInstance = axios.create({
        headers: { "Authorization": `token ${config.GITHUB_API_TOKEN}` },
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getAllParsedNotPersistedProject(exactField = ""): Promise<IProjectSelectDataModel[]> {
        let projects: any = [];
        if (exactField) {
            projects = await ProjectModel.find({ name: { $not: { $regex: exactField } } });
        } else {
            projects = await ProjectModel.find({});
        }
        const { data } = await this.axiosInstance.get(this.GH_REPOS());
        return data
            .map((d: any) => ({ id: d.id, name: d.name }))
            .filter((d: IProjectSelectDataModel) => !projects.map((p: any) => p.id).includes(d.id))
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getRepoId(repoName: string): Promise<number> {
        const { data } = await this.axiosInstance.get(this.GH_REPO(repoName));
        return data.id;
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getAllUserProjects(repoNames: string[]): Promise<IGithubProjectDataApiModel[]> {
        const { data } = await this.axiosInstance.get(this.GH_REPOS());
        const { data: colorsData } = await this.axiosInstance.get(this.GH_COLORS());

        return data
            .filter((r: any) => repoNames.includes(r.name))
            .map((r: any) => {
                const langKey = Object.keys(colorsData).find(c => c.toLowerCase() === r.language.toLowerCase());
                const searchedColor = langKey ? colorsData[langKey] : "";
                const foundedColor = searchedColor ? searchedColor.color ? searchedColor.color : "" : "";
                return {
                    id: r.id,
                    htmlUrl: r.html_url,
                    description: r.description,
                    starsCount: r.stargazers_count,
                    watchersCount: r.watchers_count,
                    forksCount: r.forks_count,
                    primaryLanguage: r.language,
                    primaryLanguageColor: foundedColor
                };
            });
    };

                return new GithubProjectDataApiModel(
                    r.id, r.html_url, r.description, r.stargazers_count, r.watchers_count, r.forks_count, r.language,
                    foundedColor
                );
            });
    };
}

export default new GithubApi;

