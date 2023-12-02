/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
