/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: copy-assets.ts
 * Last modified: 14/04/2023, 17:13
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import * as shell from "shelljs";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

shell.cp("-R","src/views","dist/");
shell.cp("-R","src/templates","dist/");
shell.cp("-R","src/public","dist/");
