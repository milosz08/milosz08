/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: personal-data.model.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:50:01
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

export type PersonalJsonData = {
  description_top: string;
  description_bottom: string;
  maven_central_link: string;
  first_email: string;
  second_email: string;
  github_account_link: string;
  social_links: PersonalLinkJsonData[];
  github_name: string;
  github_token: string;
};

export type PersonalLinkJsonData = {
  name: string;
  paraphrase: string;
  link: string;
  icon_class: string;
};
