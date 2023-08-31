'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: bootstrap-addons.js
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:50:34
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

const tooltips = document.querySelectorAll("[data-bs-toggle='tooltip']");

tooltips.forEach(el => new bootstrap.Tooltip(el));
