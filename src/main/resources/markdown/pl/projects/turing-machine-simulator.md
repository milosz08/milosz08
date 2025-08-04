---
title: Symulator maszyny Turinga
type: rAndD
---

{{badge 'kod źródłowy$github$232925$https://github.com/milosz08/turing-machine-simulator'}}
{{badge 'demo$applearcade$C70039$https://turing.miloszgilga.pl'}}

Zaawansowany, jednotaśmowy, deterministyczny symulator maszyny Turinga napisany przy użyciu biblioteki React. Dostępny
w trybach jasnym i ciemnym, w zależności od preferencji użytkownika. Ta maszyna jest świetnym fundamentem do
zrozumienia, w jaki sposób działają proste algorytmy oraz jest często spotykana jako wprowadzenie do informatyki na
kierunkach technicznych.

Maszyna (jako koncepcja) posiada nieskończoną taśmę, na której za pomocą głowicy może odczytywać, zapisywać i kasować
prawie wszystkie symbole (z pewnymi zastrzeżeniami). Użytkownik może używać prostego języka a'la Assembler(shift, jump
itp.). Maszyna ta, przy zbyt częstym wywoływaniu funkcji rekurencyjnej, zatrzyma się, by przeciwdziałać wyciekowi
pamięci. Cała składnia kompilatora jest opisana na stronie demonstracyjnej projektu.

Ponadto użytkownik może załadować istniejące programy i wyeksportować napisany przez siebie program do pliku .txt w celu
późniejszego załadowania.

Poniżej znajdują się dodatkowe linki, dzięki którym można dowiedzieć się więcej o działaniu hipotetycznej maszyny Alana
Turinga:

* [plato.stanford.edu/entries/turing-machine](https://plato.stanford.edu/entries/turing-machine)
* [en.wikipedia.org/wiki/Turing_machine](https://en.wikipedia.org/wiki/Turing_machine)

## Galeria

{{html 'projectGallery'}}

## Opis technologii

Projekt został w całości napisany w bibliotece React służącej do budowania interaktywnych interfejsów użytkownika. W
początkowej fazie, do budowania projektu użyłem środowiska Create React App. Z uwagi jednak, że nie jest już ono
wspierane zdecydowałem napisać własną konfigurację budowania przy użyciu Webpack 5. Użyłem Webpack zamiast Vite.js z
uwagi na możliwość uruchomienie na starszych przeglądarkach niewspierających nowoczesnych modułów ES6.

## Użyte technologie

- React 18,
- React DOM 18,
- React Router 6,
- Typescript,
- Webpack 5,
- Styled components,
- Redux toolkit.
