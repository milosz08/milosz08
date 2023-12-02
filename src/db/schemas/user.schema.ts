/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
 */
import * as bcrypt from 'bcrypt';
import mongoose, { Model, Schema } from 'mongoose';
import uniqueValidator from 'mongoose-unique-validator';
import dbMiddleware from '../middlewares.db';
import dbValidators from '../validators.db';

export type User = {
  login: string;
  email: string;
  password: string;
  role: string;
  firstLogin: boolean;

  compareHash(password: string): boolean;
};

const UserSchema: Schema<User> = new Schema<User>({
  login: {
    type: String,
    lowercase: true,
    trim: true,
    required: true,
    unique: true,
    maxlength: [30, 'Login must have less or equals 30 characters.'],
    validate: [
      dbValidators.validateLogin,
      'Login must contains only letters and numbers.',
    ],
  },
  email: {
    type: String,
    lowercase: true,
    trim: true,
    required: [true, 'Email field is required.'],
    unique: true,
    validate: [dbValidators.validateEmail, 'Incorrect email format.'],
  },
  password: {
    type: String,
    required: [true, 'Password field is required.'],
    minlength: [4, 'Password field must have at least 4 characters.'],
  },
  role: {
    type: String,
    uppercase: true,
    validate: [dbValidators.validateRole, 'Role must be ADMIN or MODERATOR'],
  },
  firstLogin: {
    type: Boolean,
    default: true,
    required: true,
  },
});

UserSchema.plugin(uniqueValidator, {
  type: 'mongoose-unique-validator',
  message: 'Followed {PATH} already exist.',
});
UserSchema.path('password').set(dbMiddleware.hashPassword);

UserSchema.methods = {
  compareHash(password: string): boolean {
    return bcrypt.compareSync(password, this.password);
  },
};

export const UserModel: Model<User> = mongoose.model('User', UserSchema);
