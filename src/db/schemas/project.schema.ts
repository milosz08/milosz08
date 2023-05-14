/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: project.schema.ts
 * Last modified: 30/04/2023, 01:37
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

export interface ITechStackPos {
    pos: number;
    name: string;
}

export interface IProject {
    id: number;
    position: number;
    name: string;
    alternativeName: string;
    externalLink: string;
    detailsDescription: string;
    techStackPositions: ITechStackPos[];
}

const ProjectSchema: Schema<IProject> = new Schema({
    id: {
        type: Number,
        unique: true,
        required: [ true, "Github project id field is required." ],
    },
    position: {
        type: Number,
        required: [ true, "Project position field is required." ],
    },
    name: {
        type: String,
        unique: true,
    },
    alternativeName: {
        type: String,
        unique: true,
        required: [ true, "Alternative name field is required." ],
        minlength: [ 2, "Alternative name must have at least 2 characters." ],
        maxlength: [ 150, "Alternative name must have less or equal 150 characters." ],
    },
    externalLink: {
        type: String,
        validate: [ dbValidators.validateLinkNotRequired, "Link is invalid." ],
    },
    detailsDescription: {
        type: String,
        minlength: [ 20, "Details description must have at least 20 characters." ],
        maxlength: [ 2000, "Details description must have less or equal 2000 characters." ],
        required: [ true, "Github project details description field is required." ],
    },
    techStackPositions: [
        {
            pos: {
                type: Number,
                required: true,
            },
            name: {
                type: String,
                minlength: [ 2, "Project tech stack name must have at least 5 characters." ],
                maxlength: [ 150, "Project tech stack name must have less or equal 150 characters." ],
                required: [ true, "Project tech stack name field is required." ],
            }
        }
    ],
});

ProjectSchema.plugin(uniqueValidator, { type: "mongoose-unique-validator", message: "Followed {PATH} already exist." });

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export const ProjectModel: Model<IProject> = mongoose.model("Project", ProjectSchema);
