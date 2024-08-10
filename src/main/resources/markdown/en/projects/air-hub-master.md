---
title: AirHub Master (mobile game for managing aircraft fleet)
index: 4
type: 'part'
---

[![](<https://img.shields.io/badge/github-client%20(Android)-b07219?style=for-the-badge&logo=github>)](https://github.com/Lettulouz/AirHubMaster) &nbsp;
[![](<https://img.shields.io/badge/github-server%20(Java)-b07219?style=for-the-badge&logo=github>)](https://github.com/milosz08/air-hub-master-api) &nbsp;

Spring Boot Java server created for the [AirHub Master](https://github.com/Lettulouz/AirHubMaster) mobile application
with the Rest API specifications. Provide basic functionalities: signin, signup, password reset via email token and other
specific application requirements. All application data are stored in MySQL relational database. More info about this
project you will find [here](https://github.com/Lettulouz/AirHubMaster).

This application use newest stable [JMPS library](https://github.com/milosz08/jmpsl) (used modules: communication,
core, security).

## Gallery

<[GalleryContent}]>

## Multi-language support

This project has multi-language support with a simple Bash script available to create new _i18n_ resource bundle file in
project repository content. By default, application has polish and english support. To personalize language in HTTP
requests, you must provide _Accept-Language_ header with _i18n_ tag as a value. Changing the _Accept-Language_ header also
affects the generated language of the email content.

## Used technologies

- Java 17,
- Spring Boot 3,
- MySQL (production), H2 (development),
- Liquibase (database migrations),
- JMPSL (modules: communication, core, security),
- Jakarta Mail API + JMPSL Communication + Freemarker (mail support),
- Swagger OpenAPI documentation,
- Docker containers (only in development mode).
