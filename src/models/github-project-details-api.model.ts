/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
