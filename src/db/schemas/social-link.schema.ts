/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: social-link.schema.ts
 * Last modified: 26/04/2023, 13:48
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import mongoose, { Model, Schema } from "mongoose";
import uniqueValidator from "mongoose-unique-validator";

import dbValidators from "../validators.db";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export interface ISocialLink {
    paraphrase: string;
    link: string;
    iconClass: string;
}

const SocialLinkSchema: Schema<ISocialLink> = new Schema({
    paraphrase: {
        type: String,
        unique: true,
        required: [ true, "Paraphrase of social link is required." ],
        minlength: [ 10, "Social link paraphrase must have at least 10 characters." ],
        maxlength: [ 150, "Social link paraphrase must have less or equal than 150 characters." ],
    },
    link: {
        type: String,
        unique: true,
        required: [ true, "Link of social link is required." ],
        validate: [ dbValidators.validateLink, "Link is invalid." ],
    },
    iconClass: {
        type: String,
        required: [ true, "Icon of social link is required." ],
    },
});

SocialLinkSchema.plugin(uniqueValidator, { type: "mongoose-unique-validator", message: "Followed {PATH} already exist." });

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export const SocialLinkModel: Model<ISocialLink> = mongoose.model("SocialLink", SocialLinkSchema);
