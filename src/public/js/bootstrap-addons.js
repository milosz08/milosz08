'use strict';
/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */

const tooltips = document.querySelectorAll("[data-bs-toggle='tooltip']");

tooltips.forEach(el => new bootstrap.Tooltip(el));
