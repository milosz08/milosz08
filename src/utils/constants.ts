/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */

// layouts
export const EMPTY_LAYOUT = 'layouts/empty-layout';
export const CMS_LAYOUT = 'layouts/cms-layout';
export const DEFAULT_LAYOUT = 'layouts/default-layout';

// views
export const CMS_ACCOUNT_ACCOUNTS_EJS = {
  title: 'Accounts',
  path: 'account/accounts',
  layout: CMS_LAYOUT,
};
export const CMS_ACCOUNT_ADD_ACCOUNT_EJS = {
  title: 'Add account',
  path: 'account/add-update-account',
  layout: CMS_LAYOUT,
};
export const CMS_ACCOUNT_UPDATE_ACCOUNT_EJS = {
  title: 'Update account',
  path: 'account/add-update-account',
  layout: CMS_LAYOUT,
};

export const AUTH_CHANGE_PASSWORD_EJS = {
  title: 'Change password',
  path: 'auth/change-password',
  layout: EMPTY_LAYOUT,
};
export const AUTH_REQUEST_CHANGE_PASSWORD_EJS = {
  title: 'Request change password',
  path: 'auth/request-change-password',
  layout: EMPTY_LAYOUT,
};
export const AUTH_LOGIN_EJS = {
  title: 'Login',
  path: 'auth/login',
  layout: EMPTY_LAYOUT,
};
export const AUTH_FIRST_LOGIN_EJS = {
  title: 'First login',
  path: 'auth/first-login',
  layout: EMPTY_LAYOUT,
};

export const CMS_PROJECTS_EJS = {
  title: 'Projects',
  path: 'cms/projects',
  layout: CMS_LAYOUT,
};
export const CMS_ADD_PROJECT_EJS = {
  title: 'Add project',
  path: 'cms/add-update-project',
  layout: CMS_LAYOUT,
};
export const CMS_UPDATE_PROJECT_EJS = {
  title: 'Update project',
  path: 'cms/add-update-project',
  layout: CMS_LAYOUT,
};
export const CMS_PERSONAL_DATA_EJS = {
  title: 'Personal data',
  path: 'cms/personal-data',
  layout: CMS_LAYOUT,
};
export const CMS_SOCIAL_LINKS_EJS = {
  title: 'Social links',
  path: 'cms/social-links',
  layout: CMS_LAYOUT,
};
export const CMS_ADD_SOCIAL_LINKS_EJS = {
  title: 'Add social link',
  path: 'cms/add-update-social-link',
  layout: CMS_LAYOUT,
};
export const CMS_UPDATE_SOCIAL_LINKS_EJS = {
  title: 'Update social link',
  path: 'cms/add-update-social-link',
  layout: CMS_LAYOUT,
};

export const PUBLIC_PROJECTS_EJS = {
  title: 'Projects',
  path: 'public/projects',
};
export const PUBLIC_PROJECT_DETAILS_EJS = { path: 'public/project-details' };

// roles
export const ADMIN = 'ADMIN';
export const MODERATOR = 'MODERATOR';

// alert
export const ALERT_SUCCESS = 'alert-success';
export const ALERT_WARN = 'alert-warning';
export const ALERT_DANGER = 'alert-danger';

// regex
export const EMAIL_REGEX = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;
export const PASSWORD_REGEX =
  /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
export const LOGIN_REGEX = /^[a-zA-Z\d]{3,30}$/;
export const LINK_REGEX = /^(http|https):\/\/[^ "]+$/;

// email templates
export const CHANGE_PASSWORD_MAIL_TEMPLATE = 'change-password-email';
export const AFTER_CREATED_ACCOUNT_MAIL_TEMPLATE =
  'after-created-account-email';
export const AFTER_REVOKED_PASSWORD_MAIL_TEMPLATE =
  'after-revoked-password-email';

// pagination
export const PAGINATION_STATES = [5, 10, 15, 50, 100];
