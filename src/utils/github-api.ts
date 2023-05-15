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

import { Request, Response } from "express";
import axios, { AxiosInstance } from "axios";

import logger from "./logger";
import { AlertTypeId } from "./session";
import * as Constant from "./constants";
import projectImages from "../files/project-images";

import { ProjectModel } from "../db/schemas/project.schema";
import { PersonalDataModel } from "../db/schemas/personal-data.schema";

import { IProjectSelectDataModel } from "../models/project-select-data.model";
import { IGithubProjectDataApiModel } from "../models/github-project-data-api.model";
import { IGithubProjectDetailsApiModel, ILanguage } from "../models/github-project-details-api.model";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GithubApi {

    private githubUsername: string = "";
    private axiosInstance: AxiosInstance | undefined = undefined;

    private GH_REPOS = () => `https://api.github.com/users/${this.githubUsername}/repos`;
    private GH_REPO = (repoName: string) => `https://api.github.com/repos/${this.githubUsername}/${repoName}`;
    private GH_REPO_LANGS = (repoName: string) => `https://api.github.com/repos/${this.githubUsername}/${repoName}/languages`;
    private GH_COLORS = () => "https://raw.githubusercontent.com/ozh/github-colors/master/colors.json";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    constructor() {
        PersonalDataModel.findOne().then(detailsModel => {
            if (!detailsModel) throw new Error("Could not find user details.");

            this.githubUsername = detailsModel.githubName;
            this.axiosInstance = axios.create({ headers: { "Authorization": `token ${detailsModel.githubToken}` } });
        });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getAllParsedNotPersistedProject(exactField = ""): Promise<IProjectSelectDataModel[]> {
        let projects: any = [];
        if (exactField) {
            projects = await ProjectModel.find({ name: { $not: { $regex: exactField } } });
        } else {
            projects = await ProjectModel.find({});
        }
        const { data } = await this.axiosInstance!.get(this.GH_REPOS());
        return data
            .map((d: any) => ({ id: d.id, name: d.name }))
            .filter((d: IProjectSelectDataModel) => !projects.map((p: any) => p.id).includes(d.id))
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getRepoId(repoName: string): Promise<number> {
        const { data } = await this.axiosInstance!.get(this.GH_REPO(repoName));
        return data.id;
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getAllUserProjects(repoNames: string[]): Promise<IGithubProjectDataApiModel[]> {
        const { data } = await this.axiosInstance!.get(this.GH_REPOS());
        const { data: colorsData } = await this.axiosInstance!.get(this.GH_COLORS());

        return data
            .filter((r: any) => repoNames.includes(r.name))
            .map((r: any) => {
                const langKey = Object.keys(colorsData).find(c => c.toLowerCase() === r.language?.toLowerCase());
                const searchedColor = langKey ? colorsData[langKey] : "";
                const foundedColor = searchedColor ? searchedColor.color ? searchedColor.color : "" : "";
                return {
                    id: r.id,
                    htmlUrl: r.html_url,
                    description: r.description,
                    starsCount: r.stargazers_count,
                    watchersCount: r.watchers_count,
                    forksCount: r.forks_count,
                    primaryLanguage: r.language || "Unknow",
                    primaryLanguageColor: foundedColor
                };
            });
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async getSingleProjectDetails(repoName: string): Promise<IGithubProjectDetailsApiModel> {
        const { data: projectData } = await this.axiosInstance!.get(this.GH_REPO(repoName));
        const { data: langData } = await this.axiosInstance!.get(this.GH_REPO_LANGS(repoName));
        const { data: colorsData } = await this.axiosInstance!.get(this.GH_COLORS());

        const allLanguagesCount = Object.values(langData).reduce((sum: number, langDataObj: any) => sum + langDataObj, 0);
        const lowerLangs = Object.keys(langData).map(l => l.toLowerCase());

        const allLanguagesColorKeys = Object.entries(colorsData)
            .filter(([ key, _ ]) => lowerLangs.includes(key.toLowerCase()))
            .map(([ key, value ]) => ({ name: key, color: (value as { color: string, url: string }).color }));

        const languages: ILanguage[] = Object.entries(langData).map(([ key, value ]) => ({
            name: key,
            color: allLanguagesColorKeys.find(c => c.name === key)!.color ?? "#ffffff",
            percentage: (Number(value) / allLanguagesCount) * 100,
        }));
        languages.sort((x, y) => y.percentage - x.percentage);
        return {
            githubLink: projectData.html_url,
            homepage: projectData.homepage ?? "",
            description: projectData.description,
            license: projectData.license ? projectData.license.name : "License not specified",
            repoCreated: projectData.created_at,
            repoUpdated: projectData.updated_at,
            cloneUrl: projectData.clone_url,
            altCloneUrl: `$ git clone ${projectData.svn_url}`,
            topics: projectData.topics,
            forksCount: projectData.forks_count,
            watchersCount: projectData.watchers_count,
            starsCount: projectData.stargazers_count,
            languages,
        };
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async deleteProjectAndMoveCursor(objectId: string): Promise<void> {
        const remainingElements = await ProjectModel
            .findByIdAndUpdate(
                { _id: objectId },
                { $pull: { arrayField: { _id: objectId } } },
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
        await ProjectModel.findByIdAndRemove(objectId);
        await projectImages.deleteAllProjectImages(objectId);
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async deleteAlreadyRemovedProject(
        req: Request, res: Response, name: string, projectId: string = "", removable: boolean = false
    ): Promise<boolean> {
        try {
            await this.getSingleProjectDetails(name);
            return false;
        } catch (ex: any) {
            if (removable) {
                await this.deleteProjectAndMoveCursor(projectId);
            }
            req.session[AlertTypeId.CMS_PROJECTS_PAGE] = {
                type: Constant.ALERT_SUCCESS,
                message: `Project <strong>${name}</strong> was already removed from Github account.`,
            };
            logger.info(`Deleted already removed github project with id: '${projectId}'`);
            return true;
        }
    };
}

export default new GithubApi;

