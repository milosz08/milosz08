/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import mongoose, { Model, Schema } from 'mongoose';
import uniqueValidator from 'mongoose-unique-validator';
import dbValidators from '../validators.db';

export type SocialLink = {
  paraphrase: string;
  link: string;
  iconClass: string;
};

const SocialLinkSchema: Schema<SocialLink> = new Schema({
  paraphrase: {
    type: String,
    unique: true,
    required: [true, 'Paraphrase of social link is required.'],
    minlength: [10, 'Social link paraphrase must have at least 10 characters.'],
    maxlength: [
      150,
      'Social link paraphrase must have less or equal than 150 characters.',
    ],
  },
  link: {
    type: String,
    unique: true,
    required: [true, 'Link of social link is required.'],
    validate: [dbValidators.validateLink, 'Link is invalid.'],
  },
  iconClass: {
    type: String,
    required: [true, 'Icon of social link is required.'],
  },
});

SocialLinkSchema.plugin(uniqueValidator, {
  type: 'mongoose-unique-validator',
  message: 'Followed {PATH} already exist.',
});

export const SocialLinkModel: Model<SocialLink> = mongoose.model(
  'SocialLink',
  SocialLinkSchema
);
