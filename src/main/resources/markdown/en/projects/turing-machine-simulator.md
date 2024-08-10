---
title: Turing machine simulator
index: 3
type: 'rd'
---

[![](https://img.shields.io/badge/github-808080?style=for-the-badge&logo=github)](https://github.com/milosz08/turing-machine-simulator) &nbsp;

An advanced, single-tape, deterministic Turing Machine simulator written using the React library. Available in light and
dark modes, depending on user preference. This machine is a great foundation for understanding how computer processing of
algorithms works, found frequently in Computer Science majors at technical universities.

Machine in its conception has an infinite tape on which, with the help of the head, it can read, write and delete almost
all symbols (with some reservations). User can use a simple language a'la Assembler (shift, jump, etc.). This machine,
when calling a recursive function too much, will stop to counteract the memory leak. The entire syntax of the compiler
is described on the project's demo page.

In addition, the user can load existing programs and export the program he has written to a .txt file for later loading.

Below you will find additional links to learn about the hypothetical operation of Alan Turing's machine:
* [plato.stanford.edu/entries/turing-machine](https://plato.stanford.edu/entries/turing-machine)
* [en.wikipedia.org/wiki/Turing_machine](https://en.wikipedia.org/wiki/Turing_machine)

## Gallery

<[GalleryContent}]>

## Technology design

This project was entirely written in the React library used to build interactive user interfaces. In the initial phase, I
used the Create React App environment to build the project. However, due to the fact that it is no longer supported, I
decided to write my own build configuration using Webpack 5. I used Webpack instead of Vite.js due to the possibility of
running on older browsers that do not support modern ES6 modules.

## Used technologies

- React 18,
- React DOM 18,
- React Router 6,
- Typescript,
- Webpack 5,
- Styled components,
- Redux toolkit.
