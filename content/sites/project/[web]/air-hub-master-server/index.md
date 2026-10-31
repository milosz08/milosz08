---
pageName: "${title}"
pageTitle: "${title} | ${globals.name}"
pageDescription: "${summary}"
listable: true
#
title: "AirHubMaster server"
kind: "Rest API"
summary: "Rest API for AirHubMaster android application. Support i18n standards. Written in Java 17 and Spring Boot 3."
tech:
  - "Java"
  - "SpringBoot"
  - "MySQL"
techDetails:
  - "Java 17"
  - "Spring Boot 3"
  - "MySQL (production), H2 (development)"
  - "Liquibase (database migrations)"
  - "JMPSL (modules: communication, core, security)"
  - "Jakarta Mail API + JMPSL Communication + Freemarker (mail support)"
  - "Swagger OpenAPI documentation"
  - "Docker containers (only in development mode)"
status: "completed"
pinned: false
weight: 0
repoUrls:
  - name: "Server"
    url: "https://github.com/milosz08/air-hub-master-server"
  - name: "Client"
    url: "https://github.com/Lettulouz/AirHubMaster"
downloadUrls:
  - name: "Docker image"
    url: "https://hub.docker.com/r/milosz08/air-hub-master-server"
---

Spring Boot Java server created for the [AirHub Master](https://github.com/Lettulouz/AirHubMaster) mobile application
with the Rest API specifications. Provide basic functionalities: signing, password reset via email token and other
specific application requirements. All application data are stored in MySQL relational database. More info about this
project you will find [here](https://github.com/Lettulouz/AirHubMaster).

This application use the newest stable [JMPS library](https://github.com/milosz08/jmpsl) (used modules: communication,
core, security).

## Multi-language support

This project has multi-language support with a simple Bash script available to create new _i18n_ resource bundle file in
project repository content. By default, application has Polish and English support. To personalize language in HTTP
requests, you must provide _Accept-Language_ header with _i18n_ tag as a value. Changing the _Accept-Language_ header
also affects the generated language of the email content.
