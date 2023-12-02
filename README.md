# Personal website

![](https://img.shields.io/badge/Made%20in-NodeJS%2018.12.1-1abc.svg)
&nbsp;&nbsp;
![](https://img.shields.io/badge/Using%20-TypeScript-green.svg)
&nbsp;&nbsp;
![](https://img.shields.io/badge/Packages%20manager-npm-brown.svg)
&nbsp;&nbsp;
<br>
> More info about this project you will find [on my personal website](https://miloszgilga.pl/project/personal-website)
> <br>
> See project at [miloszgilga.pl](https://miloszgilga.pl)

Personal website created using NodeJS environment with Express framework, written in TypeScript language. Includes a simple
CMS in order to add/edit/delete projects and modify other website elements. Additional informations about selected projects
are pulled dynamically from the Github API.

## Table of content
- [Clone script](#clone-script)
- [Prepare and run](#prepare-and-run)
- [Tech stack](#tech-stack)
- [Author](#author)
- [Project status](#project-status)
- [License](#license)

<a name="clone-script"></a>
## Clone script
To install the program on your computer use the command (or use the built-in GIT system in your IDE environment):
```
$ git clone https://github.com/Milosz08/personal-website personal-website
```

<a name="prepare-and-run"></a>
## Prepare and run
0. Before you run program, create `.env` file via this command:
```
$ grep -vE '^\s*$|^#' example.env > .env
```
and fill with propriet values:
```properties
# server configuration
EXPRESS_PORT              = [application port, ex. 3031]
NODE_ENV                  = ['development' or 'production']
SESSION_KEY               = [session key, ex. 'vtg290d203dkd0kd09kdgghhmnkj57jh4hg3fgv5h65h6h']

# default admin user
DEF_USER_LOGIN            = [default user login, ex. 'admin']
DEF_USER_EMAIL            = [default user email, ex. 'admin@example.com']
DEF_USER_PASSWORD         = [default user password, ex. 'StrongPassword321@']

# database
CONNECTION_STRING         = mongodb://[dbUsername]:[dbPassword]@[dbHost]:[dbPort]

# smtp server
SMTP_HOST                 = [smtp host server, ex. 'example.net']
SMTP_USERNAME             = [smtp sender email, ex. 'noreply@example.net']
SMTP_PASSWORD             = [smtp password, ex. 'SuperSecretPassword123']
SMTP_SSL                  = [true if SSL is active, otherwise false]
SMTP_PORT                 = [587 if SSL is active, otherwise 25]
```
1. Install all dependencies via:
```
$ npm install
```
2. Run project (compile TS files and move static files into `dist` directory) - listening all changes and hotreload:
```
$ npm run dev:start
```
2. To create production build, type:
```
$ npm run build
```
All compiled files will be in `dist` directory.

<a name="tech-stack"></a>
## Tech stack
* NodeJS 18.12.1
* TypeScript
* ExpressJS (main framework)
* EJS (views), Handlebars (email templates)
* MongoDB with Mongoose (database)
* Nodemailer (email sender)
* Multer (files management)

<a name="author"></a>
## Author
Created by Miłosz Gilga. If you have any questions about this application, send message: [personal@miloszgilga.pl](mailto:personal@miloszgilga.pl).

<a name="project-status"></a>
## Project status
Project is finished.

<a name="license"></a>
## License
LICENSE NOT SPECIFIED.<br>
For more info about use this code in your project, make contact with original author. Project created only for personal 
purposes.
