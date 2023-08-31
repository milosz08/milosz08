/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: github-project-data-api.model.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:49:51
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

export type GithubProjectDataApiModel = {
  id: number;
  htmlUrl: string;
  description: string;
  starsCount: number;
  watchersCount: number;
  forksCount: number;
  primaryLanguage: string;
  primaryLanguageColor: string;
};
