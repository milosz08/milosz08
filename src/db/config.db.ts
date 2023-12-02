/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 * For check application license, check LICENSE file.
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
