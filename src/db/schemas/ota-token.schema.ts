/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
