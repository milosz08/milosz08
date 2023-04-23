/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: constants.ts
 * Last modified: 19/04/2023, 12:55
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

// layouts
export const EMPTY_LAYOUT                           = "layouts/empty-layout";
export const CMS_LAYOUT                             = "layouts/cms-layout";
export const DEFAULT_LAYOUT                         = "layouts/default-layout";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// views
export const ACCOUNT_ACCOUNTS_EJS                   = { title: "Accounts", path: "account/accounts", layout: CMS_LAYOUT };
export const ACCOUNT_ADD_ACCOUNT_EJS                = { title: "Add account", path: "account/add-update-account", layout: CMS_LAYOUT };
export const ACCOUNT_UPDATE_ACCOUNT_EJS             = { title: "Update account", path: "account/add-update-account", layout: CMS_LAYOUT };

export const AUTH_CHANGE_PASSWORD_EJS               = { title: "Change password", path: "auth/change-password", layout: EMPTY_LAYOUT };
export const AUTH_REQUEST_CHANGE_PASSWORD_EJS       = { title: "Request change password", path: "auth/request-change-password", layout: EMPTY_LAYOUT };
export const AUTH_LOGIN_EJS                         = { title: "Login", path: "auth/login", layout: EMPTY_LAYOUT };
export const AUTH_FIRST_LOGIN_EJS                   = { title: "First login", path: "auth/first-login", layout: EMPTY_LAYOUT };

export const CMS_PROJECTS_EJS                       = { title: "Projects", path: "cms/projects", layout: CMS_LAYOUT };
export const CMS_ADD_PROJECT_EJS                    = { title: "Add project", path: "cms/add-update-project", layout: CMS_LAYOUT };
export const CMS_UPDATE_PROJECT_EJS                 = { title: "Update project", path: "cms/add-update-project", layout: CMS_LAYOUT };
export const CMS_PERSONAL_DATA_EJS                  = { title: "Personal", path: "cms/personal-data", layout: CMS_LAYOUT };

export const PUBLIC_PROJECTS_EJS                    = { title: "Projects", path: "public/projects" };
export const PUBLIC_PROJECT_DETAILS_EJS             = { path: "public/project-details" };

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// roles
export const ADMIN                                  = "ADMIN";
export const MODERATOR                              = "MODERATOR";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// alert
export const ALERT_SUCCESS                          = "alert-success";
export const ALERT_WARN                             = "alert-warning";
export const ALERT_DANGER                           = "alert-danger";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// regex
export const PASSWORD_REGEX                         = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{8,}$/;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// email templates
export const CHANGE_PASSWORD_MAIL_TEMPLATE          = "change-password-email";
