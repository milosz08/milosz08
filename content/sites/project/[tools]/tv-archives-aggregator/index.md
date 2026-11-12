---
pageName: "${title}"
pageTitle: "${title} | ${globals.name}"
pageDescription: "${summary}"
listable: true
#
title: "TV archives aggregator"
kind: "Data aggregator"
summary: "TV archives aggregator scraping content from \"telemagazyn.pl\" and indexing it with more comfortable search system."
tech:
  - "Java"
  - "SpringBoot"
  - "Swing"
  - "Playwright"
  - "React"
  - "Typescript"
  - "MUI"
techDetails:
  - "Java SE 17"
  - "Swing UI"
  - "Spring Boot 3"
  - "MySQL with JDBC Spring data"
  - "React with TanStack Query"
  - "MUI components library"
status: "maintained"
pinned: true
weight: 0
repoUrls:
  - url: "https://github.com/c0ifl4x/tv-archives-aggregator"
downloadUrls:
  - name: "Data extractor"
    url: "https://github.com/c0ifl4x/tv-archives-aggregator/releases/latest"
  - name: "Docker image (data visualiser)"
    url: "https://hub.docker.com/r/milosz08/tv-archives-aggregator-client"
---

The main goal of this project is to improve the archiving system for TV programs and make them more easily searchable
for interesting content from a given time period.

This project consists of 3 subprojects:

- **app-scrapper** - scrapping data from website and saving in DB (desktop Java Swing app),
- **app-backend** - Rest API written in Spring Boot,
- **app-frontend** - Web client written in React and MUI component library.

## Main features

- make availability to scrap content from telemagazyn.pl and saving in defined structure in MySQL database,
- provide web API for preexisting web client or other clients (for example mobile),
- provide web UI for searching content by program type, TV channel or genre with advanced search system and additional
  data visualization tools.
