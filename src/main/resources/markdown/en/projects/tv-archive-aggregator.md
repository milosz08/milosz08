---
title: TV Archive aggregator
type: normal
---

{{badge 'source code$github$808080$https://github.com/milosz08/tv-archive-aggregator'}}

## Project purpose

The main goal of this project is to improve the archiving system for TV programs and make them more easily searchable
for interesting content from a given time period.

This project consists of 3 sub-projects:

- *web-scrapper* - scrapping data from website and saving in DB (desktop Java Swing app),
- *data-server* - Rest API written in Spring Boot,
- *web-ui* - Web client written in React and MUI component library.

## Main features

- make availability to scrap content from telemagazyn.pl and saving in defined structure in MySQL database,
- provide web API for preexisting web client or other clients (for example mobile),
- provide web UI for searching content by program type, TV channel or genre with advanced search system and additional
  data visualization tools.

## Gallery

{{html 'projectGallery'}}

## Used technologies

- Java SE 17,
- Swing UI,
- Spring Boot 3,
- MySQL with JDBC Spring data,
- React with Tanstack Query,
- MUI components library.
