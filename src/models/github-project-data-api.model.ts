/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: github-project-data.model.ts
 * Last modified: 08/05/2023, 04:39
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

export class GithubProjectDataApiModel {
    id: number;
    htmlUrl: string;
    description: string;
    starsCount: number;
    watchersCount: number;
    forksCount: number;
    primaryLanguage: string;
    primaryLanguageColor: string;

    constructor(
        id: number, htmlUrl: string, description: string, starsCount: number, watchersCount: number, forksCount: number,
        primaryLanguage: string, primaryLanguageColor: string,
    ) {
        this.id = id;
        this.htmlUrl = htmlUrl;
        this.description = description;
        this.starsCount = starsCount;
        this.watchersCount = watchersCount;
        this.forksCount = forksCount;
        this.primaryLanguage = primaryLanguage;
        this.primaryLanguageColor = primaryLanguageColor;
    };
}
