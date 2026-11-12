---
kind: "Agregator danych"
summary: "Agregator archiwum telewizyjnego pobierający dane z portalu \"telemagazyn.pl\" i indeksujący je za pomocą wygodniejszego systemu wyszukiwania."
techDetails:
  - "Java SE 17"
  - "Swing UI"
  - "Spring Boot 3"
  - "MySQL oraz JDBC Spring data"
  - "React oraz TanStack Query"
  - "biblioteka komponentów MUI"
downloadUrls:
  - name: "Ekstraktor danych"
    url: "https://github.com/c0ifl4x/tv-archives-aggregator/releases/latest"
  - name: "Obraz docker (wizualizer danych)"
    url: "https://hub.docker.com/r/milosz08/tv-archives-aggregator-client"
---

Głównym celem tego projektu jest ulepszenie systemu archiwizacji programów telewizyjnych i ułatwienie ich wyszukiwania
oraz znajdowania interesujących treści z wybranego okresu z pomocą wygodnej wyszukiwarki pełnotekstowej.

Ten projekt składa się z 3 pod-projektów:

- **app-scrapper** - pobieranie danych ze strony internetowej i zapisywanie ich w bazie danych (desktopowa aplikacja
  Java Swing),
- **app-backend** - Rest API napisane w Spring Boot,
- **app-frontend** - klient internetowy napisany przy użyciu biblioteki React i komponentów MUI.

## Główne funkcje

- udostępnienie możliwości pobierania treści z serwisu telemagazyn.pl i zapisywania ich w określonej strukturze w bazie
  danych MySQL,
- udostępnienie web API dla istniejącego klienta webowego lub innych klientów (np. mobilnych),
- udostępnienie interfejsu użytkownika do wyszukiwania treści według typu programu, kanału telewizyjnego lub gatunku z
  zaawansowanym systemem wyszukiwania i dodatkowymi narzędzia do wizualizacji danych.
