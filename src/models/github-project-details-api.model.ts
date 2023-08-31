/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: github-project-details-api.model.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:49:54
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

export type Language = {
  name: string;
  color: string;
  percentage: number;
};

export type GithubProjectDetailsApiModel = {
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
  languages: Language[];
};
