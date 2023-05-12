/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: github-project-details-api.model.ts
 * Last modified: 12/05/2023, 06:04
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

export interface ILanguage {
    name: string;
    color: string;
    percentage: number;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export interface IGithubProjectDetailsApiModel {
    githubLink: string;
    homepage: string;
    description: string;
    license: string;
    repoCreated: string;
    repoUpdated: string;
    cloneUrl: string;
    altCloneUrl: string;
    topics: string[];
    forksCount: number;
    watchersCount: number;
    starsCount: number;
    languages: ILanguage[];
}
