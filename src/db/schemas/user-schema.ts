/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: user-schema.ts
 * Last modified: 19/04/2023, 15:27
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import mongoose, { Schema, Model } from "mongoose";
import uniqueValidator from "mongoose-unique-validator";
import bcrypt from "bcrypt";

import dbValidators from "../db-validators";
import dbMiddleware from "../db-middlewares";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

interface User {
    login: string;
    email: string;
    password: string;
    role: string;
    firstLogin: boolean;

    compareHash(password: string): boolean;
}

const UserSchema: Schema<User> = new Schema<User>({
    login: {
        type: String,
        lowercase: true,
        trim: true,
        required: true,
        unique: true,
        maxlength: [ 30, "Login must have less or equals 30 characters." ]
    },
    email: {
        type: String,
        lowercase: true,
        trim: true,
        required: [ true, "Email field is required." ],
        unique: true,
        validate: [ dbValidators.validateEmail, "Incorrect email format." ]
    },
    password: {
        type: String,
        required: [ true, "Password field is required." ],
        minlength: [ 4, "Password field must have at least 4 characters." ],
    },
    role: {
        type: String,
        uppercase: true,
        validate: [ dbValidators.validateRole, "Role must be ADMIN or MODERATOR" ],
    },
    firstLogin: {
        type: Boolean,
        default: true,
        required: true,
    },
});

UserSchema.plugin(uniqueValidator, "Error, user with followed {PATH} already exist.");
UserSchema.path("password").set(dbMiddleware.hashPassword);

UserSchema.methods = {
    compareHash(password: string): boolean {
        return bcrypt.compareSync(password, this.password);
    },
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export const UserModel: Model<User> = mongoose.model("User", UserSchema);
