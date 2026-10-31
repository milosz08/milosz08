---
kind: "Kompilator i symulator"
summary: "Zaawansowany symulator jednotaśmowej, deterministycznej maszyny Turinga z obsługą składni ASM oraz autorskim kompilatorem AoT."
downloadUrls:
  - name: "Obraz docker"
    url: "https://hub.docker.com/r/milosz08/turing-machine-simulator-app"
demoUrls:
  - name: "Strona internetowa"
    url: "https://turing.miloszgilga.pl"
---

Zaawansowany, jednotaśmowy, deterministyczny symulator maszyny Turinga napisany przy użyciu biblioteki React. Dostępny w
trybach jasnym i ciemnym, w zależności od preferencji użytkownika. Ta maszyna jest świetnym fundamentem do zrozumienia,
w jaki sposób działają proste algorytmy oraz jest często spotykana jako wprowadzenie do informatyki na kierunkach
technicznych.

Maszyna (jako koncepcja) posiada nieskończoną taśmę, na której za pomocą głowicy może odczytywać, zapisywać i kasować
prawie wszystkie symbole (z pewnymi zastrzeżeniami). Użytkownik może używać prostego języka à la Assembler (shift, jump
itp.). Maszyna ta, przy zbyt częstym wywoływaniu funkcji rekurencyjnej, zatrzyma się, by przeciwdziałać wyciekowi
pamięci. Cała składnia kompilatora jest opisana na stronie demonstracyjnej projektu.

Ponadto użytkownik może załadować istniejące programy i wyeksportować napisany przez siebie program do pliku .txt w celu
późniejszego załadowania.

Poniżej znajdują się dodatkowe linki, dzięki którym można dowiedzieć się więcej o działaniu hipotetycznej maszyny Alana
Turinga:

* [plato.stanford.edu/entries/turing-machine](https://plato.stanford.edu/entries/turing-machine)
* [en.wikipedia.org/wiki/Turing_machine](https://en.wikipedia.org/wiki/Turing_machine)

## Opis technologii

Projekt został w całości napisany w bibliotece React służącej do budowania interaktywnych interfejsów użytkownika. W
początkowej fazie, do budowania projektu użyłem środowiska Create React App. Z uwagi jednak, że nie jest już ono
wspierane, zdecydowałem napisać własną konfigurację budowania przy użyciu Webpack 5. Użyłem Webpack zamiast Vite.js z
uwagi na możliwość uruchomienie na starszych przeglądarkach niewspierających nowoczesnych modułów ES6.
