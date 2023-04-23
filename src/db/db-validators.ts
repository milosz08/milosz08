/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: db-validators.ts
 * Last modified: 19/04/2023, 15:56
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import { ADMIN, EMAIL_REGEX, LOGIN_REGEX, MODERATOR } from "../utils/constants";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class DbValidators {

    validateEmail(email: string): boolean {
        return EMAIL_REGEX.test(email);
    };

    validateRole(role: string): boolean {
        return [ MODERATOR, ADMIN ].some(r => r === role.toUpperCase());
    };

    validateLogin(login: string): boolean {
        return LOGIN_REGEX.test(login);
    };
}

export default new DbValidators;
