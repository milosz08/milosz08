---
title: Aggregator archiwów telewizyjnych
type: normal
---

{{badge:kod źródłowy$github$808080$https://github.com/milosz08/tv-archive-aggregator}}

## Cel projektu

Głównym celem tego projektu jest ulepszenie systemu archiwizacji programów telewizyjnych i ułatwienie ich wyszukiwania
interesujących treści z danego okresu.

Ten projekt składa się z 3 podprojektów:

- *web-scrapper* - pobieranie danych ze strony internetowej i zapisywanie ich w bazie danych (desktopowa aplikacja Java
  Swing),
- *data-server* - Rest API napisane w Spring Boot,
- *web-ui* - klient internetowy napisany przy użyciu biblioteki React i komponentów MUI.

## Główne funkcje

- udostępnienie możliwości pobierania treści z serwisu telemagazyn.pl i zapisywania ich w określonej strukturze w bazie
  danych MySQL,
- udostępnienie web API dla istniejącego klienta webowego lub innych klientów (np. mobilnych),
- udostępnienie interfejsu użytkownika do wyszukiwania treści według typu programu, kanału telewizyjnego lub gatunku z
  zaawansowanym systemem wyszukiwania i dodatkowymi narzędzia do wizualizacji danych.

## Galeria

{{html:projectGallery}}

## Użyte technologie

- Java SE 17,
- Swing UI,
- Spring Boot 3,
- MySQL oraz JDBC Spring data,
- React oraz Tanstack Query,
- biblioteka komponentów MUI.
