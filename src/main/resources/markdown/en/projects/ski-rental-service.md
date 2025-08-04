---
title: Ski rental service
type: normal
---

{{badge 'source code$github$232925$https://github.com/milosz08/ski-rental-service'}}
{{badge 'demo$applearcade$C70039$https://ski.miloszgilga.pl'}}

Enterprise-class Java web application for ski rental company. This application consists of 2 modules, where 1 is the
owner module and the other is the employee module.

The **owner's module** can:

- add/change/remove employee accounts (automatically generated email address, login details sent to the employee's
  mailbox)
- add/change/remove ski equipment (count of equipments, attributes, rental price etc.)
- see all rentals and returns made by all employees

The **employeer's module** can:

- browse ski equipment added by owner
- add/change/remove customers to the database
- create rentals and returns of previous saved customers

## Gallery

{{html 'projectGallery'}}

## Business design

After creating a rental, program automatically calculates the price based on the number of days of rental (set by the
owner for each ski equipment - different conversion rates for days and others for hours). After creating a rent, a PDF
document with details is sent to the email address of the client's account.

After this moment, the rent changes its status to _active_ and it is possible to return it. When generating the return
document, a recalculation of prices according to the actual rental time takes place (as in the case of return, a PDF
document is generated and sent to the customer's email address).

## Technology design

Server was mainly created using Jakarta EE and a MySQL relational database. Database migrations were managed by
Liquibase. The ORM system used was Hibernate. The client layer, on the other hand, consisted of JSP + JSTL pages and
Bootstrap with the jQuery library. Blob data (reports and bar codes) are stored in Minio S3 storage with
code-compatibility with AWS S3 API.

I realize that this project could have been done in Spring Boot and Thymeleaf technology, but it was mainly created
to familiarize with "legacy" technologies from the old Java tech stack using best practices as I can
(used ["JSP Model 2"](https://en.wikipedia.org/wiki/JSP_model_2_architecture) approach based on the MVC architecture).

This project is also presentation the posibilities of integration newest code integrity and deployment tools (Docker)
with older approach to developing enterprise-class software.

## Used technologies

- Java (Jakarta) EE 9 with EJB (Enterprise Java Bean),
- MySQL relational database system,
- JSP (views) + JSTL,
- Jakarta Mail Api (mail sender) + Freemarker (templates),
- Hibernate (ORM system) + C3P0 (connection pool) + Liquibase (database migrations),
- Bootstrap, jQuery, PopperJS,
- Barcode4J - bar codes generator,
- iText - PDF documents generator,
- TomEE web server,
- Minio S3 storage (with AWS S3 SDK for Java),
- Docker containers technology.
