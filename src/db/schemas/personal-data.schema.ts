/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: personal-data.schema.ts
 * Last modified: 26/04/2023, 13:47
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import mongoose, { Model, Schema } from "mongoose";
import dbValidators from "../validators.db";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export interface IPersonalData {
    descriptionTop: string;
    descriptionBottom: string;
    mavenCentralLink: string;
    firstEmail: string;
    secondEmail: string;
    githubAccountLink: string;
}

const PersonalDataSchema: Schema<IPersonalData> = new Schema({
    descriptionTop: {
        type: String,
        required: [ true, "First description section is required." ],
        minlength: [ 10, "First description section must have at least 3 characters." ],
        maxlength: [ 500, "First description section must have less or equal 500 characters." ],
    },
    mavenCentralLink: {
        type: String,
        required: [ true, "Maven central link is required." ],
        validate: [ dbValidators.validateLink, "Link is invalid." ],
    },
    descriptionBottom: {
        type: String,
        required: [ true, "Second description section is required." ],
        minlength: [ 10, "Second description section must have at least 3 characters." ],
        maxlength: [ 500, "Second description section must have less or equal 500 characters." ],
    },
    firstEmail: {
        type: String,
        required: [ true, "First email is required." ],
        validate: [ dbValidators.validateEmail, "Email is invalid." ],
    },
    secondEmail: {
        type: String,
        required: [ true, "Alternative email is required." ],
        validate: [ dbValidators.validateEmail, "Alternative email is invalid." ],
    },
    githubAccountLink: {
        type: String,
        required: [ true, "Github account link is required." ],
        validate: [ dbValidators.validateLink, "Link is invalid." ],
    },
});

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export const PersonalDataModel: Model<IPersonalData> = mongoose.model("PersonalData", PersonalDataSchema);
