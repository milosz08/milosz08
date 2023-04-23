/*
 * Copyright (c) 2023 by MILOSZ GILGA <http://miloszgilga.pl>
 *
 * File name: ota-token-schema.ts
 * Last modified: 22/04/2023, 19:14
 * Project name: personal-website
 *
 * LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */

import mongoose, { Model, Schema, Types } from "mongoose";
import { IUser, UserModel } from "./user-schema";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export interface IOtaToken {
    token: string;
    expiredAt: Date;
    isExpired: boolean;
    user: IUser;
}

const OtaTokenSchema: Schema<IOtaToken> = new Schema<IOtaToken>({
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

export const OtaTokenModel: Model<IOtaToken> = mongoose.model("OtaToken", OtaTokenSchema);
