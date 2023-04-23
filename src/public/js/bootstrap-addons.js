/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: bootstrap-addons.js
 * Last modified: 23/04/2023, 23:51
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

const tooltips = document.querySelectorAll("[data-bs-toggle='tooltip']");

tooltips.forEach(el => new bootstrap.Tooltip(el));
