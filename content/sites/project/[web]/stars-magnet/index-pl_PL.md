---
summary: "Klient webowy Angular dla projektu StarsMagnet z API opartym na Django."
techDetails:
  - "Angular 15.1.0"
  - "RxJS"
  - "TypeScript"
  - "NodeJS 18.16.0"
  - "Bootstrap (wraz z Angular Bootstrap)"
repoUrls:
  - name: "Serwer"
    url: "https://github.com/Lettulouz/StarsMagnet"
  - name: "Klient"
    url: "https://github.com/c0ifl4x/stars-magnet-client"
downloadUrls:
  - name: "Obraz docker"
    url: "https://hub.docker.com/r/milosz08/stars-magnet-client"
---

Klient dla [Stars Magnet](https://github.com/Lettulouz/StarsMagnet) Django rest API. Aplikacja umożliwia
rejestrację/logowanie i tworzenie nowych kont firmowych. Każda firma może być oceniona tylko raz przez zalogowanego
użytkownika (ocena ta jest wliczana do średniej). Na podstawie tych wartości możliwe jest filtrowanie firm o najwyższym
poziomie zaufania. Firma ma również możliwość odpowiadania na komentarze.

Nie miałem większego wpływu na rozwiązania biznesowe w aplikacji. Moim zadaniem było jedynie stworzenie warstwy
wizualnej. W mniejszym stopniu wprowadzałem poprawki do serwera i byłem w stałym kontakcie z programistami backend.

## Opis technologii

Projekt został oparty o ekosystem frameworka Angular. Zdecydowałem się nie implementować NgRx (implementacja redux w
ekosystemie Angular) ze względu na stosunkowo prostą architekturę aplikacji. Całą logikę biznesową oparłem na serwisach
i usługach HTTP. Cała aplikacja została podzielona na 3 leniwie ładowane moduły: logowanie i uwierzytelnianie, moduł
główny i moduł commons, który jest wspólny dla dwóch pierwszych modułów.
