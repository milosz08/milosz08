---
pageName: "${title}"
pageTitle: "${title} | ${globals.name}"
pageDescription: "${summary}"
listable: true
#
title: "Food ordering web app"
kind: "Web app"
summary: "Full stack web application written in PHP with embedded MVC framework created from scratch."
tech:
  - "PHP"
  - "MySQL"
  - "Handlebars"
techDetails:
  - "PHP 7.4"
  - "Mustache template engine"
  - "PHP Mailer"
  - "Bootstrap"
  - "Chart.js"
  - "jQuery"
status: "completed"
pinned: true
weight: 0
repoUrls:
  - url: "https://github.com/milosz08/food-ordering-web-app"
downloadUrls:
  - name: "Docker image"
    url: "https://hub.docker.com/r/milosz08/food-ordering-web-app"
---

A CMS (Content Management System) web application to support restaurant management, customer contact and product
ordering from created restaurants. For obvious reasons, the application does not have a payment system.

## Business design

This application allows to create a user and restaurant owner account. User can add new products from multiple
restaurants to the cart and the owner (after the system administrator approves the created restaurant) can add, modify
and remove dishes from the created restaurant.

## Technology design

The main core of the MVC application was written from scratch using pure PHP language without using any additional
libraries primarily for performance reasons. The application works with MySQL database version 7.4 and higher.

My role in this project was to select the technology, write the core of the application, and successively inspect the
delivered code and verify verify that it works properly. It was also not uncommon for me to participate in the
development of new functionality.
