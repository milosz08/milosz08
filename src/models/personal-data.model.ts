/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
