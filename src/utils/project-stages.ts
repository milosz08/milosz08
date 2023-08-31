/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: project-stages.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:53:16
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

export type ProjectStage = {
  name: string;
  slug: string;
};

export const PROJECT_STAGES: ProjectStage[] = [
  {
    name: 'Early dev stage',
    slug: 'early-dev-stage',
  },
  {
    name: 'In progress',
    slug: 'in-progress',
  },
  {
    name: 'Finished',
    slug: 'finished',
  },
];
