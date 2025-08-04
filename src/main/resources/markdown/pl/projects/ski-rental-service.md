---
title: System dla wypożyczalni sprzętu narciarskiego
type: normal
---

{{badge 'kod źródłowy$github$232925$https://github.com/milosz08/ski-rental-service'}}
{{badge 'demo$applearcade$C70039$https://ski.miloszgilga.pl'}}

Aplikacja webowa klasy enterprise dla wypożyczalni sprzętu narciarskiego. Aplikacja ta jest podzielona na 2 główne
moduły, gdzie odpowiednio pierwszy to moduł właściciela a drugi to moduł pracownika (sprzedawcy).

Możliwości **modułu właściciela**:

- dodanie/zmiana/usunięcie konta pracownika (wraz z automatycznym generowaniem/usuwaniem skrzynki pocztowej pracownika),
- dodanie/zmiana/usunięcie sprzętu narciarskiego (ilość, atrybuty, cena za wypożyczenie itp.),
- wgląd w wszystkie wykonane wypożyczenia i zwroty od wszystkich pracowników zakładu.

Możliwości **modułu pracownika**:

- przeglądanie sprzętów narciarskich dodanych przez właściciela,
- dodanie/edycja/usunięcie kont klientów w bazie danych,
- stworzenie wypożyczenia i zwrotu dla wcześniej zapisanych klientów.

## Galeria

{{html 'projectGallery'}}

## Funkcjonalności

Po stworzeniu wypożyczenia, program automatycznie przeliczy cenę na podstawie liczby dni wypożyczenia (która ustawiana
jest przez właściciela dla każdego sprzętu narciarskiego). System przelicza cenę na podstawie najkorzystniejszych
warunków, tj. jeśli sprzęt jest wypoyczony przez więcej niż jeden dzień, pobierze on cene za dzień oraz doliczy do
ostatecznej ceny wszystkie rozpoczęte godziny. Po stworzeniu przez pracownika wypożyczenia na podany adres email klienta
wysyłana jest wiadomość z podsumowaniem w formie dokumentu PDF.

Po udanym stworzeniu wypożyczenia, zmienia ono status na _aktywne_ i jest możliwość jego zwrotu. Podczas generowania
zwrotu przez pracownika, system ponownie przelicza czas wypożyczenia na podstawie cen poszczególnych sprzętów. Podobnie
jak w przypadku wypożyczenia, generowany jest dokument zwrotu wysyłany na adres email klienta.

## Opis technologii

Serwer został stworzony przy użyciu technologii Jakarta EE oraz relacyjnego systemu bazodanowego MySQL. Architektura
została stworzona z wykorzystaniem wzorca MVC, gdzie warstwa modelu (Entity + DAO) została zapewniona przez Hibernate,
View przez JSP oraz JSTL wraz z frameworkiem Bootstrap oraz jQuery a Controller wykorzystując standardowe serwlety
zawarte w specyfikacji Jakarta EE. Raporty PDF oraz kody kreskowe w postaci grafik PNG przechowywane są w systemie Minio
a komunikacja z nim odbywa się przy pomocy API AWS S3 SDK, toteż kod jest kompatybilny z rozwiązaniem S3 od AWS.

Zdaję sobie sprawę, że projekt ten mógł powstać z wykorzystaniem nowszej technologii (Spring Boot i silnik szablonów
Thymeleaf), ale został on głównie stworzony w celu oswojenia się i poznania technologii _legacy_ ze starego stosu Java
ale z wykorzystaniem najlepszych praktyk (wykorzystanie podejścia
["JSP Model 2"](https://en.wikipedia.org/wiki/JSP_model_2_architecture)).

Projekt ten jest również przedstawieniem możliwości integracji nowszych narzędzi do budowy i wdrożenia (Docker) ze
starszymi podejściami tworzenia oprogramowania korporacyjnego.

## Użyte technologie

- Java (Jakarta) EE 9 z EJB (Enterprise Java Bean),
- relacyjny system bazodanowy MySQL,
- JSP (widoki) + JSTL,
- Jakarta Mail Api (klient poczty) + Freemarker (szablony),
- Hibernate (system ORM) + C3P0 (pula połączeń do bazy) + Liquibase (system migracji bazy danych),
- Bootstrap, jQuery, PopperJS,
- Barcode4J - generator kodów kreskowych,
- iText - generator dokumentów PDF,
- serwer aplikacji TomEE,
- system magazynu S3 Minio (wraz z AWS S3 SDK dla Java)
