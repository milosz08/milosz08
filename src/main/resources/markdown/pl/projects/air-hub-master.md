---
title: AirHub Master (gra mobilna do zarządzania flotą samolotów)
index: 4
type: 'part'
---

[![](<https://img.shields.io/badge/github-klient%20(Android)-b07219?style=for-the-badge&logo=github>)](https://github.com/Lettulouz/AirHubMaster) &nbsp;
[![](<https://img.shields.io/badge/github-serwer%20(Java)-b07219?style=for-the-badge&logo=github>)](https://github.com/milosz08/air-hub-master-api) &nbsp;

Serwer Java Spring Boot stworzony dla aplikacji mobilnej [AirHub Master](https://github.com/Lettulouz/AirHubMaster)
zgodny ze specyfikacją Rest API. Zapewnia podstawowe funkcje: logowanie, rejestracja, resetowanie hasła za pomocą tokenu
e-mail i inne specyficzne wymagania aplikacji. Wszystkie dane aplikacji są przechowywane w relacyjnej bazie danych MySQL.
Więcej informacji o tym projekcie można znaleźć [tutaj](https://github.com/Lettulouz/AirHubMaster).

Ta aplikacja korzysta z najnowszej stabilnej wersji biblioteki [JMPS](https://github.com/milosz08/jmpsl) (użyte moduły:
communication, core, security).

## Galeria

<[GalleryContent}]>

## Wsparcie dla wielu języków

Ten projekt obsługuje wiele języków dzięki prostemu skryptowi Bash dostępnemu do tworzenia nowego pliku pakietu zasobów
_i18n_ w zawartości repozytorium projektu. Domyślnie aplikacja obsługuje język polski i angielski. Aby spersonalizować
język w żądaniach HTTP, należy podać nagłówek _Accept-Language_ z tagiem _i18n_ jako wartością. Zmiana nagłówka
_Accept-Language_ wpływa również na generowany język treści wiadomości e-mail.

## Użyte technologie

- Java 17,
- Spring Boot 3,
- MySQL (faza produkcyjna), H2 (faza rozwojowa),
- Liquibase (zarządzanie migracjami bazy danych),
- JMPSL (moduły: communication, core, security),
- Jakarta Mail API + JMPSL Communication + Freemarker (obsługa email),
- dokumentacja Swagger OpenAPI,
- kontenery Docker (wykorzystywane tylko w fazie rozwojowej).
