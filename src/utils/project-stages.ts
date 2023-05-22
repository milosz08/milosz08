/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: project-stages.ts
 * Last modified: 22/05/2023, 18:48
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

export interface IProjectStage {
    name: string;
    slug: string;
}

export const PROJECT_STAGES: IProjectStage[] = [
    {
        name: "Early dev stage",
        slug: "early-dev-stage"
    },
    {
        name: "In progress",
        slug: "in-progress"
    },
    {
        name: "Finished",
        slug: "finished"
    }
];
