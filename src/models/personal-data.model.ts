/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: personal-data.model.ts
 * Last modified: 26/04/2023, 14:44
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

export interface IPersonalJsonData {
    description_top: string;
    description_bottom: string;
    maven_central_link: string;
    first_email: string;
    second_email: string;
    github_account_link: string;
    social_links: IPersonalLinkJsonData[];
    github_name: string;
    github_token: string;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export interface IPersonalLinkJsonData {
    name: string;
    paraphrase: string;
    link: string;
    icon_class: string;
}
