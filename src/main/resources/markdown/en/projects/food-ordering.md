---
title: Food ordering web application
index: 3
type: 'part'
---

[![](https://img.shields.io/badge/github-808080?style=for-the-badge&logo=github)](https://github.com/milosz08/food-ordering-web-app) &nbsp;

A CMS (Content Management System) web application to support restaurant management, customer contact and product ordering
from created restaurants. For obvious reasons, the application does not have a payment system.

## Gallery

<[GalleryContent}]>

## Business design

This application allows to create a user and restaurant owner account. User can add new products from multiple restaurants
to the cart and the owner (after the system administrator approves the created restaurant) can add, modify and remove
dishes from the created restaurant.

## Technology design

The main core of the MVC application was written from scratch using pure PHP language without using any additional
libraries primarily for performance reasons. The application works with MySQL database version 7.4 and higher.

My role in this project was to select the technology, write the core of the application, and successively inspect the
delivered code and verify verify that it works properly. It was also not uncommon for me to participate in the development
of new functionality.

## Used technologies

- PHP 7.4,
- Mustache Template Engine,
- PHP Mailer,
- Bootstrap,
- Chart.js,
- jQuery.
