---
title: Stars Magnet (platforma do wystawiania recenzji)
index: 2
type: coop
---

{{badge:kod źródłowy-klient (Angular)$github$3178c6$https://github.com/milosz08/stars-magnet-client}}
{{badge:kod źródłowy-serwer (Python)$github$3572A5$https://github.com/Lettulouz/StarsMagnet}}

Klient dla [Stars Magnet](https://github.com/Lettulouz/StarsMagnet) Django rest API. Aplikacja umożliwia
rejestrację/logowanie i tworzenie nowych kont firmowych. Każda firma może być oceniona tylko raz przez zalogowanego
użytkownika (ocena ta jest wliczana do średniej). Na podstawie tych wartości możliwe jest filtrowanie firm o najwyższym
poziomie zaufania. Firma ma również możliwość odpowiadania na komentarze.

Nie miałem większego wpływu na rozwiązania biznesowe w aplikacji. Moim zadaniem było jedynie stworzenie warstwy
wizualnej. W mniejszym stopniu wprowadzałem poprawki do serwera i byłem w stałym kontakcie z programistami backendu.

## Opis technologii

Projekt został oparty o ekosystem frameworka Angular. Zdecydowałem się nie implementować Ngrx (implementacja redux w
ekosystemie Angular) ze względu na stosunkowo prostą architekturę aplikacji. Całą logikę biznesową oparłem na serwisach
i usługach HTTP. Cała aplikacja została podzielona na 3 leniwie ładowane moduły: logowanie i uwierzytelnianie, moduł
główny i moduł commons, który jest wspólny dla dwóch pierwszych modułów.

## Galeria

{{html:projectGallery}}

## Użyte technologie

- Angular 15.1.0,
- Rxjs,
- TypeScript,
- NodeJS 18.16.0,
- Bootstrap (wraz z Angular Bootstrap).
