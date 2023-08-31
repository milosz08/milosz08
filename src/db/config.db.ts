/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: config.db.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:48:56
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import mongoose from 'mongoose';
import config from '../utils/config';
import logger from '../utils/logger';
import dbRunners from './runners.db';

export default function (): void {
  mongoose.connect(config.CONNECTION_STRING).then(() => {
    logger.info('Successfully connected into the database');
  });
  dbRunners.createDefaultUser().then(() => {});
}
