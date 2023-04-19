/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: logger.ts
 * Last modified: 19/04/2023, 14:03
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import winston, { Logger, Logform, transport } from 'winston';

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class WinstonLogger {

    private readonly LEVELS = { error: 0, warn: 1, info: 2, http: 3, debug: 4 };
    private readonly COLORS = { error: "red", warn: "yellow", info: "green", http: "magenta", debug: "white" };
    private readonly LOGS_PATH = "logs";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private getLevel(): string {
        const env = process.env.NODE_ENV || "development";
        const isDevelopment = env === "development";
        return isDevelopment ? "debug" : "warn";
    };

    private getFormat(): Logform.Format {
        return winston.format.combine(
            winston.format.timestamp({ format: "YYYY-MM-DD HH:mm:ss:ms" }),
            winston.format.colorize({ all: true }),
            winston.format.printf(info => `${info.timestamp} ${info.level}: ${info.message}`,
        ));
    };

    private getTransports(): Array<transport> {
        return [
            new winston.transports.Console(),
            new winston.transports.File({ filename: `${this.LOGS_PATH}/error.log`, level: "error" }),
            new winston.transports.File({ filename: `${this.LOGS_PATH}/all.log` }),
        ];
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    getLogger(): Logger {
        winston.addColors(this.COLORS);
        return winston.createLogger({
            level: this.getLevel(),
            levels: this.LEVELS,
            format: this.getFormat(),
            transports: this.getTransports(),
        });
    };
}

export default new WinstonLogger().getLogger();
