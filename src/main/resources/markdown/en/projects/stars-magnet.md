---
title: Stars Magnet (company reviews platform)
type: coop
---

{{badge 'source code-client (Angular)$github$3178c6$https://github.com/milosz08/stars-magnet-client'}}
{{badge 'source code-server (Python)$github$3572A5$https://github.com/Lettulouz/StarsMagnet'}}

Client for [Stars Magnet](https://github.com/Lettulouz/StarsMagnet) Django rest API. This application allows you to
register/log in and create new company accounts. Each company can be rated only once by a logged-in user (this rating is
included in the average). Based on these values, it is possible to filter companies with the highest level of trust. The
company also has the option to respond to comments.

I didn't have much influence on the business solutions in the application. My task was only to create the visual layer.
To a lesser extent, I made improvements to the server and was in constant contact with the backend developers.

## Technology design

This project was based on the Angular framework. I decided not to implement Ngrx (redux store implementation in Angular
ecosystem) due to the relatively simple architecture of the application. I based all business logic on services and HTTP
query services. The whole application was divided into 3 lazily loaded modules: login and authentication, main module
and commons module which is a commonality for the first 2 modules.

## Gallery

{{html 'projectGallery'}}

## Used technologies

- Angular 15.1.0,
- Rxjs,
- TypeScript,
- NodeJS 18.16.0,
- Bootstrap (with Angular Bootstrap).
