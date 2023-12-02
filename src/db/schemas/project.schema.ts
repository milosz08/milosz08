/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import mongoose, { Model, Schema } from 'mongoose';
import uniqueValidator from 'mongoose-unique-validator';
import dbValidators from '../validators.db';

export type TechStackPos = {
  pos: number;
  name: string;
};

export type Project = {
  id: number;
  position: number;
  name: string;
  stage: string;
  alternativeName: string;
  externalLink: string;
  detailsDescription: string;
  techStackPositions: TechStackPos[];
};

const ProjectSchema: Schema<Project> = new Schema({
  id: {
    type: Number,
    unique: true,
    required: [true, 'Github project id field is required.'],
  },
  position: {
    type: Number,
    required: [true, 'Project position field is required.'],
  },
  name: {
    type: String,
    unique: true,
  },
  stage: {
    type: String,
    required: [true, 'Project stage field is required.'],
    validate: [dbValidators.validateStage, 'Invalid stage name'],
  },
  alternativeName: {
    type: String,
    unique: true,
    required: [true, 'Alternative name field is required.'],
    minlength: [2, 'Alternative name must have at least 2 characters.'],
    maxlength: [
      150,
      'Alternative name must have less or equal 150 characters.',
    ],
  },
  externalLink: {
    type: String,
    validate: [dbValidators.validateLinkNotRequired, 'Link is invalid.'],
  },
  detailsDescription: {
    type: String,
    minlength: [20, 'Details description must have at least 20 characters.'],
    maxlength: [
      2000,
      'Details description must have less or equal 2000 characters.',
    ],
    required: [true, 'Github project details description field is required.'],
  },
  techStackPositions: [
    {
      pos: {
        type: Number,
        required: true,
      },
      name: {
        type: String,
        minlength: [
          2,
          'Project tech stack name must have at least 5 characters.',
        ],
        maxlength: [
          150,
          'Project tech stack name must have less or equal 150 characters.',
        ],
        required: [true, 'Project tech stack name field is required.'],
      },
    },
  ],
});

ProjectSchema.plugin(uniqueValidator, {
  type: 'mongoose-unique-validator',
  message: 'Followed {PATH} already exist.',
});

export const ProjectModel: Model<Project> = mongoose.model(
  'Project',
  ProjectSchema
);
