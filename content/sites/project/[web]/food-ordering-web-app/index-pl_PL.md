---
summary: "Aplikacja full-stack napisana w PHP, oparta na autorskim, wbudowanym frameworku MVC."
techDetails:
  - "PHP 7.4"
  - "silnik szablonów Mustache"
  - "PHP Mailer"
  - "Bootstrap"
  - "Chart.js"
  - "jQuery"
downloadUrls:
  - name: "Obraz docker"
    url: "https://hub.docker.com/r/milosz08/food-ordering-web-app"
---

Aplikacja internetowa CMS (Content Management System) do obsługi zarządzania restauracjami, kontaktu z klientami i
zamawiania produktów z utworzonych restauracji. Z oczywistych względów aplikacja nie posiada systemu płatności.

## Funkcjonalności

Aplikacja ta umożliwia utworzenie konta użytkownika i właściciela restauracji. Użytkownik może dodawać nowe produkty z
wielu restauracji do koszyka, a właściciel (po zatwierdzeniu utworzonej restauracji przez administratora systemu) może
dodawać, modyfikować i usuwać dania z utworzonej restauracji.

## Opis technologii

Główny rdzeń aplikacji MVC został napisany od podstaw, wykorzystując czysty język PHP, bez wykorzystywania żadnych
dodatkowych bibliotek przede wszystkim ze względów wydajnościowych. Aplikacja działa z bazą danych MySQL w wersji 7.4 i
wyższej.

Moja rola w tym projekcie polegała na wyborze technologii, napisaniu rdzenia aplikacji oraz sukcesywnym sprawdzaniu
dostarczonego kodu i weryfikacji poprawności jego działania. Nierzadko uczestniczyłem również w tworzeniu nowych
funkcjonalności.
