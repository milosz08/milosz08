/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: ota-token.schema.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:48:35
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import mongoose, { Model, Schema, Types } from 'mongoose';
import { User, UserModel } from './user.schema';

export type OtaToken = {
  token: string;
  expiredAt: Date;
  isExpired: boolean;
  user: User;
};

const OtaTokenSchema: Schema<OtaToken> = new Schema<OtaToken>({
  token: {
    type: String,
    required: true,
    unique: true,
    maxlength: 10,
  },
  expiredAt: {
    type: Date,
    required: true,
  },
  isExpired: {
    type: Boolean,
    required: true,
    default: false,
  },
  user: {
    type: Types.ObjectId,
    required: true,
    ref: UserModel,
  },
});

export const OtaTokenModel: Model<OtaToken> = mongoose.model(
  'OtaToken',
  OtaTokenSchema
);
