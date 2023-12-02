/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
