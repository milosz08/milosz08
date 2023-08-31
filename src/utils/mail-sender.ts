/*
 * Copyright (c) 2023 by MILOSZ GILGA <https://miloszgilga.pl>
 *
 *   File name: mail-sender.ts
 *   Created at: 2023-05-29, 01:50:56
 *   Last updated at: 2023-08-31, 19:53:08
 *   Project name: <<msph_projectName>>
 *
 *   LICENSE NOT SPECIFIED.
 *
 * For more info about use this code in your project, make contact with
 * original author. Project created only for personal purposes.
 */
import * as fs from 'fs';
import * as handlebars from 'handlebars';
import * as nodemailer from 'nodemailer';
import { Options } from 'nodemailer/lib/mailer';
import config from './config';
import utilities from './utilities';

export type EmailReplacements = { [key: string]: string };

class MailSender {
  private readonly TEMPLATES_PATH = '/templates';

  async sendEmail(
    sendTo: string,
    subject: string,
    template: string,
    replacements: EmailReplacements
  ): Promise<void> {
    const transporter = nodemailer.createTransport({
      host: config.SMTP_HOST,
      port: Number(config.SMTP_PORT),
      secure:
        typeof config.SMTP_SSL === 'boolean'
          ? config.SMTP_SSL
          : /^true$/i.test(config.SMTP_SSL),
      auth: {
        user: config.SMTP_USERNAME,
        pass: config.SMTP_PASSWORD,
      },
    });

    replacements.currentYear = String(new Date().getFullYear());

    const mailOptions: Options = {
      from: `"${config.SMTP_HOST}" <${config.SMTP_HOST}>`,
      to: sendTo,
      subject: `Milosz Gilga CMS | ${subject}`,
      html: this.generateTemplate(template, replacements),
    };
    await transporter.sendMail(mailOptions);
  }

  private generateTemplate(
    templateName: string,
    replacements: EmailReplacements
  ): string {
    const source = fs
      .readFileSync(
        utilities.getProjectRootPath(
          this.TEMPLATES_PATH + '/' + templateName + '.html'
        )
      )
      .toString();
    const bufferedTemplate = handlebars.compile(source);
    return bufferedTemplate(replacements);
  }
}

export default new MailSender();
