---
summary: "REST API dla aplikacji Android AirHubMaster z obsługą standardów i18n. Napisane w Javie 17 oraz Spring Boot 3."
techDetails:
  - "Java 17"
  - "Spring Boot 3"
  - "MySQL (faza produkcyjna), H2 (faza rozwojowa)"
  - "Liquibase (zarządzanie migracjami bazy danych)"
  - "JMPSL (moduły: communication, core, security)"
  - "Jakarta Mail API + JMPSL Communication + Freemarker (obsługa email)"
  - "dokumentacja Swagger OpenAPI"
  - "kontenery Docker (wykorzystywane tylko w fazie rozwojowej)"
repoUrls:
  - name: "Serwer"
    url: "https://github.com/c0ifl4x/air-hub-master-server"
  - name: "Klient"
    url: "https://github.com/Lettulouz/AirHubMaster"
downloadUrls:
  - name: "Obraz docker"
    url: "https://hub.docker.com/r/milosz08/air-hub-master-server"
---

Serwer Java Spring Boot stworzony dla aplikacji mobilnej [AirHub Master](https://github.com/Lettulouz/AirHubMaster)
zgodny ze specyfikacją Rest API. Zapewnia podstawowe funkcje: logowanie, rejestracja, resetowanie hasła za pomocą tokenu
e-mail i inne specyficzne wymagania aplikacji. Wszystkie dane aplikacji są przechowywane w relacyjnej bazie danych
MySQL. Więcej informacji o tym projekcie można znaleźć [tutaj](https://github.com/Lettulouz/AirHubMaster).

Ta aplikacja korzysta z najnowszej stabilnej wersji biblioteki [JMPS](https://github.com/c0ifl4x/jmpsl) (użyte moduły:
communication, core, security).

## Wsparcie dla wielu języków

Ten projekt obsługuje wiele języków dzięki prostemu skryptowi Bash dostępnemu do tworzenia nowego pliku pakietu zasobów
_i18n_ w zawartości repozytorium projektu. Domyślnie aplikacja obsługuje język polski i angielski. Aby spersonalizować
język w żądaniach HTTP, należy podać nagłówek _Accept-Language_ z tagiem _i18n_ jako wartością. Zmiana nagłówka
_Accept-Language_ wpływa również na generowany język treści wiadomości e-mail.
